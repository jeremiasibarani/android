### This project is about a simple story application where user can post their story and see other's stories.

A. Domain :  
1. Authentication :  
    a. Login (email dan password)  
    b. Register (name, email, and password)  
2. Story :  
    a. Stories (where user can see others' stories)  
    b. Add Story (where user can add their own story)

B. Package  
The packaging structure is grouped by functionality, for instance all of functionalities about views are placed inside view package  

C. Design pattern  
Model-View-ViewModel is the design pattern used in this app, the [diagram](https://d17ivq9b7rppb3.cloudfront.net/original/academy/20201216131847331b0da673f09e74f68866829faebd34.png)
    
D. Dependencies : <br>
    1. Camera X  
    2. Retrofit and GSON converter  
    3. Jetpack Navigation and Safe argument  
    4. Data store  
    5. Lifecycle (view model and live data)  
    6. Activity ktx  
    7. Glide
    8. Room pagination
    9. Pagination
    10. Mockito and Espresso
    11. Mock Web Server
    12. Google play service

this project support offline-online condition, each time the app fetched data from backend service, the data will then be cached into a local database, as a way to implement Single Source of Truth, local database will be the only source of data fetched from backend service, and to reduce the usage of system resource the pagination is applied with 5 requests as the initial requests, the general flow of offline-online mechanism is below : <br><br>

<img src="https://drive.google.com/uc?id=1XbuhR27_VZVxmRzqvtLKapHLTLq-Z55H"  width="70%" height="70%">



