package ch.epfl.swissteam.services;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.location.LocationServices;

import java.util.Date;


/**
 * A fragment to create a new spontaneous post.
 *
 * @author Adrian Baudat
 */
public class CreatePostFragment extends Fragment implements View.OnClickListener {

    public CreatePostFragment() {
        // Required empty public constructor
    }

    /**
     * Creates a new {@link CreatePostFragment}.
     * @return new instance of <code>CreatePostFragment</code>
     */
    public static CreatePostFragment newInstance() {
        CreatePostFragment fragment = new CreatePostFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View frag = inflater.inflate(R.layout.fragment_create_post, container, false);
        ((Button) frag.findViewById(R.id.button_createpostfragment_send)).setOnClickListener(this);
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

            /*
            if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    ActivityCompat.requestPermissions(getActivity(),
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION},
                        1);
                }
                else{
                    Location zeroLocation = new Location("");
                    zeroLocation.setLongitude(0);
                    zeroLocation.setLatitude(0);
                    DBUtility.get().getUser(googleID, user -> {
                        Post post = new Post(key, title, googleID, body, timestamp, zeroLocation.getLongitude(), zeroLocation.getLatitude());
                        post.addToDB(DBUtility.get().getDb_());
                    });
                }
            }
            else {
                LocationServices.getFusedLocationProviderClient(getActivity()).getLastLocation()
                        .addOnSuccessListener(location -> {
                            DBUtility.get().getUser(googleID, user -> {
                                Post post = new Post(key, title, googleID, body, timestamp, location.getLongitude(), location.getLatitude());
                                post.addToDB(DBUtility.get().getDb_());
                            });
                        });

                ((MainActivity) getActivity()).showHomeFragment();
            }
            */
            Location zeroLocation = new Location("");
            zeroLocation.setLongitude(0);
            zeroLocation.setLatitude(0);
            DBUtility.get().getUser(googleID, user -> {
                Post post = new Post(key, title, googleID, body, timestamp, zeroLocation.getLongitude(), zeroLocation.getLatitude());
                post.addToDB(DBUtility.get().getDb_());
            });
        }
    }
}
