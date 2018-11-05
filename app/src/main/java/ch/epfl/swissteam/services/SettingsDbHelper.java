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
                SettingsContract.SettingsEntry._ID + " INTEGER PRIMARY KEY," +
                SettingsContract.SettingsEntry.COLUMN_NAME_DARKMODE + " INTEGER DEFAULT 0," +
                SettingsContract.SettingsEntry.COLUMN_NAME_RADIUS + " FLOAT DEFAULT " + LocationManager.MAX_POST_DISTANCE + "," +
                SettingsContract.SettingsEntry.COLUMN_NAME_HOME_LONGITUDE + " REAL," +
                SettingsContract.SettingsEntry.COLUMN_NAME_HOME_LATITUDE+ " REAL)";

        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + SettingsContract.SettingsEntry.TABLE_NAME);
        onCreate(db);
    }
}
