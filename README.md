# MoviesApp

App displays **20 movies** from https://www.themoviedb.org **API**. User can **sort** movies by Popularity or by Rating. Each movie has a **detail screen** with synopsis, rating, trailers and reviews. Movie can be added to **Favorite Movies** and can be displayed later using the Favorites option in Settings. This project was done as a part of **Udacity Android Developer Nanodegree**.

**App Preview:**

![Alt text](readme/MoviesAppPreview.gif?raw=true "App Preview")

**Main features:**
- App uses **Retrofit library** - to pull and parse data about movies from www.themoviedb.org.
- App uses **Picasso library** to display movies' posters on main navigation screen.
- App **opens movie trailer** using implicit intent.
- The **list of favorite movies** is stored in local **SQLite database** using **Content Provider**. 
- App uses **Tab layout** for displaying movies's detail.

(To be able to use app properly, insert TMDB API key in app:build.gradle within the defaultConfig)

![Alt text](readme/manifestGoogleMapsAPI.png?raw=true "AndroidManifest.xml")
