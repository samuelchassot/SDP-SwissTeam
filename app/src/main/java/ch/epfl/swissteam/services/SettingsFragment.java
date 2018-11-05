package ch.epfl.swissteam.services;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.content.Intent;
import android.widget.CompoundButton;
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
            String selection = SettingsContract.SettingsEntry.COLUMN_NAME_DARKMODE + " LIKE ?";
            String[] selectionArgs = {"0"};

            if(isChecked){
                values.put(SettingsContract.SettingsEntry.COLUMN_NAME_DARKMODE, 1);
            }else{
                values.put(SettingsContract.SettingsEntry.COLUMN_NAME_DARKMODE, 0);
                selectionArgs[0] = "1";
            }

            db.update(SettingsContract.SettingsEntry.TABLE_NAME,
                    values, selection, selectionArgs);
        });

        return view;
    }
}
