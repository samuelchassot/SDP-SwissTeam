package ch.epfl.swissteam.services;

import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Fragment for displaying users in form of a list
 *
 * @author simonwicky
 */
public class ServicesFragment extends Fragment {

    private RecyclerView.Adapter mAdapter_;
    private ArrayList<User> users = new ArrayList<>();
    private Location currentUserLocation_;


    public ServicesFragment() {
        // Required empty public constructor
    }

    /**
     * Create a new instance of Services fragment
     *
     * @return the created instance
     */
    public static ServicesFragment newInstance() {
        return new ServicesFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Toolbar toolbar = getActivity().findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.toolbar_services);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_services, container, false);
        RecyclerView mRecyclerView = view.findViewById(R.id.services_recycler);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        mAdapter_ = new UserAdapter(users, getContext());
        mRecyclerView.setAdapter(mAdapter_);
        mRecyclerView.setLayoutManager(mLayoutManager);

        Spinner filterSpinner = (Spinner) view.findViewById(R.id.services_spinner);
        ArrayAdapter<Categories> filterSpinnerAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, Categories.values());
        filterSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        filterSpinner.setAdapter(filterSpinnerAdapter);

        filterSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                initDataSet((Categories) adapterView.getItemAtPosition(i));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        return view;
    }

    private void initDataSet(Categories category) {
        View view = getView();
        if (category == Categories.ALL) {
            DBUtility.get().getAllUsers((usersdb -> {
                users.clear();
                for (User u : usersdb) {
                    if(! u.getGoogleId_().equals(GoogleSignInSingleton.get().getClientUniqueID())){
                        //don't add current user to the list
                        users.add(u);
                    }
                }
                Collections.sort(users, this::compareUsersUsingDistanceWithRef);
                mAdapter_.notifyDataSetChanged();
                services_problem_text_udpate(view, users.isEmpty());
            }));
        } else {
            DBUtility.get().getUsersFromCategory(category, (googleIds) -> {
                users.clear();
                services_problem_text_udpate(view, googleIds.isEmpty());
                mAdapter_.notifyDataSetChanged();

                for (String googleId : googleIds) {
                    DBUtility.get().getUser(googleId, user -> {
                        if (user != null && !users.contains(user) && !user.getGoogleId_().equals(GoogleSignInSingleton.get().getClientUniqueID())) {
                            users.add(user);
                            Collections.sort(users, this::compareUsersUsingDistanceWithRef);
                            mAdapter_.notifyDataSetChanged();
                        }
                    });
                }
            });
        }

    }

    private void services_problem_text_udpate(View view, boolean empty) {
        if (view != null) {
            if (empty) {
                view.findViewById(R.id.services_problem_text).setVisibility(View.VISIBLE);
            } else {
                view.findViewById(R.id.services_problem_text).setVisibility(View.INVISIBLE);
            }
        }
    }

    private int compareUsersUsingDistanceWithRef(User u1, User u2){
        Location ref = LocationManager.get().getCurrentLocation_();
        int result = 0;
        Location u1Location = new Location("");
        u1Location.setLatitude(u1.getLatitude_());
        u1Location.setLongitude(u1.getLongitude_());

        Log.i("U1Latitude", u1.getLatitude_() + "");

        Location u2Location = new Location("");
        u2Location.setLatitude(u2.getLatitude_());
        u2Location.setLongitude(u2.getLongitude_());

        if(ref != null) {
            result = (int) u1Location.distanceTo(ref) - (int) u2Location.distanceTo(ref);
            Log.i("Ref", "not null");
        }
        return result;
    }

}
