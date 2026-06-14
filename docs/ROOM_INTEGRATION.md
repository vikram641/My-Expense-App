# Room integration — Expense app

Adds an **offline-first cache** for the expense list. Room becomes the single
source the UI reads from; the API refreshes it. The app now shows cached
expenses instantly (and works offline), then updates when the network call
returns.

## 1. Where each file goes

| File | Destination in your project |
|---|---|
| `ExpenseEntity.kt` | `app/src/main/java/com/example/data/local/` |
| `ExpenseDao.kt` | `app/src/main/java/com/example/data/local/` |
| `ExpenseDatabase.kt` | `app/src/main/java/com/example/data/local/` |
| `ExpenseMappers.kt` | `app/src/main/java/com/example/data/local/` |
| `DatabaseModule.kt` | `app/src/main/java/com/example/data/api/` (next to `AppModule.kt`) |
| `Repository.kt` | replaces `app/src/main/java/com/example/data/model/Repository.kt` |
| `build.gradle.kts` | replaces `app/build.gradle.kts` |
| `libs.versions.toml` | replaces `gradle/libs.versions.toml` |

The `data/local` package is new — create the folder. Everything else slots
into existing folders.

## 2. What changed in the files you already had

- **`libs.versions.toml`** — added `room = "2.6.1"` and three Room library
  entries (`room-runtime`, `room-ktx`, `room-compiler`).
- **`app/build.gradle.kts`** — added the Room block under Hilt. Uses **kapt**
  (same as your Hilt/Glide setup), so no new plugin is needed.
- **`Repository.kt`** — added `expenseDao` to the constructor and four cache
  methods at the bottom (`observeCachedExpenses`, `cacheExpenses`,
  `clearExpenseCache`, `refreshExpenses`). All existing methods are untouched.
  Hilt injects the DAO automatically via `DatabaseModule`.

## 3. Using it from a ViewModel (offline-first)

`ExpensesViewModel` currently overwrites a single StateFlow from the API.
The offline-first version observes Room for display and calls `refreshExpenses`
to update it:

```kotlin
@HiltViewModel
class ExpensesViewModel @Inject constructor(
    private val repository: Repository
) : ViewModel() {

    // UI collects this — comes straight from Room, survives offline.
    val expenses: StateFlow<List<ExpenseX>> =
        repository.observeCachedExpenses()
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Surface network errors without hiding cached data.
    private val _refreshState = MutableStateFlow<UiState<*>>(UiState.Idle)
    val refreshState: StateFlow<UiState<*>> = _refreshState

    fun refresh(query: ExpenseQuery = ExpenseQuery()) {
        viewModelScope.launch {
            _refreshState.value = UiState.Loading
            _refreshState.value = repository.refreshExpenses(query)
        }
    }
}
```

Needed imports: `kotlinx.coroutines.flow.*`, `androidx.lifecycle.viewModelScope`,
`kotlinx.coroutines.launch`.

## 4. Using it from the Fragment

```kotlin
viewLifecycleOwner.lifecycleScope.launch {
    viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
        launch {
            viewModel.expenses.collect { list ->
                // your existing grouping helper still works on List<ExpenseX>
                val items = Utils().mapExpenses(list)
                adapter.submitList(items)
            }
        }
        launch {
            viewModel.refreshState.collect { state ->
                if (state is UiState.Error) { /* show snackbar, keep cache */ }
            }
        }
    }
}

// kick a refresh on open / on swipe-to-refresh
viewModel.refresh()
```

## 5. Notes / decisions

- **Why flatten `CategoryX`?** Room stores primitive columns. The nested
  category becomes `categoryId/Name/Color/Icon`, and `ExpenseMappers.kt`
  rebuilds the `CategoryX` object on the way out — your UI code keeps working
  with `ExpenseX` unchanged.
- **`receiptUrl`** is nullable in the entity (`String?`) but maps back to `""`
  to match `ExpenseX.receiptUrl: String`.
- **`fallbackToDestructiveMigration()`** is set for dev convenience — it wipes
  the DB on a schema change instead of crashing. Swap in real migrations before
  release.
