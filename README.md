# My_Airlines
Assigment for work interview

### First version with limited libs. For imho better solution check "ao" branch

Goal is to create a sample app that displays information about
various airlines. Using open source or 3rd party libraries is allowed but should not be done
excessively.
The app should have at least two screens:

Airline List:
- This screen should display a list of airlines retrieved from this JSON endpoint:https://www.kayak.com/h/mobileapis/directory/airlines
- Each item should have the airline logo and name
- Provide a control to toggle between showing the entire list and a subset of the list, containing only those airlines that the user has marked as a "favourite"

Airline Details:
- Tapping on an airline in the first screen should display a second screen containing the details of that airline
- Display the airline logo, name, website and phone number on the screen
- Provide a button to launch the airline website in an external browser
- Provide a toggle control to mark an airline as a "favourite"
- Extra credit for persisting the list of favourites across app launches

Used technologies:
- AndroidX
- Picasso
- Gson
- RecyclerView
