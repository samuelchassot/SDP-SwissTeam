# SDP-SwissTeam

## Specifications for android app
### General idea
An app that let people make new relations based on what they can give or what they need, especially help or many kind of services.

### More detailed specs
1. connects to the app with a known platform (Facebook, Google, Snapchat, ...)

2. At the creation of the account, chooses in which domain you're ready to help others, what the user can do (in a list that we define). The user can choose none if he’s looking for services only.

3. to have a account page that let the user change his capabilities and manage pwd, email, center of action and radius.

4. when the user needs help :
    1. chooses the domain
    2. can upload a picture of his problem
    3. the app show user that have the selected capability. and sort the result by geographic proximity (using Android location)
    4. the user chooses the profile that he likes then :
        1. the app let the user choose if he wants :
            1. a real meeting : touch a button that automatically send a notification to the other letting him know he's needed
            2. an online chat : open a chat with the person that provides the service (like any other chat app, possibly send media like images) that sends notifications
        3. If anybody can solve his problem and is in a nearby area, it opens a pop-up : “This person can solve your problem and is at x minutes walk from you”

### Used android capabilities
1. Server connection
2. local database as cache
3. push notification
4. GPS / camera 
5. Network





Backlog

Connection (via Google)
Profil :
•    Photo (camera sensor) 
•    ID 
•    Spoken language
•    Center and radius of action
•    Popularity
Settings :
•    Show X kilometers around me
•    Language (of the application)
•    Disconnection
Services exchange page :
Online chat (between two “matches”)
Current location (GPS signal)

