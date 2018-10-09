package ch.epfl.swissteam.services;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
        initDataSet();

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
        return view;
    }


    private void initDataSet(){
        users.clear();
        DBUtility.get().getAllUsers(new MyCallBack<ArrayList<User>>() {
            @Override
            public void onCallBack(ArrayList<User> value) {
                users.addAll(value);
                mAdapter.notifyDataSetChanged();
            }
        });


    }


}
