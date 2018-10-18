package ch.epfl.swissteam.services;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import java.util.ArrayList;

/**
 * Fragment for displaying users in form of a list
 *
 * @author simonwicky
 */
public class ServicesFragment extends Fragment {

    private RecyclerView.Adapter mAdapter;
    private ArrayList<User> users = new ArrayList<>();


    public ServicesFragment() {
        // Required empty public constructor
    }

    /**
     * Create a new instance of Services fragment
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
        View view =  inflater.inflate(R.layout.fragment_services, container, false);
        RecyclerView mRecyclerView = view.findViewById(R.id.services_recycler);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        mAdapter = new UserAdapter(users, getContext());
        mRecyclerView.setAdapter(mAdapter);
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



    private void initDataSet(Categories category){
        View view = getView();
        if (category == Categories.ALL){
            DBUtility.get().getAllUsers((usersdb ->{
                users.clear();
                users.addAll(usersdb);
                mAdapter.notifyDataSetChanged();
                services_problem_text_udpate(view, users.isEmpty());
            }));
        } else {
            DBUtility.get().getUsersFromCategory(category, (googleIds) -> {
                users.clear();
                services_problem_text_udpate(view, googleIds.isEmpty());

                    for (String googleId : googleIds) {
                        DBUtility.get().getUser(googleId, user -> {
                            if (!users.contains(user)) {
                                users.add(user);
                                mAdapter.notifyDataSetChanged();
                            }
                        });
                    }
            });
        }

    }

    private void services_problem_text_udpate(View view, boolean empty){
        if (view != null) {
            if (empty) {
                view.findViewById(R.id.services_problem_text).setVisibility(View.VISIBLE);
            } else {
                view.findViewById(R.id.services_problem_text).setVisibility(View.INVISIBLE);
            }
        }
    }





}
