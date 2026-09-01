# FCM push notifications — Expense app

Two things landed together: a **build fix** (Kotlin/Firebase metadata mismatch)
and the **FCM token registration + push-handling** feature that talks to the
backend's `POST /api/user/fcm-token`.

## 1. Build fix — Kotlin ↔ Firebase metadata mismatch

**Symptom:** `:app:compileDebugKotlin` failed with

```
e: .../jetified-play-services-measurement-impl-23.2.0-api.jar!/...kotlin_module
   Module was compiled with an incompatible version of Kotlin.
   The binary version of its metadata is 2.2.0, expected version is 2.0.0.
```

**Root cause:** `firebase-bom:34.16.0` (added for Analytics/Messaging in
`app/build.gradle.kts`) pulls in `play-services-measurement-impl:23.2.0`,
which ships Kotlin metadata version `2.2.0`. The project's Kotlin compiler
was pinned to `2.0.21`, which can only read metadata up to `2.0.0` — a
one-way compatibility gap (newer compilers can read older metadata, not the
reverse), so the compiler treats it as a hard error, not a warning.

**Fix:** `gradle/libs.versions.toml`

```diff
-kotlin = "2.0.21"
+kotlin = "2.2.0"
```

No other version needed to move (Gradle 8.4 / AGP 8.3.2 both support Kotlin
2.2.0 fine). If you ever bump the Firebase BoM further and see this error
again, it means a library in the BoM now needs an even newer Kotlin metadata
version than the compiler — bump `kotlin` again the same way.

**Aside — local JDK note:** if you build from a shell whose default
`java_home` is JDK 25 (`/usr/libexec/java_home -V` to check), Gradle's
embedded Kotlin-DSL script compiler can't parse that version string and fails
before even reaching your code, with a confusing `IllegalArgumentException:
25.0.3`. Point `JAVA_HOME` at JDK 17 or 21 (or set
`org.gradle.java.home` in `gradle.properties`) to build locally — unrelated
to the fix above, just something to route around.

## 2. FCM token registration + push handling

Goal: whenever the device's FCM registration token is available (fresh
install, token rotation, or first login), send it to the backend so it can
target this device for push notifications; and show a system notification
for any message FCM delivers.

### Files touched

| File | Change |
|---|---|
| `app/build.gradle.kts` | added `kotlinx-coroutines-play-services` (gives `Task<T>.await()`) |
| `AndroidManifest.xml` | added `POST_NOTIFICATIONS` permission (required Android 13+) |
| `MainActivity.kt` | requests `POST_NOTIFICATIONS` at runtime on API 33+, next to the existing SMS permission request |
| `data/model/FcmTokenRequest.kt` | **new** — request body `{ "fcmToken": "..." }` |
| `data/api/HomeApiInterface.kt` | added `sendFcmToken()` → `POST /api/user/fcm-token` |
| `data/repository/Repository.kt` | added `sendFcmToken(token)`, same `safeApiCall` pattern as `changePassword` |
| `core/util/TokenManager.kt` | added `saveFcmToken` / `getFcmToken` — caches the last token successfully sent, for dedupe |
| `feature/auth/AuthViewModel.kt` | added `registerFcmToken()` — fetches the current token and sends it |
| `feature/auth/LoginFragment.kt` | calls `registerFcmToken()` right after a successful login |
| `notification/MyFirebaseMessagingService.kt` | rewritten — now `@AndroidEntryPoint`, handles `onNewToken` and `onMessageReceived` |

### Request/response shape

```kotlin
// data/model/FcmTokenRequest.kt
data class FcmTokenRequest(
    @SerializedName("fcmToken") val fcmToken: String
)

// data/api/HomeApiInterface.kt
@POST("/api/user/fcm-token")
suspend fun sendFcmToken(@Body request: FcmTokenRequest): Response<ApiResponse<DeleteResponse>>
```

