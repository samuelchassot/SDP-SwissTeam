package ch.epfl.swissteam.services;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.content.Intent;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Locale;

/**
 * A fragment to set the different settings of the application
 *
 * @author Ghali Chraïbi
 */
public class SettingsFragment extends Fragment {

    private SettingsDbHelper dbHelper_;
    private String id_;

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

        dbHelper_ = new SettingsDbHelper(this.getContext());
        id_ = GoogleSignInSingleton.get().getClientUniqueID();

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, container, false);

        // Toolbar
        Toolbar toolbar = getActivity().findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.toolbar_settings);

        Button deleteAccountButton = (Button) view.findViewById(R.id.button_settings_deleteaccount);
        deleteAccountButton.setOnClickListener(v->{
            Intent intent = new Intent(v.getContext(), DeleteAccountActivity.class);
            v.getContext().startActivity(intent);
        });

        constructDarkModeSettings(view);
        constructRadiusSettings(view);


        //Home
        double longitude = SettingsDBUtility.retrieveHome(dbHelper_, SettingsContract.SettingsEntry.COLUMN_SETTINGS_HOME_LONGITUDE, id_);
        double latitude = SettingsDBUtility.retrieveHome(dbHelper_, SettingsContract.SettingsEntry.COLUMN_SETTINGS_HOME_LATITUDE, id_);

        return view;
    }

    private void constructDarkModeSettings(View view){
        Switch darkModeSwitch = view.findViewById(R.id.switch_settings_darkmode);
        darkModeSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if(isChecked){
                SettingsDBUtility.updateDarkMode(dbHelper_, id_, 1);
            }else{
                SettingsDBUtility.updateDarkMode(dbHelper_, id_, 0);
            }
        });

        //Retrieve Dark mode from local DB
        int dark = SettingsDBUtility.retrieveDarkMode(dbHelper_, id_);
        boolean darkModeChecked = dark == 1;
        darkModeSwitch.setChecked(darkModeChecked);
    }

    private void constructRadiusSettings(View view){
        //Retrieve radius from local DB
        int radius = SettingsDBUtility.retrieveRadius(dbHelper_, id_);

        TextView textview = view.findViewById(R.id.textview_settings_currentradius);
        String currentRadius = String.format(Locale.ENGLISH,
                getResources().getString(R.string.settings_seekbar_currentradius) + " %.2f km",
                radius/1000.0);
        textview.setText(currentRadius);

        constructSeekBar(view, radius, textview);
    }

    private void constructSeekBar(View view, int radius, TextView textview){
        SeekBar radiusSeekBar = view.findViewById(R.id.seekbar_settings_radius);
        radiusSeekBar.setProgress(radius);
        radiusSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int progress = radius;

            @Override
            public void onProgressChanged(SeekBar seekBar, int progressValue, boolean fromUser) {
                progress = progressValue;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                SettingsDBUtility.updateRadius(dbHelper_, id_, progress);

                String displayCurrentRadius = String.format(Locale.ENGLISH,
                        getResources().getString(R.string.settings_seekbar_currentradius) + " %.2f km",
                        progress/1000.0);
                textview.setText(displayCurrentRadius);
            }
        });
    }
}
