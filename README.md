# Pixel Coding Exercise
This is a single screen page that fetches and displays top 20 StackOverflow users.

Afther the app is launched, it fetches the list of top 20 users and the list of followed users, and displays each of them in a tile which contains user's profile image, name (formatted) and the total reputation number. There is a `Follow`/ `Unfollow` button in each tile as well for simulating the `follow functionality` locally.

# Follow functionality
The app displays the `Follow`/ `Unfollow` based on the following status. The entire `follow functionality` is run locally, room database is used to store this data, and the `follow status` persist between sessions.

There is a `Unfollow All` button in the top bar if the app user has followed any StackOverflow users. Clicking on that will unfollow all StackOverflow users.

# Tech
kotlin, jetpack compose and MVVM architecture were used to create the app

Hilt was chosen for Dependency injection

okhttp and moshi were chosen for performing network call (fetching top 20 StackOverflow users) and parsing the json file

lottie was used to display the loading spinner to avoid `frozen screen`

room database was use to store the data of followed users for simulating the `follow functionality` locally

some user names contain html code, they are formatted using the `fromHtml()` function

There are two separate repositories for handling specific jobs which are fetching top 20 StackOverflow users and dealing with the `follow functionality`, the  view model observes the flows of these repositories and collate data for it's ui state.

# Error Handling
Implemented `LiveNetworkMonitor` to catch no network connect error. Error message and status code will be logged as well.

To ensure it covers the edge case (receiving an empty list), a check will be performed after a successful call to identify if the list of top 20 StackOverflow users is empty or not. 

In terms of UX, an alert dialog will be dispayed if error occurs. There is a `Retry` button for performing the network call again.

# Unit tests
Unit tests for the view model has been implemented which cover 4 cases:
1. NO top users list (error), NO followed users
2. HAS top users list, NO followed users
3. NO top users list (error), Has followed users
4. Has top users list, Has followed users

These tests check the initial ui state and the ui state after emitting the data set

# Future work
User Details Page can be implemented, clicking on the tile on the main screen will bring the app user to the User Details Page which display more info of the selected top user. The VM of this page can observe the flows of the repositories to form it ui state as well.
