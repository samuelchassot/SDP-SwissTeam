package ch.epfl.swissteam.services;

import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

/**
 * Fragment for displaying users in form of a list
 *
 * @author simonwicky
 */
public class ServicesFragment extends Fragment {

    private RecyclerView.Adapter mAdapter_;
    private ArrayList<User> users = new ArrayList<>();
    private ArrayList<String> keywords_;
    private Categories currentCategory_;
    private boolean sortByRating = true;

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
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_services, container, false);

        // Toolbar
        Toolbar toolbar = getActivity().findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.toolbar_services);

        keywords_ = new ArrayList<>();

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
                currentCategory_ = (Categories) adapterView.getItemAtPosition(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        EditText keywordsInput = (EditText) view.findViewById(R.id.edittext_services_keywordsinput);
        keywordsInput.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                String inKeywords = s.toString();
                Log.i("KEYWORDS_LENGTH", "length = " + s.length());
                keywords_ = new ArrayList<>(Arrays.asList(inKeywords.split(" ")));
                Log.i("LIST_LENGTH", "list length = " + keywords_.size());
                keywords_.remove("");
                Log.i("LIST_LENGTH", "list length = " + keywords_.size());
            }
        });

        Button searchButton = (Button) view.findViewById(R.id.button_services_search);
        searchButton.setOnClickListener(v ->{
            Log.i("KEYWORDS", "list of keywords is empty ? " + keywords_.isEmpty());
            initDataSet(currentCategory_, keywords_);
        });

        String[] sortType = {getResources().getString(R.string.servicesfragment_proximity), getResources().getString(R.string.servicesfragment_rating)};
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, sortType);
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The drop down view
        ((Spinner)view.findViewById(R.id.spinner_services_sorttype)).setAdapter(spinnerArrayAdapter);
        ((Spinner)view.findViewById(R.id.spinner_services_sorttype)).setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    sortByRating = i != 0;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        initDataSet(Categories.ALL, keywords_);

        return view;
    }

    private void initDataSet(Categories category, ArrayList<String> keywords) {
        ActivityManager.hideKeyboard(this.getActivity());
        View view = getView();
        if (category == Categories.ALL) {
            DBUtility.get().getAllUsers((usersdb -> {
                users.clear();
                mAdapter_.notifyDataSetChanged();
                for (User u : usersdb) {
                    if (userContainsKeywords(u, keywords, category)) {
                        if (!u.getGoogleId_().equals(GoogleSignInSingleton.get().getClientUniqueID())) {
                            //don't add current user to the list
                            users.add(u);
                        }
                    }
                }
                serviceProblemTextUpdate(view, users.isEmpty());
                sortUserList();
            }));
        } else {
            DBUtility.get().getUsersFromCategory(category, (googleIds) -> {
                users.clear();
                mAdapter_.notifyDataSetChanged();
                serviceProblemTextUpdate(view, googleIds.isEmpty());
                for (String googleId : googleIds) {
                    DBUtility.get().getUser(googleId, user -> {
                        if (user != null && !users.contains(user) && !user.getGoogleId_().equals(GoogleSignInSingleton.get().getClientUniqueID()) &&
                                userContainsKeywords(user, keywords, category)) {
                            users.add(user);
                            sortUserList();
                        }
                    });
                }
            });
        }
    }

    private void sortUserList() {
        if(sortByRating){
            Collections.sort(users, (a, b) -> b.getRating_() - a.getRating_());
        }
        else {
            Collections.sort(users, this::compareUsersUsingDistanceWithRef);
        }
        mAdapter_.notifyDataSetChanged();
    }

    private void serviceProblemTextUpdate(View view, boolean empty) {
        if (view != null) {
            if (empty) {
                view.findViewById(R.id.services_problem_text).setVisibility(View.VISIBLE);
            } else {
                view.findViewById(R.id.services_problem_text).setVisibility(View.INVISIBLE);
            }
        }
    }

    /**
     * return true if keywords of the user contains at least one of the list kw
     * for the given category
     * the list of keywords kw must contains ONLY LOWERCASE words
     * @param u the user
     * @param kw list of keywords to search for
     * @param cat the category for which want to search the keywords
     * @return boolean user has or not
     */
    private boolean userContainsKeywords(User u, ArrayList<String> kw, Categories cat){
        ArrayList<String> listForCat;
        if(cat.compareTo(Categories.ALL) == 0){
            listForCat = new ArrayList<>();
               for(Categories c : Categories.values()){
                   listForCat.addAll(u.getKeyWords(c));
               }

        }else{
            listForCat = u.getKeyWords(cat);
        }
        if(kw.isEmpty()){
            return true;
        }
        for (String k : kw){
            if(listForCat.contains(k.toLowerCase())){
                return true;
            }
        }
        return false;
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
