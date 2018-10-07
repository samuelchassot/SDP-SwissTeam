package ch.epfl.swissteam.services;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Arrays;


public class ServicesFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    public ServicesFragment() {
        // Required empty public constructor
    }

    public static ServicesFragment newInstance() {
        ServicesFragment fragment = new ServicesFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_services, container, false);
        User simon = new User("swicky","Simon", "Wicky", "Je suis un étudiant de l'epfl", new ArrayList<>(Arrays.asList("IC", "Maths", "Jardinage")));
        User samuel = new User("schassot", "Samuel ", "Chassot", "Salut, je m'appelle Samuel", new ArrayList<>(Arrays.asList("IC", "Allemand", "Anglais")));
        User sjobs = new User("sjobs", "Steve ", "Jobs", "You know who I am", new ArrayList<>(Arrays.asList("IC", "Anglais", "Jardinage", "Business")));
        ArrayList<User> users = new ArrayList<>();
        users.add(simon);
        users.add(samuel);
        users.add(sjobs);
        mRecyclerView = view.findViewById(R.id.services_recycler);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mAdapter = new UserAdapter(users);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(mLayoutManager);
        return view;
    }


}