- **kapt vs KSP:** kept kapt to match your existing Hilt/Glide config. If you
  later move to KSP, replace `kapt(libs.room.compiler)` with
  `ksp(libs.room.compiler)` and add the KSP plugin.

## 6. Sync

After editing the Gradle files: **Sync Project with Gradle Files**, then build.
The first run creates `expense.db`; you can inspect it with Android Studio's
**App Inspection → Database Inspector**.

---

# Add-on: Offline "add expense" queue + sync

Lets users add expenses while offline. Each add either reaches the server
(**Synced**) or drops into a local Room queue (**Queued**) that drains
automatically when connectivity returns.

## New / changed files

| File | Destination | Status |
|---|---|---|
| `PendingExpenseEntity.kt` | `data/local/` | new |
| `PendingExpenseDao.kt` | `data/local/` | new |
| `PendingExpenseMappers.kt` | `data/local/` | new |
| `NetworkMonitor.kt` | `data/local/` | new |
| `AddExpenseResult.kt` | `data/local/` | new (sealed result + `SyncResult`) |
| `ExpenseDatabase.kt` | `data/local/` | **updated** — version `2`, 2nd entity + DAO |
| `DatabaseModule.kt` | `data/api/` | **updated** — provides `PendingExpenseDao` |
| `Repository.kt` | `data/model/` | **updated** — queue/sync methods + 2 new ctor deps |

## Manifest — add this permission

`NetworkMonitor` reads connectivity state. Add next to your existing
`INTERNET` permission in `AndroidManifest.xml`:

```xml
<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
```

## DB version bump

`ExpenseDatabase` is now version `2`. Because `fallbackToDestructiveMigration()`
is on, upgrading from v1 wipes the local cache on first launch (fine in dev).
Add a real `Migration(1, 2)` that `CREATE TABLE pending_expenses ...` before
release if you need to preserve cached data.

## Repository API you now have

```kotlin
suspend fun addExpenseOfflineFirst(request: AddExpenseRequest): AddExpenseResult
suspend fun syncPendingExpenses(): SyncResult
fun observePendingCount(): Flow<Int>
```

## Wiring AddExpenseViewModel

Swap the existing `addExpense(...)` to use the offline-first path:

```kotlin
private val _addResult = MutableStateFlow<AddExpenseResult?>(null)
val addResult: StateFlow<AddExpenseResult?> = _addResult

// live badge of unsynced items
val pendingCount = repository.observePendingCount()
    .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

fun addExpense(request: AddExpenseRequest) {
    viewModelScope.launch {
        _addResult.value = repository.addExpenseOfflineFirst(request)
    }
}

fun syncPending() {
    viewModelScope.launch { repository.syncPendingExpenses() }
}
```

## Reacting in the Fragment

```kotlin
lifecycleScope.launch {
    viewModel.addResult.collect { result ->
        when (result) {
            is AddExpenseResult.Synced -> { /* toast "Saved", pop back */ }
            AddExpenseResult.Queued     -> { /* toast "Saved offline, will sync" */ }
            is AddExpenseResult.Failed  -> { /* show result.message */ }
            null -> Unit
        }
    }
}
```

## When to call sync

`syncPendingExpenses()` is safe to call anytime; it no-ops when offline. Good
trigger points:

- App start (e.g. in your splash/home `ViewModel.init` or `MainActivity`)
- Swipe-to-refresh on the expense list
- When connectivity returns

The simplest robust trigger is a `ConnectivityManager.NetworkCallback` that
calls `viewModel.syncPending()` on `onAvailable`. For guaranteed background
sync that survives app death, a `WorkManager` `OneTimeWorkRequest` with a
`NetworkType.CONNECTED` constraint is the production-grade option — say the
word and I'll add it.

## Design notes

- **No poison queue:** when online, a server *rejection* returns `Failed`
  rather than queuing, so a bad request can't get stuck retrying forever. Only
  genuine offline adds are queued. Input is already validated by
  `Utils.validateExpenseInput` upstream.
- **FIFO + stop-on-failure:** sync drains oldest-first and stops at the first
  failure to preserve order and avoid hammering a flaky server.
- After a successful sync you may want to call `refreshExpenses(...)` so the
  newly-synced items appear in the cached list with their server ids.
