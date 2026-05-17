# MediaTracker

MediaTracker is a social media application designed for tracking personal libraries of books, movies, and TV shows. Users can manage their progress, discover new media, and see what their friends are currently consuming.

## 🚀 Features

- **Personal Library:** Track media items with statuses like "Want to", "In Progress", and "Finished".
- **Social Feed:** Stay updated with activities from people you follow.
- **User Profiles:** Customize your profile with a bio, display name, and avatar.
- **Media Discovery:** View details about various media items including ratings and genres.
- **Connections:** Follow other users to build your social circle.

## 🛠 Tech Stack

- **Language:** [Kotlin](https://kotlinlang.org/)
- **UI Framework:** [Jetpack Compose](https://developer.android.com/jetpack/compose) with Material 3
- **Asynchronous Programming:** [Coroutines](https://kotlinlang.org/docs/coroutines-overview.html) & [Flow](https://kotlinlang.org/docs/flow.html)
- **Architecture:** MVVM (Model-View-ViewModel)
- **Image Loading:** [Coil](https://coil-kt.github.io/coil/)
- **Networking:** [Retrofit](https://square.github.io/retrofit/) & [OkHttp](https://square.github.io/okhttp/) (Integration ongoing)
- **Local Storage:** [DataStore Preferences](https://developer.android.com/topic/libraries/architecture/datastore)

## 🏗 Project Structure

- `data/`: Contains models and repositories. Currently utilizes a `FakeMediaRepository` for prototyping.
- `ui/`: Compose-based screens and ViewModels organized by feature:
  - `activity/`: Social feed components.
  - `connections/`: Follower/Following management.
  - `detail/`: Media detail views.
  - `library/`: User library management.
  - `profile/`: User profile and settings.

## 🚦 Getting Started

### Prerequisites

- Android Studio (Ladybug or newer recommended)
- Android SDK 34+
- Java 22

### Installation

1. Clone the repository.
2. Open the project in Android Studio.
3. Sync the project with Gradle files.
4. Run the `app` module on an emulator or physical device (Min SDK 26).

## 📝 Current Status

The project is currently in active development. Most features are currently powered by mock data via the `FakeMediaRepository` while the backend API integration (Retrofit) is being finalized in the coming milestones.
