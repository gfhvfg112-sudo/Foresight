<div dir="rtl">

# 🕐 Foresight / فورسایت

**Never let something important expire again.**
**هرگز اجازه ندهید چیز مهمی منقضی شود.**

---

</div>

[![Kotlin](https://img.shields.io/badge/Kotlin-2.1.0-7F52FF?logo=kotlin)](https://kotlinlang.org)
[![Jetpack Compose](https://img.shields.io/badge/Jetpack%20Compose-BOM%202024-4285F4)](https://developer.android.com/jetpack/compose)
[![Material 3](https://img.shields.io/badge/Material%203-Dynamic%20Color-4285F4)](https://m3.material.io)
[![Room](https://img.shields.io/badge/Room%20DB-2.6.1-3DDC84)](https://developer.android.com/training/data-storage/room)
[![Hilt](https://img.shields.io/badge/Hilt%20DI-2.53-689F63)](https://dagger.dev/hilt/)
[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](LICENSE)

<div dir="rtl">

## 📱 درباره اپلیکیشن

**فورسایت** یک اپلیکیشن اندرویدی برای پیگیری تاریخ انقضای همه چیز در زندگی شماست — از مواد غذایی و دارو گرفته تا مدارک، اشتراک‌ها، بیمه و گارانتی‌ها.

### ✨ امکانات کلیدی

</div>

| Feature | Description |
|---------|-------------|
| 🌍 **Bilingual** | English + فارسی with full RTL support |
| 📋 **Universal Tracker** | Track expiry dates for food, medicine, documents, subscriptions, insurance, warranties, household items |
| 📸 **Smart OCR** | Scan labels/receipts with ML Kit to auto-extract expiry dates |
| 📊 **Barcode Scanner** | Scan barcodes to auto-fill product info via Open Food Facts |
| 🔔 **Smart Alerts** | Configurable notifications: 30, 14, 7, 3, 1 day(s) before expiry |
| 📊 **Dashboard** | At-a-glance view of expiring items, expired items, and total count |
| 🔁 **Recurring Items** | Auto-create new items when they expire |
| 📱 **Widgets** | Home screen widgets for quick access |
| 🌙 **Dark Mode** | Full dark theme support with Material 3 dynamic colors |
| ☁️ **Export** | Export data as CSV/JSON |

<div dir="rtl">

## 🏗️ معماری پروژه

</div>

```
app/src/main/java/com/foresight/app/
├── ForesightApplication.kt     # Hilt Application + WorkManager
├── MainActivity.kt             # Single Activity + Compose
├── data/
│   ├── local/
│   │   ├── ForesightDatabase.kt
│   │   ├── entity/             # Room entities (Item, Category, Alert, etc.)
│   │   ├── dao/                # Data Access Objects
│   │   ├── relations/          # Room relations (ItemWithCategory, etc.)
│   │   └── seed/               # Seed data for default categories
│   └── model/                  # Enums (ItemStatus)
├── di/                          # Hilt modules
├── repository/                  # Repository layer
├── ui/
│   ├── components/             # Shared composables
│   ├── navigation/             # Navigation graph + routes
│   ├── screens/
│   │   ├── home/               # Dashboard + item list
│   │   ├── addedit/            # Add/Edit item form
│   │   ├── detail/             # Item detail view
│   │   ├── categories/         # Category grid + detail
│   │   ├── search/             # Search with debounce
│   │   ├── alerts/             # Expiry alerts
│   │   ├── settings/           # App settings
│   │   ├── onboarding/         # First-time onboarding
│   │   └── premium/            # Premium upgrade screen
│   └── theme/                  # Material 3 theme
├── util/                        # DateUtils, ExpiryUtils
└── worker/                      # Background expiry checks + notifications
```

<div dir="rtl">

## 🛠️ تکنولوژی‌ها

</div>

| Layer | Technology |
|-------|-----------|
| Language | Kotlin 2.1 |
| UI | Jetpack Compose + Material 3 |
| Architecture | MVVM + Clean Architecture |
| DI | Hilt (Dagger) |
| Database | Room (SQLite) |
| OCR | Google ML Kit |
| Camera | CameraX |
| Background | WorkManager |
| Navigation | Navigation Compose |
| Image Loading | Coil |
| Build | Gradle Kotlin DSL |

<div dir="rtl">

## 🚀 اجرا

</div>

### Prerequisites
- Android Studio Hedgehog (2023.1) or newer
- JDK 17+
- Android SDK 35

### Build & Run
```bash
git clone https://github.com/YOUR_USERNAME/Foresight.git
cd Foresight
./gradlew assembleDebug
```

The APK will be at `app/build/outputs/apk/debug/app-debug.apk`.

Or open in Android Studio → **Run ▶** on your device/emulator.

<div dir="rtl">

## 📱 صفحات اپلیکیشن

</div>

| Screen | Description |
|--------|-------------|
| 🏠 **Home** | Dashboard with summary cards, expiring items, all items list |
| 📂 **Categories** | Grid view of all categories with color-coded badges |
| 🔍 **Search** | Real-time search with debounce filtering |
| 🔔 **Alerts** | Expired and expiring-soon items |
| ➕ **Add/Edit** | Form with date picker, category selector, recurring toggle |
| 📋 **Detail** | Full item view with actions (discard/replace) |
| ⚙️ **Settings** | Notifications, alert timing, about, export |
| ⭐ **Premium** | Feature list + pricing |

<div dir="rtl">

## 📝 مجوز

</div>

Apache License 2.0

---

<div dir="rtl">

**ساخته شده با ❤️ توسط فورسایت**

</div>
