# PocketFlow 💸

[![Kotlin](https://img.shields.io/badge/Kotlin-1.9.0-blue.svg)](https://kotlinlang.org)
[![Android](https://img.shields.io/badge/Platform-Android-green.svg)](https://developer.android.com)
[![Architecture](https://img.shields.io/badge/Architecture-MVVM-orange.svg)](https://developer.android.com/topic/libraries/architecture)
[![License](https://img.shields.io/badge/License-MIT-yellow.svg)](LICENSE)
[![Download APK](https://img.shields.io/badge/Download-APK-brightgreen?style=for-the-badge&logo=android)](https://drive.google.com/file/d/1Of7-xIJuU99WafAq7kF3o2JMWlBXro5B/view?usp=drivesdk)

**PocketFlow** is a premium, high-performance personal finance manager built with a focus on aesthetics, speed, and security. Designed to help you track your expenses, manage budgets, and visualize your financial health with ease.

<p align="center">
  <a href="https://drive.google.com/file/d/1Of7-xIJuU99WafAq7kF3o2JMWlBXro5B/view?usp=drivesdk">
    <img src="https://img.shields.io/badge/⬇️%20Download%20PocketFlow%20APK-4CAF50?style=for-the-badge&logo=android&logoColor=white" alt="Download APK" height="50"/>
  </a>
</p>

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

**Option 1 — Direct APK Download (Recommended)**

👉 [**Download PocketFlow APK**](https://drive.google.com/file/d/1Of7-xIJuU99WafAq7kF3o2JMWlBXro5B/view?usp=drivesdk)

1. Download the APK from the link above.
2. Enable **Install from Unknown Sources** in your Android settings.
3. Install and launch PocketFlow.

**Option 2 — Build from Source**

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

### ☀️ Light Mode

<p align="center">
  <img src="https://github.com/user-attachments/assets/4cd1c065-d5c0-455a-9679-4f9705d0607c" width="18%" alt="Light Screen 1"/>
  &nbsp;
  <img src="https://github.com/user-attachments/assets/7b0c400a-6352-48f6-ae30-81c78e7dd3ae" width="18%" alt="Light Screen 2"/>
  &nbsp;
  <img src="https://github.com/user-attachments/assets/5b1ee3c2-3a9b-415d-980c-5bed812b05be" width="18%" alt="Light Screen 3"/>
  &nbsp;
  <img src="https://github.com/user-attachments/assets/4f3bd10f-6181-441c-a14d-8fc074105f8a" width="18%" alt="Light Screen 4"/>
  &nbsp;
  <img src="https://github.com/user-attachments/assets/151ca21b-e803-4887-8255-0f36b100eb6c" width="18%" alt="Light Screen 5"/>
</p>

### 🌙 Dark Mode

<p align="center">
  <img src="https://github.com/user-attachments/assets/c34ff5d7-e524-48ed-8a0a-894c3c5e849f" width="18%" alt="Dark Screen 1"/>
  &nbsp;
  <img src="https://github.com/user-attachments/assets/66287d88-74f0-4522-acf7-a6ef27979f37" width="18%" alt="Dark Screen 2"/>
  &nbsp;
  <img src="https://github.com/user-attachments/assets/ff15ae1d-2d14-4fdb-83ad-899ed437ecf6" width="18%" alt="Dark Screen 3"/>
  &nbsp;
  <img src="https://github.com/user-attachments/assets/06e8fe55-0120-4212-aadb-56a3fa9212fc" width="18%" alt="Dark Screen 4"/>
  &nbsp;
  <img src="https://github.com/user-attachments/assets/05efc3b7-7f42-4dc9-b1e0-89f444541593" width="18%" alt="Dark Screen 5"/>
</p>

> [!TIP]
> Enable Dark Mode on your device to experience the premium gradients and refined UI in full effect.

---

---

## 🤝 Contributing

Contributions are welcome! If you have a feature request or bug report, please open an issue or submit a pull request.

---

<p align="center"><b>Crafted with ❤️ for a better financial future.</b></p>
