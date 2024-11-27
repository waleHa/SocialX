# SocialX - A Modern Social Media Feed App

Welcome to **SocialX**, a social media feed application developed as an assignment. SocialX provides a seamless and interactive experience for users to browse, like, and comment on posts, all within a beautifully designed interface that provides very basic functionality.

---

## Introduction

SocialX is designed to mimic the core functionalities of social media platforms, providing users with a scrollable feed of visually appealing posts, likes, and comments. While developed as an assignment, SocialX showcases advanced techniques in Android development, focusing on performance optimization, efficient memory management, robust network handling, and complex UI interactions.

**Note**: The APIs used in this app are for demonstration purposes and utilize dummy data to simulate real-world interactions.

**Sample**:
<img width="329" alt="image" src="https://github.com/user-attachments/assets/a4a31f27-56b8-487a-8f10-ca12e50ab55f">


---

## Key Features

### Scrollable Feed of Posts with Images

#### Description

Users can browse through an infinite feed of posts featuring high-quality images. The feed supports smooth scrolling and efficient data loading, providing a seamless user experience.

#### Implementation Details

- **Paging 3 Library**: Used for efficient data pagination and infinite scrolling.
- **LazyColumn**: Jetpack Compose component for rendering lists with minimal recomposition.
- **PostsPagingSource**: Custom `PagingSource` implementation to fetch data from the API and cache it locally.

#### Associated Files

- **UI Layer**:
    - `HomeScreen.kt`: Renders the feed using `LazyColumn` and integrates with the ViewModel.
    - `PostItem.kt`: Composable for displaying individual posts, including images and user information.
- **Domain Layer**:
    - `GetPostsUseCase.kt`: Use case for fetching paginated posts.
    - `PostMapper.kt`: Maps data models between layers.
- **Data Layer**:
    - `PostsPagingSource.kt`: Handles data loading and pagination logic.
    - `PostRepositoryImpl.kt`: Repository implementation fetching posts from remote and local data sources.
    - `PostService.kt`: Retrofit service interface defining API endpoints for posts.

### Like/Unlike Functionality

#### Description

Engage with posts by liking or unliking them. The UI updates immediately upon interaction, providing instant feedback.

#### Implementation Details

- **Optimistic Updates**: UI state updates immediately before confirming the operation with the backend.
- **State Management**: Utilizes `MutableStateFlow` to manage the like state efficiently.
- **Local Caching**: Likes are stored locally using Room database to reflect the state even when offline.

#### Associated Files

- **UI Layer**:
    - `HomeViewModel.kt`: Manages the like state and handles user interactions.
    - `PostItem.kt`: Provides the UI elements for like/unlike interactions.
- **Domain Layer**:
    - `LikePostUseCase.kt`: Use case for toggling the like state of a post.
- **Data Layer**:
    - `PostRepositoryImpl.kt`: Updates the like information in the local database.
    - `LikeDao.kt` and `LikeEntity.kt`: Manage like data persistence.

### Comment Section

#### Description

View and add comments to posts, fostering user interaction. Comments are loaded dynamically and can be added seamlessly.

#### Implementation Details

- **Expandable Views**: Comments are displayed when the user expands a post.
- **Asynchronous Loading**: Comments are fetched asynchronously to prevent blocking the UI.
- **Add Comments**: Users can add comments, which are immediately reflected in the UI.

#### Associated Files

- **UI Layer**:
    - `HomeViewModel.kt`: Handles fetching and adding comments.
    - `CommentSection.kt` and `CommentItem.kt`: Composables for displaying comments.
- **Domain Layer**:
    - `GetCommentsUseCase.kt`: Use case for fetching comments.
    - `CommentMapper.kt`: Maps comment data models.
- **Data Layer**:
    - `CommentDao.kt` and `CommentEntity.kt`: Manage comment data persistence.
    - `PostService.kt`: Defines API endpoints for fetching comments.

### Pull to Refresh

#### Description

Refresh the feed at any time to load the latest posts. This enhances the user experience by providing control over content updates.

#### Implementation Details

- **SwipeRefresh**: Integrated using `accompanist-swiperefresh` library.
- **State Management**: `SwipeRefreshState` is used to manage the refreshing state.

#### Associated Files

- **UI Layer**:
    - `HomeScreen.kt`: Implements the pull-to-refresh feature.
- **ViewModel**:
    - `HomeViewModel.kt`: Refreshes data upon user action.

### User Profiles

#### Description

Tap on a user's avatar to view their detailed profile, including name, username, email, and address.

#### Implementation Details

- **Navigation Component**: Navigates to the user details screen.
- **UserDetailsScreen.kt**: Displays the user's information.
- **Asynchronous Data Loading**: Fetches user details asynchronously to prevent UI blocking.

#### Associated Files

- **UI Layer**:
    - `UserDetailsScreen.kt` and `UserDetailsContent.kt`: Render user profile information.
- **Domain Layer**:
    - `GetUserDetailsUseCase.kt`: Use case for fetching user details.
    - `UserMapper.kt`: Maps user data models.
- **Data Layer**:
    - `UserViewModel.kt`: Manages the state and data fetching for user profiles.
    - `UserRepositoryImpl.kt`: Fetches user data from the remote API.
    - `UserService.kt`: Defines API endpoints for user data.

