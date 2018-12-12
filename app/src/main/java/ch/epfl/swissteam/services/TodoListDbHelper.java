package ch.epfl.swissteam.services;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Helper class for SQLite database for the posts of the To do list
 *
 * @author Julie Giunta
 */
public class TodoListDbHelper extends SQLiteOpenHelper{

    /**
     * Creates a SettingsDbHelper
     * @param context, the context of the creator.
     */
    public TodoListDbHelper(Context context){
        super(context, TodoListContract.LOCAL_DB_NAME, null, TodoListContract.DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + TodoListContract.TodolistEntry.TABLE_NAME + " (" +
                TodoListContract.TodolistEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                TodoListContract.TodolistEntry.COLUMN_ID + " VARCHAR(30) NOT NULL," +
                TodoListContract.TodolistEntry.COLUMN_POSTS + " VARCHAR(50) NOT NULL)";

        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TodoListContract.TodolistEntry.TABLE_NAME);
        onCreate(db);
    }
}
