package com.example.android.moviesapp.DbData;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by evi on 17. 3. 2018.
 */

public class FavoritesContract {
    public static final String AUTHORITY = "com.example.android.moviesapp";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);
    public static final String PATH_FAVORITES = "favorites";

    public static final class FavoritesEntry implements BaseColumns {

        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_FAVORITES).build();

        public static final String TABLE_NAME = "favorites";

        public static final String COLUMN_MOVIE_ID = "movieId";
        public static final String COLUMNN_TITLE = "title";
        public static final String COLUMN_POSTER_URL = "posterUrl";


    }
}
