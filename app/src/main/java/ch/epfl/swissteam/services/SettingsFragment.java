package ch.epfl.swissteam.services;

import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.util.Log;
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

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * A fragment to set the different settings of the application
 *
 * @author Ghali Chraïbi
 */
public class SettingsFragment extends Fragment implements OnMapReadyCallback {

    private static final String MAPVIEW_BUNDLE_KEY = "MapViewBundleKey";
    private static final double KILOMETER_TO_METER_FACTOR = 1000.0;

    private GoogleMap googleMap_;
    private MapView mapView_;
    private Marker homeMarker_;

    private SettingsDbHelper dbHelper_;
    private String id_;

    private double homeLng_, homeLat_;

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
        retrieveHomeLocation();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        // Toolbar
        Toolbar toolbar = getActivity().findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.toolbar_settings);
      
        View view = inflater.inflate(R.layout.fragment_settings, container, false);

        Bundle mapViewBundle = null;
        if (savedInstanceState != null) {
            mapViewBundle = savedInstanceState.getBundle(MAPVIEW_BUNDLE_KEY);
        }

        mapView_ = view.findViewById(R.id.mapview_settings);
        mapView_.onCreate(mapViewBundle);
        mapView_.getMapAsync(this);

        Button setHome = view.findViewById(R.id.button_settings_sethome);
        setHome.setOnClickListener(v -> {
            Location currentLocation = LocationManager.get().getCurrentLocation_();
            updateHomeLocation(currentLocation.getLatitude(), currentLocation.getLongitude());
            updateMapView();
        });


        Button deleteAccountButton = (Button) view.findViewById(R.id.button_settings_deleteaccount);
        deleteAccountButton.setOnClickListener(v->{
            Intent intent = new Intent(v.getContext(), DeleteAccountActivity.class);
            v.getContext().startActivity(intent);
        });

        constructDarkModeSettings(view);
        constructRadiusSettings(view);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        mapView_.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView_.onResume();
    }

    @Override
    public void onStop() {
        super.onStop();
        mapView_.onStop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView_.onDestroy();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView_.onPause();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView_.onLowMemory();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        Bundle mapViewBundle = outState.getBundle(MAPVIEW_BUNDLE_KEY);
        if (mapViewBundle == null) {
            mapViewBundle = new Bundle();
            outState.putBundle(MAPVIEW_BUNDLE_KEY, mapViewBundle);
        }

        mapView_.onSaveInstanceState(mapViewBundle);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        googleMap_ = googleMap;
        googleMap_.setMinZoomPreference(12);
        updateMapView();
    }

    private void updateMapView() {
        retrieveHomeLocation();

        LatLng newLatLng = new LatLng(homeLat_, homeLng_);
        googleMap_.moveCamera(CameraUpdateFactory.newLatLng(newLatLng));

        if (homeMarker_ == null) {
            homeMarker_ = googleMap_.addMarker(new MarkerOptions().position(newLatLng));
        } else {
            homeMarker_.setPosition(newLatLng);
        }
    }

    private void retrieveHomeLocation() {
        homeLng_ = SettingsDBUtility.retrieveHome(dbHelper_, SettingsContract.SettingsEntry.COLUMN_SETTINGS_HOME_LONGITUDE, id_);
        homeLat_ = SettingsDBUtility.retrieveHome(dbHelper_, SettingsContract.SettingsEntry.COLUMN_SETTINGS_HOME_LATITUDE, id_);
    }

    private void updateHomeLocation(double newLat, double newLng) {
        SettingsDBUtility.updateHome(dbHelper_, SettingsContract.SettingsEntry.COLUMN_SETTINGS_HOME_LATITUDE, id_, newLat);
        SettingsDBUtility.updateHome(dbHelper_, SettingsContract.SettingsEntry.COLUMN_SETTINGS_HOME_LONGITUDE, id_, newLng);
    }

      
    private void constructDarkModeSettings(View view){
        Switch darkModeSwitch = view.findViewById(R.id.switch_settings_darkmode);
        //Retrieve Dark mode from local DB
        int dark = SettingsDBUtility.retrieveDarkMode(dbHelper_, id_);
        boolean darkModeChecked = dark == 1;
        darkModeSwitch.setChecked(darkModeChecked);
        darkModeSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if(isChecked){
                SettingsDBUtility.updateDarkMode(dbHelper_, id_, 1);
                getActivity().setTheme(R.style.DarkMode);
            }else{
                SettingsDBUtility.updateDarkMode(dbHelper_, id_, 0);
                getActivity().setTheme(R.style.AppTheme);

                Log.e("Dark", "Unset");
            }
            getActivity().recreate();

        });

    }

    private void constructRadiusSettings(View view){
        //Retrieve radius from local DB
        int radius = SettingsDBUtility.retrieveRadius(dbHelper_, id_);

        TextView textview = view.findViewById(R.id.textview_settings_currentradius);
        String currentRadius = String.format(Locale.ENGLISH,
                getResources().getString(R.string.settings_seekbar_currentradius) + " %.2f km",
                radius/KILOMETER_TO_METER_FACTOR);
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
                String displayCurrentRadius = String.format(Locale.ENGLISH,
                        getResources().getString(R.string.settings_seekbar_currentradius) + " %.2f km",
                        progress/KILOMETER_TO_METER_FACTOR);
                textview.setText(displayCurrentRadius);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                SettingsDBUtility.updateRadius(dbHelper_, id_, progress);

                String displayCurrentRadius = String.format(Locale.ENGLISH,
                        getResources().getString(R.string.settings_seekbar_currentradius) + " %.2f km",
                        progress/KILOMETER_TO_METER_FACTOR);
                textview.setText(displayCurrentRadius);
            }
        });
    }
}
