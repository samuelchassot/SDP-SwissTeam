package ch.epfl.swissteam.services;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public interface SettingsDBUtility {

    static boolean userHasAlreadyARow(SettingsDbHelper helper, String id){
        SQLiteDatabase db = helper.getReadableDatabase();

        // Define a projection that specifies which columns from the database
        // you will actually use after this query.
        String[] projection = {
                SettingsContract.SettingsEntry.COLUMN_ID
        };

        // Filter results WHERE "id" = 'id'
        String selection = SettingsContract.SettingsEntry.COLUMN_ID + " = ?";
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

    static int retrieveDarkMode(SettingsDbHelper helper, String id){
        SQLiteDatabase db = helper.getReadableDatabase();

        String[] projection = {
                SettingsContract.SettingsEntry.COLUMN_SETTINGS_DARKMODE
        };

        String selection = SettingsContract.SettingsEntry.COLUMN_ID + " = ?";
        String[] selectionArgs = { id };

        Cursor cursor = db.query(
                SettingsContract.SettingsEntry.TABLE_NAME,
                projection, selection, selectionArgs,
                null, null, null
        );

        int dark = -1;

        if(cursor.moveToFirst()){
            dark = cursor.getInt(
                    cursor.getColumnIndexOrThrow(SettingsContract.SettingsEntry.COLUMN_SETTINGS_DARKMODE));
        }
        cursor.close();
        db.close();

        return dark;
    }

    static int retrieveRadius(SettingsDbHelper helper, String id){
        SQLiteDatabase db = helper.getReadableDatabase();

        String[] projection = {SettingsContract.SettingsEntry.COLUMN_SETTINGS_RADIUS};

        String selection = SettingsContract.SettingsEntry.COLUMN_ID + " = ?";
        String[] selectionArgs = { id };

        Cursor cursor = db.query(
                SettingsContract.SettingsEntry.TABLE_NAME,
                projection, selection, selectionArgs,
                null, null, null
        );

        int radius = -1;

        if(cursor.moveToFirst()){
            radius = cursor.getInt(cursor.getColumnIndexOrThrow(SettingsContract.SettingsEntry.COLUMN_SETTINGS_RADIUS));
        }
        cursor.close();
        db.close();

        return radius;
    }

    static double retrieveHome(SettingsDbHelper helper, String column, String id){
        SQLiteDatabase db = helper.getReadableDatabase();

        String[] projection = {column};

        String selection = SettingsContract.SettingsEntry.COLUMN_ID + " = ?";
        String[] selectionArgs = { id };

        Cursor cursor = db.query(
                SettingsContract.SettingsEntry.TABLE_NAME,
                projection, selection, selectionArgs,
                null, null, null
        );

        double coord = -1;

        if(cursor.moveToFirst()){
            coord = cursor.getDouble(cursor.getColumnIndexOrThrow(column));
        }
        cursor.close();
        db.close();

        return coord;
    }

    static void updateDarkMode(SettingsDbHelper helper, String id, int newValue){
        SQLiteDatabase db = helper.getWritableDatabase();

        // New value for one column
        ContentValues values = new ContentValues();
        values.put(SettingsContract.SettingsEntry.COLUMN_SETTINGS_DARKMODE, newValue);

        // Which row to update, based on the id
        String selection = SettingsContract.SettingsEntry.COLUMN_ID + " LIKE ?";
        String[] selectionArgs = { id };

        db.update(SettingsContract.SettingsEntry.TABLE_NAME,
                values, selection, selectionArgs);

        db.close();
    }

    static void updateRadius(SettingsDbHelper helper, String id, int newValue){
        SQLiteDatabase db = helper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(SettingsContract.SettingsEntry.COLUMN_SETTINGS_RADIUS, newValue);

        String selection = SettingsContract.SettingsEntry.COLUMN_ID + " LIKE ?";
        String[] selectionArgs = { id };

        db.update(SettingsContract.SettingsEntry.TABLE_NAME,
                values, selection, selectionArgs);

        db.close();
    }

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
}
