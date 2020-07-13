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
    * Public Email
    * Portfolio
        * Users Portfolio consists of their feed and additional material.
        * Import videos from Youtube
        * Display any music certificates/authorizations
* A "near you" Map
    * Notices/Events/Artist location is put on map
* Search Activity
    * Search by location
    * Search by hourly rate 
    * Search by genre
    * Search by instrument 
    * Search by band/solo
* Feed/Stream - User can either post videos/music and images of their performances or post notices looking for jobs/hire.
    * Posts can be liked
* User can follow people/ see their followers.



**Optional Nice-to-have Stories**
* User can log in with Facebook and Google
* User can import MP3s from Soundcloud for Portfolio
* Notification Fragment
    * Has a chat screen and user can chat with potential employers/employees
* Posts can have comments.
* Users can see their reviews and add reviews on other profiles

### 2. Screen Archetypes
   * Login Screen
   * Sign Up Screen
   * Stream
   * Map View
   * Search Screen
   * Profile Screen
   * Detail Sceen
   * Settings

#### Optional Screen Archetypes
   * Splash Pages
   * Notifications Screen
   * Chat Screen

### 3. Navigation

**Tab Navigation** (Tab to Screen)
I plan to use a general bottom navigation bar 
* Feed
* Profile
* Search/Discover
* Map

#### Optional Tab Navigation
* Notifications Screen

**Flow Navigation** (Screen to Screen)

* Feed
   * Details of post
* Discover
   * Notices
   * Search
* Profile 
    * Band groups
    * Feed posts - Videos/MP3s
    * Portfolio (different to feed)
    * Settings

## Wireframes

<img src="https://github.com/patrice99/Patrice-MusicApp/blob/6e13930d88362d0401c53593d5748d72e435cb05/Wireframes1.png?raw=true" width=600>


<img src="https://github.com/patrice99/Patrice-MusicApp/blob/6e13930d88362d0401c53593d5748d72e435cb05/Wireframes2.png?raw=true" width=600>




### [BONUS] Digital Wireframes & Mockups

### [BONUS] Interactive Prototype

## Schema 
### Models

Post
| Property        | Type                | Description                                          |
|:--------------- |:------------------- |:---------------------------------------------------- |
| objectId        | String              | unique id for the user post   (Default)              |
| author          | Pointer to User     | post author                                          |
| image           | File                | image to be attached to post                         |
| MP3             | File                | MP3 to be attached to post                           |
| video           | File                | Video to be attached to post                         |
| title           | String              | short Title to be attached to the post               |
| caption         | String              | description that the author attaches to the post.    |
| location        | Pointer to Location | location of the user/location of the post taken      |
| timeStamp       | DateTime            | date when the post is created (Default)              |
| * commentsCount | Number              | number of comments the post receives                 |
| * comments      | JSONObject          | JSONObject that contains an array of Comment Objects |
| isNotice        | boolean             | true if the post is a notice, false if not.          |
| likesCount      | Number              | number of likes the post receives                    |


User
| Property        | Type                  | Description                                                      |
|:--------------- |:--------------------- |:---------------------------------------------------------------- |
| userId          | String                | unique id for the user   (Default)                               |
| username        | String                | username chosen by the user                                      |
| password        | String                | password chosen by the user                                      |
| emailAddress    | String                | email Address chosen by the user when registering                |
| pubEmailAddress | String                | email address to be displayed on profile. Default - emailAddress |
| profileImage    | File                  | profile image of the user                                        |
| bio             | String                | Bio of the user chosen by the user when customizing profile      |
| location        | Location              | location that the user usually operates                          |
| createdAt       | DateTime              | date when the user account is created (Default)                  |
| postCount       | Number                | number of posts the user created                                 |
| followers       | Number                | number of followers the user has                                 |
| following       | Number                | number of users the user follows                                 |
| bandGroups      | Pointer to Band Group | bands that this user is a member                                 |
| instruments     | ENUM                  | instruments the user plays                                       |
| genres          | ENUM                  | genres the user usually plays                                    |
| rate            | Float                 | hourly rate for billing purposes                                 |
| artistType      | boolean               | true for solo, false for band                                    |
| * review          | String                | reviews left by other users                                    |


Portfolio

| Property     | Type            | Description                                   |
|:------------ |:--------------- |:--------------------------------------------- |
| portfolioId  | String          | unique id for the user's portfolio  (Default) |
| author       | Pointer to User | porfolio author                               |
| image        | File            | image to be attached to portfolio             |
| MP3          | File            | MP3 to be attached to portofolio              |
| video        | File            | Video to be attached to portofolio            |
| certificates | File            | Certificate to be attached to portfolio       |
| title        | String          | short Title to be attached to the post        |

## Optional Features
Chat
| Property    | Type            | Description                                               |
|:----------- |:--------------- |:--------------------------------------------------------- |
| chatId      | String          | unique id for the chat  (Default)                         |
| userIdOther | Pointer to User | other user in chat                                        |
| createdAt   | DateTime        | date when the chat is created (Default)                   |
| messages    | JSONObject      | a JSONObject that contains a JSONArray of message objects |


Message
| Property  | Type            | Description                                |
|:--------- |:--------------- |:------------------------------------------ |
| messageId | String          | unique id for the message  (Default)       |
| author    | Pointer to User | author of the message                      |
| body      | String          | message body                               |
| createdAt | DateTime        | date when the message is created (Default) |


Comments 
| Property  | Type            | Description                                |
|:--------- |:--------------- |:------------------------------------------ |
| commentId | String          | unique id for each comment (Default)       |
| userId    | Pointer to User | user commenting                            |
| body      | String          | comment body                               |
| createdAt | DateTime        | date when the comment is created (Default) |



### Networking
- Login Screen
- Sign Up Screen
- Home
    * (Read/GET) Query all posts for feed timeline
       ```ParseQuery<Post> query = ParseQuery.getQuery(Post.class);
        query.include(Post.KEY_USER);
        query.setLimit(20);
        query.addDescendingOrder(Post.KEY_CREATED_AT);
        query.findInBackground(new FindCallback<Post>() {
            @Override
            public void done(List<Post> posts, ParseException e) {
                if (e != null){
                    Log.e(TAG, "Issue with getting posts", e);
                    return;
                }
                for(Post post: posts){
                    Log.i(TAG, "Post: " + post.getDescription() + " Username: " + post.getUser().getUsername());
                }
                allPosts.addAll(posts);
                adapter.notifyDataSetChanged();
            }
        });```
        
    * (Create/POST) Create a new like on a post
    * (Delete) Delete existing like
    * (Create/POST) Create a new comment on a post (Optional)
    * (Delete) Delete existing comment (Optional)
    * (Create/POST) Create a new post
    * (Delete) Delete an existing post
- Map View
    - (Read/GET) Get following artists locations, notices by following on map.
- Search Screen
    - (Read/GET) Get notices or artists through complex search algorithm.
- Profile Screen
    - (Update/PUT) Update User's Profile Image
    - (Update/PUT) Update User's Profile Name
    - (Update/PUT) Update User's screenName
    - (Update/PUT) Update User's email
    - (Update/PUT) Update User's genres
        - etc. 
- Detail Sceen
    (Create/POST) Create a new like on a post
    * (Delete) Delete existing like
    * (Create/POST) Create a new comment on a post (Optional)
    * (Delete) Delete existing comment (Optional)
- Settings
    (Logout)

