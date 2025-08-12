# NoteTalk 📝

NoteTalk is a modern, offline-first note-taking application for Android, built entirely with Jetpack Compose and following clean architecture principles. This project showcases a robust, feature-rich application that is scalable, maintainable, and testable.

## ✨ Features

-   **Create, Read, Update & Delete Notes:** Full CRUD functionality for managing notes.
-   **Offline First:** All notes are saved locally using a Room database.
-   **Real-time Search:** Instantly filter notes by title or content.
-   **Dynamic Theming:** Choose between Light, Dark, and System Default themes.
-   **Modern UI:** A clean, animated, and user-friendly interface built with Material 3.

## 📸 Screenshots(Updated Version)
| Light Mode                                      
| ----------------------------------------------- 
| [Light Mode Screenshot](https://github.com/badal-pundir/NoteTalk-Android/tree/master/Screenshots/NewLightTheme)

## 📸 Screenshots(Older Version)
| Light Mode                                      
| ----------------------------------------------- 
| [Light Mode Screenshot](https://github.com/badal-pundir/NoteTaking-Android/tree/master/Screenshots/LightTheme)

| Dark Mode                                      
| ----------------------------------------------- 
| [Dark Mode Screenshot](https://github.com/badal-pundir/NoteTaking-Android/tree/master/Screenshots/DarkTheme)

## 🛠️ Tech Stack & Architecture

-   **UI:** Jetpack Compose
-   **Architecture:** MVVM (Model-View-ViewModel)
-   **Asynchronicity:** Kotlin Coroutines & Flow
-   **Dependency Injection:** Hilt
-   **Database:** Room Persistence Library
-   **Navigation:** Jetpack Navigation Component for Compose
-   **User Preferences:** Jetpack DataStore
-   **Testing:** JUnit4, MockK (Unit Tests), Compose UI Tests

## 🏗️ Architecture

The app follows a clean architecture pattern, separating concerns into distinct layers:

-   **UI Layer:** Displays application data on the screen and handles user interaction.
-   **ViewModel Layer:** Manages UI-related state and exposes it to the UI.
-   **Repository Layer:** Acts as a single source of truth for data, abstracting the data source from the ViewModel.
-   **Data Layer:** Handles data persistence using the Room database.

## 🚀 How to Build

1.  Clone the repository: git clone `git clone https://github.com/badal-pundir/NoteTalk-Android.git`
2.  Open the project in Android Studio.
3.  Let Gradle sync and build the project.

## Note
_UI Testing may cause some errors_
