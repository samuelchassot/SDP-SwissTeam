package ch.epfl.swissteam.services;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
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

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private ArrayList<User> users = new ArrayList<>();


    public ServicesFragment() {
        // Required empty public constructor
    }

    /**
     * Create a new instance of Services fragment
     * @return the created instance
     */
    public static ServicesFragment newInstance() {
        ServicesFragment fragment = new ServicesFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initDataSet(Categories.ALL);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_services, container, false);
        mRecyclerView = view.findViewById(R.id.services_recycler);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mAdapter = new UserAdapter(users);
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
        users.clear();
        if (category == Categories.ALL){
            DBUtility.get().getAllUsers((usersdb ->{
                users.addAll(usersdb);
                mAdapter.notifyDataSetChanged();
            }));
        }
        DBUtility.get().getUsersFromCategory(category, (googleIds) -> {
            for (String googleId : googleIds){
                DBUtility.get().getUser(googleId, user->{
                    users.add(user);
                    mAdapter.notifyDataSetChanged();
                });
            }
        });


    }





}
