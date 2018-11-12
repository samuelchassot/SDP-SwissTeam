package ch.epfl.swissteam.services;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/**
 * Interface that provides some static methods to interact with the local settings DB.
 *
 * @author Julie Giunta
 */
public interface SettingsDBUtility {
    String selection = SettingsContract.SettingsEntry.COLUMN_ID + " = ?";

    /**
     * Check if an user has already a corresponding row in the database.
     * @param helper, a SettingsDbHelper created with the context of the caller,
     * @param id, the google ID of the user,
     * @return true if the user has already a row.
     */
    static boolean userHasAlreadyARow(SettingsDbHelper helper, String id){
        SQLiteDatabase db = helper.getReadableDatabase();

        // Define a projection that specifies which columns from the database
        // you will actually use after this query.
        String[] projection = {
                SettingsContract.SettingsEntry.COLUMN_ID
        };

        // Filter results WHERE "id" = 'id'
        String[] selectionArgs = { id };

        // How you want the results sorted in the resulting Cursor
        //String sortOrder =
        //        FeedEntry.COLUMN_NAME_SUBTITLE + " DESC";

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

        if(cursor.moveToFirst()){
            exists = true;
        }
        cursor.close();
        db.close();

        return exists;
    }

    /**
     * Retrieve the value of the darkmode column for the row of the specified user.
     * @param helper, a SettingsDbHelper created with the context of the caller,
     * @param id, the google ID of the user,
     * @return an int corresponding to the value in the darkmode column of the table
     *          or 0 if there was a problem when retrieving the value.
     */
    static int retrieveDarkMode(SettingsDbHelper helper, String id){
        SQLiteDatabase db = helper.getReadableDatabase();

        String[] projection = {
                SettingsContract.SettingsEntry.COLUMN_SETTINGS_DARKMODE
        };

        String[] selectionArgs = { id };
        int dark = 0;
        try{
            Cursor cursor = db.query(
                    SettingsContract.SettingsEntry.TABLE_NAME,
                    projection, selection, selectionArgs,
                    null, null, null
            );

            if(cursor.moveToFirst()){
                dark = cursor.getInt(
                        cursor.getColumnIndexOrThrow(SettingsContract.SettingsEntry.COLUMN_SETTINGS_DARKMODE));
            }
            cursor.close();
        }catch(Exception e){
            Log.e("SETTINGSDBUTILITY", "Could not query the DB");
        }
        db.close();

        return dark;
    }

    /**
     * Retrieve the value of the radius column for the row of the specified user.
     * @param helper, a SettingsDbHelper created with the context of the caller,
     * @param id, the google ID of the user,
     * @return an int corresponding to the value in the radius column of the table
     *          or LocationManager.MAX_POST_DISTANCE if there was a problem when retrieving the value.
     */
    static int retrieveRadius(SettingsDbHelper helper, String id){
        SQLiteDatabase db = helper.getReadableDatabase();

        String[] projection = {SettingsContract.SettingsEntry.COLUMN_SETTINGS_RADIUS};

        String[] selectionArgs = { id };

        int radius = (int)LocationManager.MAX_POST_DISTANCE;

        try{
            Cursor cursor = db.query(
                    SettingsContract.SettingsEntry.TABLE_NAME,
                    projection, selection, selectionArgs,
                    null, null, null
            );

            if(cursor.moveToFirst()){
                radius = cursor.getInt(cursor.getColumnIndexOrThrow(SettingsContract.SettingsEntry.COLUMN_SETTINGS_RADIUS));
            }
            cursor.close();
        }catch(Exception e){
            Log.e("SETTINGSDBUTILITY", "Could not query the DB");
        }
        db.close();

        return radius;
    }

    /**
     * Retrieve the value of the specified home column for a specified user
     * @param helper, a SettingsDbHelper created with the context of the caller,
     * @param column, the column we want to retrieve, either the longitude or the latitude column
     * @param id, the google ID of the user,
     * @return a double corresponding to the wanted value or 0 if there was a problem.
     */
    static double retrieveHome(SettingsDbHelper helper, String column, String id){
        SQLiteDatabase db = helper.getReadableDatabase();

        String[] projection = {column};

        String[] selectionArgs = { id };

        double coord = 0;

        try{
            Cursor cursor = db.query(
                    SettingsContract.SettingsEntry.TABLE_NAME,
                    projection, selection, selectionArgs,
                    null, null, null
            );

            if(cursor.moveToFirst()){
                coord = cursor.getDouble(cursor.getColumnIndexOrThrow(column));
            }
            cursor.close();
        }catch(Exception e){
            Log.e("SETTINGSDBUTILITY", "Could not query the DB");
        }
        db.close();

        return coord;
    }

    /**
     * Update the value of the darkmode column for the row of the specified user.
     * @param helper, a SettingsDbHelper created with the context of the caller,
     * @param id, the google ID of the user,
     * @param newValue, the value to put in the table.
     */
    static void updateDarkMode(SettingsDbHelper helper, String id, int newValue){
        updateInt(helper, SettingsContract.SettingsEntry.COLUMN_SETTINGS_DARKMODE, id, newValue);
    }

    /**
     * Update the value of the radius column for the row of the specified user.
     * @param helper, a SettingsDbHelper created with the context of the caller,
     * @param id, the google ID of the user,
     * @param newValue, the value to put in the table.
     */
    static void updateRadius(SettingsDbHelper helper, String id, int newValue){
        updateInt(helper, SettingsContract.SettingsEntry.COLUMN_SETTINGS_RADIUS, id, newValue);
    }


    /**
     * Update the value of one of the home column for the row of the specified user.
     * @param helper, a SettingsDbHelper created with the context of the caller,     *
     * @param column, the column you want to update, either the longitude or the latitude column
     * @param id, the google ID of the user,
     * @param newValue, the value to put in the table.
     */
    static void updateHome(SettingsDbHelper helper, String column, String id, double newValue){
        SQLiteDatabase db = helper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(column, newValue);

        String[] selectionArgs = { id };

        db.update(SettingsContract.SettingsEntry.TABLE_NAME,
                values, selection, selectionArgs);

        db.close();
    }

    /**
     * Insert a new row of settings for a specified user if the user does not have already a row
     * @param helper, a SettingsDbHelper created with the context of the caller,
     * @param id, the google ID of the user,
     * @return the key of the newly inserted row or -1 either if there was already a row
     *          or if there was a problem during the insertion.
     */
    static long addRowIfNeeded(SettingsDbHelper helper, String id){
        long rowKey = -1;
        if(!userHasAlreadyARow(helper, id)){
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
     * Update the value of one of the column which contains int (like darkmode or radius)
     * for the row of the specified user.
     * @param helper, a SettingsDbHelper created with the context of the caller,     *
     * @param column, the column you want to update,
     * @param id, the google ID of the user,
     * @param newValue, the value to put in the table.
     */
     static void updateInt(SettingsDbHelper helper, String column, String id, int newValue){
         SQLiteDatabase db = helper.getWritableDatabase();

         ContentValues values = new ContentValues();
         values.put(column, newValue);

         String[] selectionArgs = { id };

         db.update(SettingsContract.SettingsEntry.TABLE_NAME,
                 values, selection, selectionArgs);

         db.close();
    }
}
