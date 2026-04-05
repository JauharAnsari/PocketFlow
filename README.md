# PocketFlow 💸

[![Kotlin](https://img.shields.io/badge/Kotlin-1.9.0-blue.svg)](https://kotlinlang.org)
[![Android](https://img.shields.io/badge/Platform-Android-green.svg)](https://developer.android.com)
[![Architecture](https://img.shields.io/badge/Architecture-MVVM-orange.svg)](https://developer.android.com/topic/libraries/architecture)
[![License](https://img.shields.io/badge/License-MIT-yellow.svg)](LICENSE)

**PocketFlow** is a premium, high-performance personal finance manager built with a focus on aesthetics, speed, and security. Designed to help you track your expenses, manage budgets, and visualize your financial health with ease.

---

## ✨ Key Features

### 🏢 Premium Dashboard
- Get an instant overview of your Balance, Income, and Expenses.
- Beautiful, interactive charts to visualize spending categories.
- Recent transactions at a glance for quick tracking.

### 🔐 Unmatched Security
- **Biometric Login**: Secure your financial data with integrated Fingerprint and Face ID support.
- **Privacy First**: Secure local-only storage ensures your data never leaves your device.

### ⚡ Smooth Interactions
- **Advanced History**: Search, filter, and sort your transactions with zero lag.
- **Quick Gestures**: 
  - ⬅️ **Swipe Left** to quickly delete a transaction (with Undo support).
  - ➡️ **Swipe Right** to instantly edit a transaction.
- **Splash Screen**: A beautifully animated entry point that sets the tone for a premium experience.

### 📊 Smart Budgeting
- Set monthly limits for different categories (Food, Transport, Bills, etc.).
- Real-time tracking and alerts when you're approaching your budget limits.

---

## 🛠 Tech Stack

- **UI/UX**: Material 3 Design, Lottie Animations, Shimmer Loading, AAChartCore.
- **Core**: Kotlin, Jetpack Components (Navigation, ViewModel, LiveData).
- **Persistence**: Room Database with Coroutines and StateFlow.
- **Image Loading**: Glide for smooth profile and icon rendering.
- **Background Actions**: WorkManager for scheduled notifications and cleanups.

---

## 🚀 Getting Started

### Prerequisites
- Android Studio Hedgehog or newer.
- Android SDK 26 (Oreo) or higher.

### Installation
1. Clone the repository:
   ```bash
   git clone https://github.com/[Your-Username]/PocketFlow.git
   ```
2. Open the project in Android Studio.
3. Sync Project with Gradle Files.
4. Run the app on your emulator or physical device.

---

## 🏗 Project Architecture

PocketFlow follows the **MVVM (Model-View-ViewModel)** architectural pattern to ensure a clean separation of concerns and maintainability.

- **UI Layer**: Fragments and Activities handling the display and user interactions.
- **Domain Layer**: ViewModel managing the UI state and communicating with the Repository.
- **Data Layer**: Repository pattern coordinating data from the Room local database.

---

## 📸 Screenshots

| Splash Screen | Home Dashboard | History (Swipe) |
| :---: | :---: | :---: |
| ![Splash](app/src/main/res/drawable/finance_app_icon.png) | (Add Screenshot) | (Add Screenshot) |

> [!TIP]
> To get the best experience, enable Dark Mode on your device to see the premium gradients in action.

---

## 📄 License
This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

---

## 🤝 Contributing
Contributions are welcome! If you have a feature request or bug report, please open an issue or submit a pull request.

---

**Crafted with ❤️ for better financial future.**
