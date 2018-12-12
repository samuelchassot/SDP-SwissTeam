package ch.epfl.swissteam.services.providers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import ch.epfl.swissteam.services.utils.SettingsContract;
import ch.epfl.swissteam.services.utils.SettingsDbHelper;

/**
 * Class that provides some static methods to interact with the local settings DB.
 *
 * @author Julie Giunta
 */
public final class SettingsDBUtility {

    private SettingsDBUtility() {
    }

    private static final String selection = SettingsContract.SettingsEntry.COLUMN_ID + " = ?";

    /**
     * Check if an user has already a corresponding row in the database.
     *
     * @param helper, a SettingsDbHelper created with the context of the caller,
     * @param id,     the google ID of the user,
     * @return true if the user has already a row, false otherwise.
     */
    private static boolean userHasAlreadyARow(SettingsDbHelper helper, String id) {
        SQLiteDatabase db = helper.getReadableDatabase();

        // Define a projection that specifies which columns from the database
        // you will actually use after this query.
        String[] projection = {
                SettingsContract.SettingsEntry.COLUMN_ID
        };

        // Filter results WHERE "id" = 'id'
        String[] selectionArgs = {id};

        Cursor cursor = db.query(
                SettingsContract.SettingsEntry.TABLE_NAME,   // The table to query
                projection,             // The array of columns to return (pass null to get all)
                selection,              // The columns for the WHERE clause (selection)
                selectionArgs,          // The values for the WHERE clause (selectionArgs)
                null,                   // don't group the rows
                null,                   // don't filter by row groups
                null              // The sort order (sortOrder)
        );

        boolean exists = false;

        if (cursor.moveToFirst()) {
            exists = true;
        }
        cursor.close();
        db.close();

        return exists;
    }

    /**
     * Retrieve the value of the dark mode column for the row of the specified user.
     *
     * @param helper, a SettingsDbHelper created with the context of the caller,
     * @param id,     the google ID of the user,
     * @return an int corresponding to the value in the dark mode column of the table
     * or 0 if there was a problem when retrieving the value.
     */
    public static int retrieveDarkMode(SettingsDbHelper helper, String id) {
        SQLiteDatabase db = helper.getReadableDatabase();

        String[] projection = {
                SettingsContract.SettingsEntry.COLUMN_SETTINGS_DARKMODE
        };

        String[] selectionArgs = {id};
        int dark = 0;
        try {
            Cursor cursor = db.query(
                    SettingsContract.SettingsEntry.TABLE_NAME,
                    projection, selection, selectionArgs,
                    null, null, null
            );

            if (cursor.moveToFirst()) {
                dark = cursor.getInt(
                        cursor.getColumnIndexOrThrow(SettingsContract.SettingsEntry.COLUMN_SETTINGS_DARKMODE));
            }
            cursor.close();
        } catch (Exception e) {
            Log.e("SETTINGS_DB_UTILITY", "Could not query the DB");
        }
        db.close();

        return dark;
    }

    /**
     * Retrieve the value of the dark mode column for the row of the current user.
     *
     * @param context, a context to create a SettingsDbHelper with,
     * @return an int corresponding to the value in the dark mode column of the table
     * or 0 if there was a problem when retrieving the value.
     */
    public static int retrieveDarkMode(Context context) {
        return retrieveDarkMode(new SettingsDbHelper(context), GoogleSignInSingleton.get().getClientUniqueID());
    }


    /**
     * Retrieve the value of the radius column for the row of the specified user.
     *
     * @param helper, a SettingsDbHelper created with the context of the caller,
     * @param id,     the google ID of the user,
     * @return an int corresponding to the value in the radius column of the table
     * or LocationManager.MAX_POST_DISTANCE if there was a problem when retrieving the value.
     */
    public static int retrieveRadius(SettingsDbHelper helper, String id) {
        SQLiteDatabase db = helper.getReadableDatabase();

        String[] projection = {SettingsContract.SettingsEntry.COLUMN_SETTINGS_RADIUS};

        String[] selectionArgs = {id};

        int radius = (int) LocationManager.MAX_POST_DISTANCE;

        try {
            Cursor cursor = db.query(
                    SettingsContract.SettingsEntry.TABLE_NAME,
                    projection, selection, selectionArgs,
                    null, null, null
            );

            if (cursor.moveToFirst()) {
                radius = cursor.getInt(cursor.getColumnIndexOrThrow(SettingsContract.SettingsEntry.COLUMN_SETTINGS_RADIUS));
            }
            cursor.close();
        } catch (Exception e) {
            Log.e("SETTINGS_DB_UTILITY", "Could not query the DB");
        }
        db.close();

        return radius;
    }

