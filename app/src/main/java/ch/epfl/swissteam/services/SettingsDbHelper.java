package ch.epfl.swissteam.services;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Helper class for SQLite database for the settings
 *
 * @author Julie Giunta
 */
public class SettingsDbHelper extends SQLiteOpenHelper{

    public SettingsDbHelper(Context context){
        super(context, SettingsContract.LOCAL_DB_NAME, null, SettingsContract.DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + SettingsContract.SettingsEntry.TABLE_NAME + " (" +
                SettingsContract.SettingsEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                SettingsContract.SettingsEntry.COLUMN_ID + " VARCHAR(30) NOT NULL," +
                SettingsContract.SettingsEntry.COLUMN_SETTINGS_DARKMODE + " INTEGER DEFAULT 0," +
                SettingsContract.SettingsEntry.COLUMN_SETTINGS_RADIUS + " INTEGER DEFAULT 300000," +
                SettingsContract.SettingsEntry.COLUMN_SETTINGS_HOME_LONGITUDE + " REAL DEFAULT 0," +
                SettingsContract.SettingsEntry.COLUMN_SETTINGS_HOME_LATITUDE + " REAL DEFAULT 0)";

        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + SettingsContract.SettingsEntry.TABLE_NAME);
        onCreate(db);
    }
}
