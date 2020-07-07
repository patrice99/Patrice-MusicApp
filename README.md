## App Design Product Spec
by Patrice Gill 

### Top three App Ideas 
1. Music App
2. Hair App 
3. Fashion App


Original App Design Project - README
===

# Music App

## Table of Contents
1. [Overview](#Overview)
1. [Product Spec](#Product-Spec)
1. [Wireframes](#Wireframes)
2. [Schema](#Schema)

## Overview
### Description
This app helps to connect musicians to jobs and gigs in their area. 

### App Evaluation
[Evaluation of your app across the following attributes]
- **Category:** Lifestyle
- **Mobile:** Users will be able to use their location services on their devices to be connected to music events in their area. They can also use the camera and import videos from their phone to their profile. 
- **Story:** Musicians and non-musicians can use this app to showcase their talent or source talent for their events. Even without a specific event in mind, there is a feed with alot of cool videos and MP3s of music to watch and listen to. 
- **Market:** The market is lovers of all music, especially live music. 
- **Habit:** You can spend hours on the feed looking at cool videos of music techniques or live performances. Event Planners can also use this app every time they are on a talent hunt.
- **Scope:** I would like to expand to have a marketplace where musicians can buy and sell equipment on the app. It would also be fun to implement a "learning tab" where musicians can aquire more skills through youtube tutorials or other learning videos.

## Product Spec

### 1. User Stories (Required and Optional)

**Required Must-have Stories**

##### FBU project Expectations
    * Your app has multiple views
    * Your app interacts with a database (e.g. Parse)
    * You can log in/log out of your app as a user
    * You can sign up with a new user profile
    * Somewhere in your app you can use the camera to take a picture and do something with the picture (e.g. take a photo and share it to a feed, or take a photo and set a user’s profile picture)
    * Your app integrates with a SDK (e.g. Google Maps SDK, Facebook SDK)
    * Your app contains at least one more complex algorithm (talk over this with your manager)
    * Your app uses gesture recognizers (e.g. double tap to like, e.g. pinch to scale)
    * Your app use an animation (doesn’t have to be fancy) (e.g. fade in/out, e.g. animating a view growing and shrinking)
    * Your app incorporates an external library to add visual polish


* A login page
    * you can either log in with Facebook/Google or without 
* A user profile
    * unique screenName and a more flexible name 
    * Profile pic
    * User type
        * Looking for a gig 
        * Looking for a musician
    * Solo Artist / Create a band group (invite other user profiles)
    * Type of Instruments played
    * Common genres played 
    * Bio
    * Hourly rate
    * Portfolio
        * Import videos or MP3 from device 
        * Import videos from Youtube
        * Display any music certificates/authorizations
    * Followers
    * Following
* A "near you" Map
* Search Activity
    * Search by location
    * Search by hourly rate 
    * Search by genre
    * Search by instrument 
    * Search by band/solo


**Optional Nice-to-have Stories**

* Import MP3s from Soundcloud for Portfolio
* Feed tab
    * Liked posts(from FeedTab)

### 2. Screen Archetypes
   * Splash Pages
   * Login Screen
   * Sign Up Screen
   * Stream
   * Map View
   * Search Screen
   * Profile Screen
   * Notifications Screen
   * Detail Sceen
   * Settings

### 3. Navigation

**Tab Navigation** (Tab to Screen)
I plan to use a general bottom navigation bar 

* Feed
* Profile
* Search/Discover

**Flow Navigation** (Screen to Screen)

* Feed
   * Details
* Search
   * Maps
   * Events
   * Genres
* Profile 
    * Band groups
    * Feed posts - Videos/MP3s
    * Portfolio (different to feed)
    * Liked posts
    * Settings

## Wireframes
[Add picture of your hand sketched wireframes in this section]
<img src="YOUR_WIREFRAME_IMAGE_URL" width=600>

### [BONUS] Digital Wireframes & Mockups

### [BONUS] Interactive Prototype

## Schema 
[This section will be completed in Unit 9]
### Models
[Add table of models]
### Networking
- [Add list of network requests by screen ]
- [Create basic snippets for each Parse network request]
- [OPTIONAL: List endpoints if using existing API such as Yelp]