### Offline Support

#### Description

Cached posts and data allow browsing even without an internet connection, providing a seamless experience regardless of connectivity.

#### Implementation Details

- **Room Database**: Used for local data caching of posts, likes, and comments.
- **Error Handling**: On network failure, the app gracefully falls back to cached data.

#### Associated Files

- **Data Layer**:
    - `AppDatabase.kt`: Configures the Room database.
    - `PostDao.kt`, `LikeDao.kt`, `CommentDao.kt`: DAOs for data access.
    - `PostEntity.kt`, `LikeEntity.kt`, `CommentEntity.kt`: Entities representing data models in the database.
    - `PostsPagingSource.kt`: Loads data from local cache when network is unavailable.
- **Domain Layer**:
    - `PostRepositoryImpl.kt`: Manages data retrieval from local storage.

---

## Technical Implementation

### Architecture Overview

SocialX is built using **Clean Architecture** principles, promoting separation of concerns and scalability. The app is divided into three main layers:

1. **Presentation (UI) Layer**: Contains all UI components and ViewModels.
2. **Domain Layer**: Includes business logic, use cases, and domain models.
3. **Data Layer**: Manages data retrieval, storage, and network operations.

### Tech Stack

- **Language**: Kotlin
- **Framework**: Jetpack Compose for UI development
- **Architecture**: MVVM (Model-View-ViewModel) with Clean Architecture principles
- **Networking**:
    - **Retrofit**: For network requests
    - **OkHttp**: HTTP client with logging capabilities
    - **Gson**: JSON serialization/deserialization
- **Data Persistence**:
    - **Room Database**: Local caching and offline support
- **Dependency Injection**: Dagger Hilt
- **Asynchronous Programming**: Coroutines and Flow
- **Image Loading**: Coil library for efficient image loading and caching
- **Pagination**: Paging 3 library for infinite scrolling
- **Navigation**: Navigation component with Compose integration
- **Version Control**: Git

### Design Patterns and Principles

- **Clean Architecture**: Separation of concerns across layers.
- **Repository Pattern**: Abstracts data access and business logic.
- **Use Cases**: Encapsulate business logic for specific operations.
- **Dependency Injection**: Dagger Hilt provides dependencies, improving testability and scalability.
- **Immutable Data Models**: Ensures thread safety and predictability.

---

## Requirements Coverage

### List Performance

- **Paging 3 Library**: Efficiently loads data in chunks, reducing memory usage.
- **LazyColumn**: Renders lists efficiently with minimal recompositions.
- **Optimized Data Structures**: Uses immutable data classes and StateFlows.

### Memory Management

- **ViewModel Scopes**: Manages UI-related data across configuration changes.
- **StateFlow**: Reactive streams for state management without memory leaks.
- **Lifecycle Awareness**: Uses `collectAsStateWithLifecycle` for safe data collection.

### Network Handling

- **Retrofit with OkHttp**: Robust network layer with efficient HTTP requests.
- **Error Handling**: Specific exception handling for network errors, timeouts, and parsing issues.
- **Coroutine Dispatchers**: Network operations run on `Dispatchers.IO` to keep the main thread responsive.

### Complex UI Interactions

- **Optimistic UI Updates**: Immediate feedback on likes and comments.
- **Composable Functions**: Modular UI components enhance readability and reusability.
- **Navigation**: Handles complex navigation flows with safe argument passing.

---

## Future Enhancements
0. **Unit Tests**: Start by testing ViewModels and Use Cases for verifying business logic.
1. **User Authentication**: Implement sign-up and login for personalized experiences.
2. **Real-Time Updates**: Use WebSockets or Firebase for live feed updates.
3. **Enhanced Like Functionality**:
    - Attach likes to specific users, allowing users to see who liked a post.
    - Add animations for like/unlike actions.
4. **Improved Comments**:
    - Threaded comments for direct replies.
    - Comment moderation features.
5. **Deferred Actions**:
    - Allow offline likes and comments, syncing when online.
6. **Connectivity Awareness**:
    - Notify users of network status changes.
7. **Profile Customization**:
    - Users can edit their profiles, including bio and profile picture.
8. **Push Notifications**:
    - Notify users of interactions like likes, comments, or mentions.
9. **Explore Page**:
    - Discover new content based on interests or trends.
10. **Direct Messaging**:
    - Private messaging between users.

---

## Getting Started

### Prerequisites

- **Android Studio Arctic Fox** or higher
- **Minimum SDK**: 24
- **Recommended SDK**: 33

### Installation

1. **Clone the Repository**:

   ```bash
   git clone https://github.com/waleHa/SocialX.git
   ```

2. **Open in Android Studio**:

    - Open Android Studio.
    - Click on `File` > `Open...` and select the cloned repository folder.

3. **Build the Project**:

    - Allow Gradle to sync and build the project.
    - Resolve any dependencies if prompted.

4. **Run the App**:

    - Select an emulator or physical device.
    - Click on the `Run` button or use the `Shift + F10` shortcut.

---

## License

This project is developed as an assignment and is intended for demonstration purposes.
