its is my assignment o 3 where i perform the specfic task
Section 1. Application Setup and Theme Management
1.1 Implement multiple themes (e.g., Light, Dark, Custom Color Theme).
1.2 Allow users to switch themes at runtime using Options Menu.
1.3 Persist the selected theme using SharedPreferences.
1.4 Apply the saved theme on app restart (Activity lifecycle handling).

Section 2. User State and Authentication Flag
2.1 Implement a basic login or welcome screen.
2.2 Store an authentication flag (isLoggedIn) in SharedPreferences.
2.3 On app launch, check the flag and redirect the user using Intent navigation.
2.4 Maintain state during configuration changes (rotation).

Section 3. Web Services / API Integration
3.1 Fetch data from a public REST API (e.g., users, posts, products).
3.2 Use HTTP client (e.g., HttpURLConnection or Retrofit).
3.3 Parse JSON response into model classes.
3.4 Handle network failure and empty responses.

Section 4. Local Data Persistence (SQLite)
4.1 Design an SQLite database schema for the fetched data.
4.2 Store API response data into SQLite.
4.3 Retrieve data from SQLite when offline.
4.4 Implement basic CRUD operations.

Section 5. Adapter Implementation
5.1 Display stored data using a default adapter (ArrayAdapter / SimpleCursorAdapter).
5.2 Implement a custom adapter using RecyclerView.Adapter.
5.3 Bind SQLite data to UI components efficiently.
5.4 Handle item click events via adapters.

Section 6. Menu Implementation and Navigation
6.1 Implement Options Menu for global actions (theme change, logout).
6.2 Implement Context Menu for item-specific actions (edit, delete).
6.3 Implement Popup Menu for quick actions inside list items.
6.4 Navigate between activities using Intents.

Section 7. WebView Integration
7.1 Integrate WebView to display external or internal web content.
7.2 Load a URL related to the fetched data (e.g., location, profile, documentation).
7.3 Enable JavaScript and handle page loading states.
7.4 Provide in-app browsing without switching to external browser.

Section 8. Input Controls and UI Interaction
8.1 Use standard input controls (EditText, Button, Spinner, Switch).
8.2 Validate user input before processing.
8.3 Reflect user interactions in stored data and UI updates.

Section 9. Activity Lifecycle and State Management
9.1 Handle configuration changes (rotation).
9.2 Preserve UI state using onSaveInstanceState.
9.3 Restore data without re-fetching from API unnecessarily.
9.4 Ensure no memory leaks during lifecycle transitions.

Non-Functional Requirements
•	App must work in offline mode using SQLite data
•	Clean UI following Material Design principles
•	Proper separation of concerns (Activities, Adapters, DB, Network)
•	Code must be commented and readable
