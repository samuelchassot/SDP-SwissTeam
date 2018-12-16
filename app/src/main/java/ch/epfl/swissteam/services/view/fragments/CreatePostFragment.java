package ch.epfl.swissteam.services.view.fragments;

import android.content.Context;
import android.location.Location;
import android.support.v4.app.Fragment;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Date;

import ch.epfl.swissteam.services.R;
import ch.epfl.swissteam.services.utils.SettingsContract;
import ch.epfl.swissteam.services.providers.SettingsDBUtility;
import ch.epfl.swissteam.services.utils.SettingsDbHelper;
import ch.epfl.swissteam.services.models.Post;
import ch.epfl.swissteam.services.providers.DBUtility;
import ch.epfl.swissteam.services.providers.GoogleSignInSingleton;
import ch.epfl.swissteam.services.providers.LocationManager;
import ch.epfl.swissteam.services.utils.ActivityUtils;
import ch.epfl.swissteam.services.utils.Utils;
import ch.epfl.swissteam.services.view.activities.MainActivity;


/**
 * A fragment to create a new spontaneous post.
 *
 * @author Adrian Baudat
 */
public class CreatePostFragment extends Fragment implements View.OnClickListener {

    private SettingsDbHelper dbHelper_;
    private String id_;
    private boolean isHomeLocation_;
    private Date timeoutDate_;

    /**
     * An enum containing different periods of time, used to represent lifetime of posts
     */
    public enum TimeOut {
        oneDay,
        threeDays,
        oneWeek,
        twoWeeks;

        private static Context ctx;

        public static void setContext(Context ctx_){
            ctx = ctx_;
        }

        @Override
        public String toString() {
            String result = "";
            if(ctx != null) {
                switch (this) {
                    case oneDay:
                        result = ctx.getString(R.string.createpost_timeout_oneday);
                        break;
                    case threeDays:
                        result = ctx.getString(R.string.createpost_timeout_threedays);
                        break;
                    case oneWeek:
                        result = ctx.getString(R.string.createpost_timeout_oneweek);
                        break;
                    case twoWeeks:
                        result = ctx.getString(R.string.createpost_timeout_twoweeks);
                        break;

                }
            }
            return result;
        }

        public Date getTimeoutDate(){
            Calendar cal = Calendar.getInstance();
            switch(this){
                case oneDay:
                    cal.add(Calendar.DAY_OF_YEAR, 1);
                    break;
                case threeDays:
                    cal.add(Calendar.DAY_OF_YEAR, 3);
                    break;
                case oneWeek:
                    cal.add(Calendar.WEEK_OF_YEAR, 1);
                    break;
                case twoWeeks:
                    cal.add(Calendar.WEEK_OF_YEAR, 2);
                    break;
            }

            return cal.getTime();
        }

    }

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
            switchTextInfo.setText(R.string.createpostfragment_location_switch_on);
            isHomeLocation_ = true;
        } else {
            switchTextInfo.setText(R.string.createpostfragment_location_switch_off);
            isHomeLocation_ = false;
        }

            // Make the textView change when we switch the slider
        switchButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
                if (checked) {
                    switchTextInfo.setText(R.string.createpostfragment_location_switch_on);
                    isHomeLocation_ = true;
                } else {
                    switchTextInfo.setText(R.string.createpostfragment_location_switch_off);
                    isHomeLocation_ = false;
                }
            }
        });

        TimeOut.setContext(this.getContext());
        Spinner timeoutSpinner = frag.findViewById(R.id.spinner_createpost_timeout);
        ArrayAdapter<TimeOut> timeOutArrayAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, TimeOut.values());
        timeOutArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        timeoutSpinner.setAdapter(timeOutArrayAdapter);

        timeoutSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                timeoutDate_ = ((TimeOut)adapterView.getItemAtPosition(i)).getTimeoutDate();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        return frag;
    }

    @Override
    public void onClick(View v) {

        EditText titleField = ((EditText) getView().findViewById(R.id.plaintext_createpostfragment_title));
        EditText bodyField = ((EditText) getView().findViewById(R.id.plaintext_createpostfragment_body));
        Spinner timeoutSpinner = getView().findViewById(R.id.spinner_createpost_timeout);


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
            if (isHomeLocation_) {
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
                Post post = new Post(key, title, googleID, body, timestamp, longitude, latitude, Utils.dateToString(timeoutDate_));
                post.addToDB(DBUtility.get().getDb_());
            ((MainActivity) getActivity()).showMyPostsFragment();
            });
            ActivityUtils.hideKeyboard(this.getActivity());
        }
    }
}
