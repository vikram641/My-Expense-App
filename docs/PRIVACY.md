# Privacy Policy — FixMoney: AI Money Manager

_Last updated: 18 October 2026_

FixMoney ("the app") is developed and maintained by an independent developer. This policy explains what information the app uses, where it goes, and why.

**In short:** there is no sign-up and no account. Your expenses, budgets and profile are stored **only on your phone**. A few features send a small amount of text to our AI service to work; these are listed below.

## Data stored only on your device

- **Expenses, budgets, categories and notes** you add, and your **profile** (name, monthly budget, currency, avatar). These are kept in a local database on your phone. We do not have a server copy, so uninstalling the app or clearing its data deletes them. Android's own device backup may include them if you have it switched on.
- **CSV exports** you create are written to your phone's Downloads folder.

## SMS auto-detect (Android, India only, optional)

If you turn on **SMS Auto-Detect** (during setup or in Settings) and grant the SMS permission, the app receives new incoming SMS so it can spot payment messages from banks and UPI apps (for example "Rs 250 debited … to SWIGGY").

- SMS are read **on your phone only**. The text of an SMS is **never uploaded, stored, or shared** with us or anyone else.
- The app only looks at messages from bank / payment sender IDs. Messages from personal phone numbers, OTPs, and credit, refund, due-date or ATM messages are ignored.
- From a payment SMS the app keeps only the **amount, payee name, a suggested category and the date**, so it can show you a notification asking "Add it to FixMoney?". **Nothing is added to your expenses until you tap Add.** Ignored suggestions are discarded.
- The app does not read your existing SMS inbox (it does not use the READ_SMS permission).
- You can turn this off at any time in Settings, or revoke the SMS permission in Android settings.

## AI features (Fixoo chat, voice add, receipt scan)

These features use an AI language model. To answer, the app sends text to our AI service, which runs on Cloudflare Workers and uses Groq (and, as a fallback, Cloudflare Workers AI) to process it:

- **Fixoo chat:** your question and a summary of your spending (totals and categories for the current and previous month, no personal identifiers).
- **Voice add:** the words you spoke, as text. Speech is turned into text by your phone's speech recognition service (for example Google). Simple phrases such as "150 chai" are understood on your phone and are not sent at all.
- **Receipt scan:** the photo stays on your phone. Text is read from it on the device (Google ML Kit / Apple Vision), and only that text is sent.

This text is used only to produce the answer. We do not use it to build a profile of you and do not sell it.

## Fixoo Coins, ads and Premium

- To keep track of Fixoo Coins and your free trial without an account, the app registers an **anonymous device key** (a one-way hash of your device's Android ID) with our service and receives a random account ID. The service stores your coin balance, trial dates, usage counts and subscription status against that ID. Your network IP address is used briefly to limit how many new wallets can be created from one network per day.
- **Rewarded video ads** are served by **Google AdMob**. AdMob may use your device's advertising ID and other data as described in [Google's Privacy Policy](https://policies.google.com/privacy). Where required (for example in the EEA/UK), you are asked for consent through Google's consent form.
- **Fixoo Premium** is sold through **Google Play Billing**. We receive a purchase token from Google Play to verify the subscription; we never see your payment details.

## Permissions

- **SMS (receive):** SMS auto-detect, described above. Optional.
- **Microphone:** voice add and the floating voice bubble. Optional.
- **Camera:** receipt scan. Optional.
- **Display over other apps:** the floating voice bubble. Optional.
- **Notifications:** payment suggestions, daily reminders and the weekly summary. These are generated on your phone.
- **Internet:** AI features, coins, ads, app settings.

## Other services

The app downloads the list of expense categories and app settings from Google Firebase (Cloud Firestore). No personal data is sent with these requests.

## Data retention and deletion

Your expenses and profile live on your phone; delete them by clearing the app's data or uninstalling. Wallet records (anonymous account ID, coins, subscription status) are kept while the app is in use. To have your wallet record deleted, email us with the account ID shown in the app and we will remove it.

## Children

FixMoney is not directed at children under 13, and we do not knowingly collect information from children under 13.

## Changes to this policy

We may update this policy. Changes will be posted on this page with a new "Last updated" date.

## Contact

Questions about this policy or your data: vikram8575229@gmail.com
