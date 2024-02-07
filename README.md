# Cinemates

![WhatsApp Image 2024-02-07 at 21 16 56](https://github.com/sim0nlee/Cinemates/assets/94008546/e2f0e9ac-fa7d-46ac-978d-0a1225e8207e)                       ![WhatsApp Image 2024-02-07 at 21 18 01](https://github.com/sim0nlee/Cinemates/assets/94008546/7be4889f-ec32-4c05-8c80-ee0b0dfa608a)


Cinemates was developed in a simulated process of Software Engineer - Stakeholder professional relationship. The request was to create a movie-based social network as an Android application, accompanied by an in-depth documentation and usability analysis of the software through use-case diagrams, mock ups, Cockburn tables, etc. The following requirements had to be satisfied:

‣ User Registration and Authentication. Authentication should be possible through an external provider. For this feature we have employed Google Firebase Auth and the Facebook Graph API.

‣ Updatable user profiles, with personal data such as a profile picture.

‣ Movie search. This feature was implemented through the TMDB API, allowing for instant retrieval of movies upon user search.

‣ Personalized "Favorites" and "Saved" movie lists. 

‣ User search and following requests, with the possibility of accepting and refusing received requests.

‣ Active feed to visualize the actions of your followed users.

‣ Complementary desktop client for Administrators to visualize application statistics, such as the number of currently logged users. This was made possible through the Google Firebase Real-Time Database.

The application was created with horizontal scalability in mind, making use of Google Firebase Firestore to keep track of user data, and is structured according to the Model-View-Controller design pattern. 