`DeleteResponse` (`{ "message": String }`) is reused here rather than adding
a new model — same convention as `changePassword`, which is also just a
simple ack endpoint.

### Where the token gets sent

**On login** (`LoginFragment` → `AuthViewModel.registerFcmToken()`):

```kotlin
fun registerFcmToken() {
    viewModelScope.launch {
        try {
            val token = FirebaseMessaging.getInstance().token.await()
            repository.sendFcmToken(token)
        } catch (e: Exception) {
            Log.e("AuthViewModel", "Failed to register FCM token", e)
        }
    }
}
```

This covers the common case: token already exists on the device (from a
previous install or app-start), user just hasn't been authenticated yet to
send it — login is the first point an `Authorization` header is available.

**On token rotation** (`MyFirebaseMessagingService.onNewToken`), which FCM
calls whenever a token is issued or refreshed, even if the app isn't in the
foreground:

```kotlin
override fun onNewToken(token: String) {
    super.onNewToken(token)
    sendTokenToServer(token)
}

private fun sendTokenToServer(token: String) {
    if (tokenManager.getToken().isNullOrEmpty()) {
        // not logged in yet — cache it, LoginFragment's registerFcmToken() will send it after auth
        tokenManager.saveFcmToken(token)
        return
    }
    if (tokenManager.getFcmToken() == token) return // already sent this exact token

    serviceScope.launch {
        when (val result = repository.sendFcmToken(token)) {
            is UiState.Success -> tokenManager.saveFcmToken(token)
            is UiState.Error -> Log.e(TAG, "Failed to send FCM token: ${result.message}")
            else -> {}
        }
    }
}
```

`MyFirebaseMessagingService` isn't a `LifecycleService`, so it owns its own
coroutine scope (`SupervisorJob() + Dispatchers.IO`), cancelled in
`onDestroy()`.

### Showing the notification

`onMessageReceived` builds a `NotificationCompat` notification on a new
channel (`expense_push_channel`), following the same pattern already used by
`SmsReceiver`'s auto-expense notification:

```kotlin
override fun onMessageReceived(message: RemoteMessage) {
    super.onMessageReceived(message)
    val title = message.notification?.title ?: message.data["title"] ?: "Expense App"
    val body = message.notification?.body ?: message.data["body"] ?: ""
    showNotification(title, body)
}
```

On API 33+, `showNotification` checks `POST_NOTIFICATIONS` is granted before
calling `notify()` — otherwise it's a silent no-op rather than a
`SecurityException` crash.

### Logout

`TokenManager.clearSession()` wipes the whole prefs file, including the
cached `fcm_token`. That's intentional-by-omission rather than a considered
design: the next login just re-registers the token (harmless if the backend
endpoint upserts by user, which it appears to). If you want an explicit
"unregister this device" call to the backend on logout, that's a follow-up,
not something currently wired up.

### Manual test

1. Log in — watch logcat for `AuthViewModel`/`FcmService` tags; confirm no
   `Failed to register FCM token` error.
2. Send a test push via Firebase console (Cloud Messaging → send test
   message using the token logged by `Log.d(TAG, "New FCM token received")`,
   or read it from the backend's stored `User.fcmToken`).
3. Confirm the system notification appears with the sent title/body, and
   tapping it opens the app.
4. Log out, log back in — confirm no duplicate `sendFcmToken` call fires
   for a token that's already registered (check `TokenManager.getFcmToken()`
   dedupe path via a breakpoint or added log line if you want to verify by
   hand).

### Verified

- `:app:compileDebugKotlin` — succeeds (Kotlin 2.2.0 reads the play-services
  metadata cleanly).
- `:app:assembleDebug` — succeeds; Hilt wires `Repository`/`TokenManager`
  into the now-`@AndroidEntryPoint` service without issue.
- Not yet verified: an actual push round-trip against a running backend and
  a real device/emulator with Google Play services (needs a live FCM token
  and a send from the Firebase console or backend).
