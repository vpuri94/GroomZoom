Original App Design Project - README Template
===

# GroomZoom

## Table of Contents
1. [Overview](#Overview)
1. [Product Spec](#Product-Spec)
2. [Weekly Plan] (#Weekly-Plan)
1. [Wireframes](#Wireframes)
2. [Schema](#Schema)

## Overview
### Description
Uber for haircut/beard grooming, so that you can have a barber come to your house patio and get yourself groomed without having to anywhere.

### App Evaluation
[Evaluation of your app across the following attributes]
- **Category:** Lifestyle/Personal Care
- **Mobile:** Mobile is essential for the ability of someone to be able to quickly share their location, as well as facial profile to communicate with and find the right barber to come to them that is somewhat nearby, and can provide the services needed. Camera is used to send pics of their hair/beard structure.
- **Story:** Allows users to be able to get the personal grooming care they need, without the hassle of having to plan out, test, and waste gas on finding the right barber for them.
- **Market:** Predominantly anybody thats male and can grow hair/beards, but women can also get haircuts without the beard grooming, so basically the market expands out to anyone who has hair.
- **Habit:** People would be able to constantly use this, as no one person gets a haircut/beard shaping at the same time each month/every few months, and due to the sheer number of people who are looking to make a few extra bucks on the side cutting hair, as well as people needing grooming, this app would be constantly used by people.
- **Scope:** V1 would allow customers to setup their profiles by uploading their profile pics of how their head and beard currently look, sharing their location, as well as what they are looking to get done on their head, and a brief description of the type of service to get done.  V2 would allow barbers to create profile and a quick bio of themselves, the tools they use, what they specialize in, where they are located, and a radius of how far they are willing to go. V3 would see the connection between these two, allowing users to make a request in a database of barbers who fit the needs of the user, and allowing barbers to accept the request. V4 would allow for live tracking of barber location when they are on their way, as well as any other Uber-esque features to add.

## Product Spec

### 1. User Stories (Required and Optional)

#### FBU REQUIREMENTS:

* Your app has multiple views
  -> Satisfied by the multiple screens/objects required for the account pages, ordering a barber, etc.
* Your app interacts with a database (e.g. Parse)
  -> Using Parse database to store the users of the app (clients and barber)
* You can log in/log out of your app as a user
  -> Will allow log in/log out feature as a core functionality
* You can sign up with a new user profile
  -> Can create new barber/client account
* Somewhere in your app you can use the camera to take a picture and do something with the picture (e.g. take a photo and share it to a feed, or take a photo and set a user’s profile picture)
  -> Require clients to use camera to take picture of their facial profile and upload it to the batabase and share it to the GroomZoom feed for barbers to view
* Your app integrates with a SDK (e.g. Google Maps SDK, Facebook SDK)
  -> Using Google Maps SDK for sharing location of client to barber
* Your app contains at least one more complex algorithm (talk over this with your manager)
  -> Including a filtration algorithm when displaying barbers in the RecyclerView of available barbers (by barbers within x miles, by barber preference, time availability, etc.)
* Your app uses gesture recognizers (e.g. double tap to like, e.g. pinch to scale)
  -> Will include a pinch to scale when barbers look at facial profile of clients
* Your app use an animation (doesn’t have to be fancy) (e.g. fade in/out, e.g. animating a view growing and shrinking)
  -> Will include a fade in/out animation when going to the next page of available barbers
* Your app incorporates an external library to add visual polish
  -> TBD once i find a good external library


**Required Must-have Stories**

* [x] User can create a new account (2 types, barber or customer)
* [x] User can login
* [x] Customer can share pictures of their hair, and face profiles (for beard work)
* [x] User can share location
* [x] User can post a brief description of what kind of service they are looking for
* [x]  Barber can post the services they provide, and their location
* [x] Barber can post price of specific services
* [x] Client can scroll through list of available barbers and click on the services they offer, their experience, the tools they use, and book an appointment for them to come for same day or another day
* [ ] Use fade in/out animation when scrolling through pages
* [x] Use filtration/sorting algorithm when displaying barbers on browse screen
* [ ] Use gesture recognizer for pinch to scale when looking at picture of clients

**Optional Nice-to-have Stories**

* [ ] Live tracking of barbers when an "order" is confirmed (like how you can track an Ubereats order when its on the way)
* [ ] Ability for barber to post pictures of their equipment
* [ ] Ability for profile pictures
* [ ] Ability for messaging between barbers and clients
* [ ] Ability for maps feature to default to automatically get your location and reccoment it as your address

### 2. Screen Archetypes

* Registration Screen
   * Barber/Client (User) can create a new account
* Login Screen
   * User can login
 * My profile page
   * Client can share pictures of their hair, and face profiles (for beard work)
   * Client can post a brief description of what kind of service they are looking for
   * Client/Barber can put in their address
   * Barber can put the kind of services they provide
   * Barber can post their prices for specific services
 * Browse
   * Client can scroll through list of available barbers and click on the services they offer, their experience, the tools they use, and book an appointment for them to come for same day or another day
 * My appointments
   * Users can look at upcoming appointments


### 3. Navigation

**Tab Navigation** (Tab to Screen)

* Browse
* My appointments
* My profile

**Flow Navigation** (Screen to Screen)

* Login Screen
   * => Browse
* Registration Screen
   * => Browse
* My profile screen
    * => Camera screen to take selfie of yourself to upload to profile
    * => What I am looking for Screen? (for clients)
    * => What services can i provide (along w/ prices)? screen (for barbers)
    * => Location/Distance I can travel(for barbers)? screen
* My Appointments screen
    * =>List of all available apointments screen
        * => Upon clicking one, the detailed description of an upcoming appointment


## Weekly-plan
### Week one
- Set up basic data models (user and appointments) and validate they work
- Build the navigational skeleton (bottom navigation bar + fragments)
- Build out skeleton views
- Implement basic login/logout/signup feature
- Start working on the home timeline screen query and display data from Parse
    - Make a details view for this
- Start fleshing out the profile fragment
- Set up skeleton of camera or Google maps sdk to app

### Week Two
- Finish up polishing appointments/browse barbers page
- Ensure Parse database and appointments/browsw page are setup correctly
- Work on the ratings/reviews page and sync it with another Parse database
- Finish working on camera or google maps sdk to app
- Attempt to finish out the My Profile page
- Implement the sorting/filtering barber algorithm
- Finish both the google maps and Camera feature in the my profile tab

## Week three
- Begin adding visual polish
- Begin adding an animation
- Begin adding a gesture Recognizer
- Ensure required stories are done by end of week three
- Finish all visual features/external visual library

## Week four
- Work on any stretch features after

## Wireframes
[Add picture of your hand sketched wireframes in this section]

### CLIENT WIREFRAME

<img src="https://github.com/vpuri94/GroomZoom/blob/master/ClientWireframe.jpg" width=600>

### BARBER WIREFRAME

<img src="https://github.com/vpuri94/GroomZoom/blob/master/BarberWireframe.jpg" width=600>


### [BONUS] Digital Wireframes & Mockups

### [BONUS] Interactive Prototype

## Schema
### Models
#### Post

MODEL FOR APPOINTMENTS

   | Property      | Type     | Description |
   | ------------- | -------- | ------------|
   | UserName      | String   | unique id for the client/barber |
   | profilePic    | File     | image of client/barber |
   | dateOfAppt    | DateTime | The date and time a user has an appointmen |
   | Price         | Number   | Cost of the service |
   | services      | Array    | Array of list of services offered |
   | occurred      | Boolean  | True if appointment has already happened |

MODEL FOR BROWSING BARBERS

   | Property      | Type     | Description |
   | ------------- | -------- | ------------|
   | barberName    | String   | unique id for the barber |
   | profilePic    | File     | image of barber |
   | Price         | Number   | Cost of the service |
   | services      | Array    | Array of list of services offered |
   | rating        | Number   | Rating of barber from 1-5 |
   | distance      | Number   | Approx miles away from your location |



### Networking
- Home feed of appointments
  - (Read/GET) Query all posts (possible option to sort posts based on post fields which would mean specific queries)
  - (Create/POST) Create a new appointment (for barbers)
  - (Update/PUT) Update the rating of a user/price of service
  - (Update/PUT) Update the distance of how far a barber is from client
  - (Update/PUT) Update the profile picture of user
  - (Update/PUT) Update if appointment is a past appointment or not
  - (Delete) Delete appointment
- Browse Barbers page
  - (Read/GET) Query all posts (possible option to sort posts based on post fields which would mean specific queries)
  - (Create/POST) Create a new barber near you
  - (Update/PUT) Update the rating of a user/price of service
  - (Update/PUT) Update the distance of how far a barber is from client
  - (Update/PUT) Update the profile picture of user
  - (Delete) Delete barber near you
- Profile section
  - (Read/GET) Get info about current user (name, services, address, location, pfp)
  - (Update/PUT) Update user profile image (side profile pics if applicable) and other user components (name, services wanted/offered, address, payment, etc.)
  - (Create/POST) Create a new user/information about the user
# GroomZoom
