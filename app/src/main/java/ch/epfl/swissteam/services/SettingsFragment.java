package ch.epfl.swissteam.services;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.content.Intent;
import android.widget.Switch;

/**
 * A fragment to set the different settings of the application
 *
 * @author Ghali Chraïbi
 */
public class SettingsFragment extends Fragment {

    private SettingsDbHelper dbHelper_;

    public SettingsFragment() {
        // Required empty public constructor
    }

    /**
     * Create a new instance of this fragment
     *
     * @return the settings fragment
     */
    public static SettingsFragment newInstance() {
        SettingsFragment fragment = new SettingsFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Toolbar toolbar = getActivity().findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.toolbar_settings);

        dbHelper_ = new SettingsDbHelper(this.getContext());

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, container, false);

        Button deleteAccountButton = (Button) view.findViewById(R.id.button_settings_deleteaccount);
        deleteAccountButton.setOnClickListener(v->{
            Intent intent = new Intent(v.getContext(), DeleteAccountActivity.class);
            v.getContext().startActivity(intent);
        });

        Switch darkModeSwitch = view.findViewById(R.id.switch_settings_darkmode);
        darkModeSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {

            SQLiteDatabase db = dbHelper_.getWritableDatabase();

            // New value for one column
            ContentValues values = new ContentValues();

            // Which row to update, based on the mode
            String selection = SettingsContract.SettingsEntry.COLUMN_SETTINGS_DARKMODE + " LIKE ?";
            String[] selectionArgs = {"0"};

            if(isChecked){
                values.put(SettingsContract.SettingsEntry.COLUMN_SETTINGS_DARKMODE, 1);
            }else{
                values.put(SettingsContract.SettingsEntry.COLUMN_SETTINGS_DARKMODE, 0);
                selectionArgs[0] = "1";
            }

            db.update(SettingsContract.SettingsEntry.TABLE_NAME,
                    values, selection, selectionArgs);

            db.close();
        });

        //Retrieve data from locale database
        SQLiteDatabase db = dbHelper_.getReadableDatabase();

        // Define a projection that specifies which columns from the database
        // you will actually use after this query.
        String[] projection = {
                SettingsContract.SettingsEntry._ID,
                SettingsContract.SettingsEntry.COLUMN_SETTINGS_DARKMODE
                //,SettingsContract.SettingsEntry.COLUMN_SETTINGS_HOME_LONGITUDE...
        };

        // Filter results WHERE "title" = 'My Title'
        //String selection = FeedEntry.COLUMN_NAME_TITLE + " = ?";
        //String[] selectionArgs = { "My Title" };

        // How you want the results sorted in the resulting Cursor
        //String sortOrder =
        //        FeedEntry.COLUMN_NAME_SUBTITLE + " DESC";

        Cursor cursor = db.query(
                SettingsContract.SettingsEntry.TABLE_NAME,   // The table to query
                projection,             // The array of columns to return (pass null to get all)
                null,              // The columns for the WHERE clause (selection)
                null,          // The values for the WHERE clause (selectionArgs)
                null,                   // don't group the rows
                null,                   // don't filter by row groups
                null              // The sort order (sortOrder)
        );

        int dark = 0;

        if(cursor.moveToFirst()){
            dark = cursor.getInt(
                    cursor.getColumnIndexOrThrow(SettingsContract.SettingsEntry.COLUMN_SETTINGS_DARKMODE));
        }

        boolean darkModeChecked = dark == 1;
        darkModeSwitch.setChecked(darkModeChecked);

        cursor.close();

        return view;
    }
}