    /**
     * Retrieve the value of the specified home column for a specified user
     *
     * @param helper, a SettingsDbHelper created with the context of the caller,
     * @param column, the column we want to retrieve, either the longitude or the latitude column
     * @param id,     the google ID of the user,
     * @return a double corresponding to the wanted value or 0 if there was a problem.
     */
    public static double retrieveHome(SettingsDbHelper helper, String column, String id) {
        SQLiteDatabase db = helper.getReadableDatabase();

        String[] projection = {column};

        String[] selectionArgs = {id};

        double home = 0;

        try {
            Cursor cursor = db.query(
                    SettingsContract.SettingsEntry.TABLE_NAME,
                    projection, selection, selectionArgs,
                    null, null, null
            );

            if (cursor.moveToFirst()) {
                home = cursor.getDouble(cursor.getColumnIndexOrThrow(column));
            }
            cursor.close();
        } catch (Exception e) {
            Log.e("SETTINGS_DB_UTILITY", "Could not query the DB");
        }
        db.close();

        return home;
    }

    /**
     * Update the value of the dark mode column for the row of the specified user.
     *
     * @param helper,   a SettingsDbHelper created with the context of the caller,
     * @param id,       the google ID of the user,
     * @param newValue, the value to put in the table.
     */
    public static void updateDarkMode(SettingsDbHelper helper, String id, int newValue) {
        updateInt(helper, SettingsContract.SettingsEntry.COLUMN_SETTINGS_DARKMODE, id, newValue);
    }

    /**
     * Update the value of the radius column for the row of the specified user.
     *
     * @param helper,   a SettingsDbHelper created with the context of the caller,
     * @param id,       the google ID of the user,
     * @param newValue, the value to put in the table.
     */
    public static void updateRadius(SettingsDbHelper helper, String id, int newValue) {
        updateInt(helper, SettingsContract.SettingsEntry.COLUMN_SETTINGS_RADIUS, id, newValue);
    }


    /**
     * Update the value of one of the home column for the row of the specified user.
     *
     * @param helper,   a SettingsDbHelper created with the context of the caller,     *
     * @param column,   the column you want to update, either the longitude or the latitude column
     * @param id,       the google ID of the user,
     * @param newValue, the value to put in the table.
     */
    public static void updateHome(SettingsDbHelper helper, String column, String id, double newValue) {
        ContentValues values = new ContentValues();
        values.put(column, newValue);

        updateValue(helper, id, values);
    }

    /**
     * Insert a new row of settings for a specified user if the user does not have already a row
     *
     * @param helper, a SettingsDbHelper created with the context of the caller,
     * @param id,     the google ID of the user,
     * @return the key of the newly inserted row or -1 either if there was already a row
     * or if there was a problem during the insertion.
     */
    public static long addRowIfNeeded(SettingsDbHelper helper, String id) {
        long rowKey = -1;
        if (!userHasAlreadyARow(helper, id)) {
            SQLiteDatabase db = helper.getWritableDatabase();

            //Value which will be stored as a row in the local DB
            ContentValues values = new ContentValues();
            values.put(SettingsContract.SettingsEntry.COLUMN_ID, id);

            //Store the new row in the DB
            rowKey = db.insert(SettingsContract.SettingsEntry.TABLE_NAME,
                    null,
                    values);
            db.close();
        }
        return rowKey;
    }

    /**
     * Update the value of one of the column which contains int (like dark mode or radius)
     * for the row of the specified user.
     *
     * @param helper,   a SettingsDbHelper created with the context of the caller,     *
     * @param column,   the column you want to update,
     * @param id,       the google ID of the user,
     * @param newValue, the value to put in the table.
     */
    private static void updateInt(SettingsDbHelper helper, String column, String id, int newValue) {
        ContentValues values = new ContentValues();
        values.put(column, newValue);

        updateValue(helper, id, values);
    }

    /**
     * Update the value of one of the column for the row of the specified user.
     *
     * @param helper,   a SettingsDbHelper created with the context of the caller,
     * @param id,       the google ID of the user,
     * @param newValue, the value to put in the table.
     */
    private static void updateValue(SettingsDbHelper helper, String id, ContentValues newValue) {
        SQLiteDatabase db = helper.getWritableDatabase();

        String[] selectionArgs = {id};

        db.update(SettingsContract.SettingsEntry.TABLE_NAME,
                newValue, selection, selectionArgs);

        db.close();
    }
}
