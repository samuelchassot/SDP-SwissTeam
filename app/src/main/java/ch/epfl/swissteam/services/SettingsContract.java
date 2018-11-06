package ch.epfl.swissteam.services;

import android.provider.BaseColumns;

/**
 * Contract defining the database table name and column names
 * for a single table representing the settings of a client
 *
 * @author Julie Giunta
 */
public class SettingsContract {
    public static final String LOCAL_DB_NAME = "ch.epfl.swissteam.services.localDB";
    public static final int DB_VERSION = 1;

    private SettingsContract(){}

    public static class SettingsEntry{
        public static final String TABLE_NAME = "entry";

        //Column for the id of the logged in user
        public static final String COLUMN_NAME_ID = "id";

        //Column for the dark mode
        //By default 0, which means normal mode
        public static final String COLUMN_NAME_DARKMODE = "darkmode";

        //Column for the radius at which the user wants to see posts
        //By default LocationManager.MAX_POST_DISTANCE
        public static final String COLUMN_NAME_RADIUS = "radius";

        //Columns for home location of the user
        public static final String COLUMN_NAME_HOME_LONGITUDE = "home_longitude";
        public static final String COLUMN_NAME_HOME_LATITUDE = "home_latitude";
    }
}
