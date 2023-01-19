# MusicDiscoveryApp
An Android application that allows users to discover and listen to music.

## Description
 The app utilizes the Deezer API to access a vast library of songs and artists, and provides users with a simple and user-friendly interface for browsing and listening to music.

The app uses OkHttpClient, a popular library for handling API calls, to interact with the Deezer API and retrieve information about songs, artists and albums. The data is then parsed and displayed in the app using RecyclerView and CardView.

The app also implements pagination to handle the large number of songs and artists that are retrieved from the API, allowing users to efficiently browse through the music library.

Users can search for songs, artists and albums, and can also view more detailed information about the selected song such as lyrics, artist bio and cover art. Additionally, the app allows users to create a personal music library by adding their favorite songs to a playlist.

The app also uses Media Player for Android, to play the songs and manage the playback of the audio. It also uses Firebase Cloud Messaging (FCM) to handle the authentication and database systems.

The source code is written in Java and follows the MVP pattern for the architecture. The app also makes use of other popular libraries such as Picasso for displaying album covers.
