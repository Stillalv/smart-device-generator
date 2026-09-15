# Smart Device Generator

Native Android application built with Jetpack Compose, Material 3, and MVVM architecture to generate realistic synthetic Android device profiles rooted in real historical and modern hardware specifications, covering Android 1.0 (API 1) to Android 17 (API 37).

## Tech Stack
- **Kotlin** & **Coroutines** & **StateFlow**
- **Jetpack Compose** & **Material 3**
- **MVVM Architecture**
- **Local Static Dataset** (`assets/devices.json`)
- **Offline-First** (Zero backend or network calls)
- **Min SDK**: 21 (Android 5.0 Lollipop)
- **Compile / Target SDK**: 35

## Key Features
- **Comprehensive Android History**: All 37 Android API levels (Android 1.0 to Android 17).
- **Extensive Device Dataset**: Authentic specifications across 40+ manufacturers (Google, Samsung, HTC, LG, Sony, Motorola, Nokia, Xiaomi, OnePlus, OPPO, vivo, realme, ASUS, Nothing, Fairphone, etc.).
- **Hardware & Build Consistency**: SoC, CPU, GPU, display resolutions, RAM/storage variants, and manufacturer fingerprint templates are tightly coupled to authentic device profiles.
- **Synthetic ID Generation**: 16-hex Android IDs and alphanumeric serial numbers generated strictly without touching user device data.
- **Premium iPhone-Style Utility UI**: Soft neutral surfaces, rounded cards (16-24dp), clean typography, zero emojis (Lucide vector icons), and seamless dark mode.
- **Search & Filters**: Search by model, brand, or SoC, and filter by CPU architecture (ARM64, ARM, x86) and refresh rate.
- **Export & History**: Local persistence, detail inspection, Copy All, Export JSON, and native Android Share intent.
- **Unit & Stress Testing**: Unit tests including a 10,000 profile generation stress test.

## Build & Run
```bash
./gradlew test
./gradlew assembleDebug
```
