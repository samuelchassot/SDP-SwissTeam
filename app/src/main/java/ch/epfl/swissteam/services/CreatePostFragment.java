package ch.epfl.swissteam.services;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;

import java.util.Date;


/**
 * A fragment to create a new spontaneous post.
 *
 * @author Adrian Baudat
 */
public class CreatePostFragment extends Fragment implements View.OnClickListener {

    private SettingsDbHelper dbHelper_;
    private String id_;
    private boolean homeLocation;

    public CreatePostFragment() {
        // Required empty public constructor
    }

    /**
     * Creates a new {@link CreatePostFragment}.
     *
     * @return new instance of <code>CreatePostFragment</code>
     */
    public static CreatePostFragment newInstance() {
        CreatePostFragment fragment = new CreatePostFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        dbHelper_ = new SettingsDbHelper(this.getContext());
        id_ = GoogleSignInSingleton.get().getClientUniqueID();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View frag = inflater.inflate(R.layout.fragment_create_post, container, false);
        ((Button) frag.findViewById(R.id.button_createpostfragment_send)).setOnClickListener(this);

        // Add a switchButton with a textView indicating which location to use
        Switch switchButton = (Switch) frag.findViewById(R.id.switch_createpostfragment_location);
        TextView switchTextInfo = (TextView) frag.findViewById(R.id.textView_createpostfragment);

            // Initialise the textView
        if (switchButton.isChecked()) {
            switchTextInfo.setText("Home Location");
            homeLocation = true;
        } else {
            switchTextInfo.setText("Current Location");
            homeLocation = false;
        }

            // Make the textView change when we switch the slider
        switchButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
                if (checked) {
                    switchTextInfo.setText("Home Location");
                    homeLocation = true;
                } else {
                    switchTextInfo.setText("Current Location");
                    homeLocation = false;
                }
            }
        });

        return frag;
    }

    @Override
    public void onClick(View v) {

        EditText titleField = ((EditText) getView().findViewById(R.id.plaintext_createpostfragment_title));
        EditText bodyField = ((EditText) getView().findViewById(R.id.plaintext_createpostfragment_body));


        if (TextUtils.isEmpty(titleField.getText())) {
            Toast.makeText(getActivity(), R.string.createpostfragment_titleempty, Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(bodyField.getText())) {
            Toast.makeText(getActivity(), R.string.createpostfragment_bodyempty, Toast.LENGTH_SHORT).show();
        } else {
            String title = titleField.getText().toString();
            String body = bodyField.getText().toString();

            String googleID = GoogleSignInSingleton.get().getClientUniqueID();
            long timestamp = (new Date()).getTime();
            String key = googleID + "_" + timestamp;

            // choose location according to the Slider state
            double latitude, longitude;
            if (homeLocation) {
                latitude = SettingsDBUtility.retrieveHome(dbHelper_, SettingsContract.SettingsEntry.COLUMN_SETTINGS_HOME_LATITUDE, id_);
                longitude = SettingsDBUtility.retrieveHome(dbHelper_, SettingsContract.SettingsEntry.COLUMN_SETTINGS_HOME_LONGITUDE, id_);
            } else {
                Location location = LocationManager.get().getCurrentLocation_();
                if (location != null) {
                    latitude = location.getLatitude();
                    longitude = location.getLongitude();
                } else {
                    latitude = 0;
                    longitude = 0;
                }
            }

            DBUtility.get().getUser(googleID, user -> {
                Post post = new Post(key, title, googleID, body, timestamp, longitude, latitude);
                post.addToDB(DBUtility.get().getDb_());
            });

            ((MainActivity) getActivity()).showHomeFragment();
        }
    }
}
