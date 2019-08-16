# GoTenna
Time Spent: 10 hours Approx 
1. As Kotlin is not my primary language, I consumed a bit more of time to deal with the syntax.
2. But will be confident to say I have a decent grip on the language by now.
3. I spent my time architecting the app (might feel overkill for just one task feature).
4. Tried to show my skills writing modular code, with loose coupling with the new MVVM architecture.

Architecture
1. Used MVVM+RepositoryPattern+Coroutines+LiveData+Room+Koin
2. Repository pattern: Abstracts the data source from ViewModel, trying to get the latest data from network and updating the room database
3. Single source of truth: With Room and LiveData, this becomes the single source of truth for the data that is given to the views.
4. Used Koin, lightweight dependency injection
5. Divided the project in 3 modules
    1. app - The main app
    2. network - Retrofit, webservice
    3. database - Room, data class
6. Although I didnt have enough time to write unit test cases, the current architecture supports good level of testing

Extra Features
1. App supports orientation changes with the live data in place.
2. If not connected to internet, and gets the data from local db, then the app notifies the user that data might be old.
3. User Permissions are handled elegantly, with snackbar poping from below with action button or notifying the user to enable permissions from app info if said "do not ask" to permissions
4. Using BottomSheetDialogFragment with recycler view to show the list of points.
