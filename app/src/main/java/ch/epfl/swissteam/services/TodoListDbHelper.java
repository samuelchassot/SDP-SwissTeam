package ch.epfl.swissteam.services;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Helper class for SQLite database for the posts of the To do list
 *
 * @author Julie Giunta
 */
public class TodolistDbHelper extends SQLiteOpenHelper{

    /**
     * Creates a SettingsDbHelper
     * @param context, the context of the creator.
     */
    public TodolistDbHelper(Context context){
        super(context, TodolistContract.LOCAL_DB_NAME, null, TodolistContract.DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + TodolistContract.TodolistEntry.TABLE_NAME + " (" +
                TodolistContract.TodolistEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                TodolistContract.TodolistEntry.COLUMN_ID + " VARCHAR(30) NOT NULL," +
                TodolistContract.TodolistEntry.COLUMN_POSTS + " VARCHAR(50) NOT NULL)";

        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TodolistContract.TodolistEntry.TABLE_NAME);
        onCreate(db);
    }
}
