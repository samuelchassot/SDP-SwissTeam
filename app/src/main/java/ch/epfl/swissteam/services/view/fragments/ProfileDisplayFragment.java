package ch.epfl.swissteam.services.view.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ch.epfl.swissteam.services.R;
import ch.epfl.swissteam.services.models.Categories;
import ch.epfl.swissteam.services.providers.DBUtility;
import ch.epfl.swissteam.services.providers.GoogleSignInSingleton;
import ch.epfl.swissteam.services.view.activities.ProfileSettingsActivity;
import ch.epfl.swissteam.services.view.builders.CategoriesAdapterProfileActivity;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * Use the {@link ProfileDisplayFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileDisplayFragment extends Fragment {

    private String clientUniqueID_;
    private CategoriesAdapterProfileActivity mAdapter_;
    private List<Categories> mCapabilities_ = new ArrayList<Categories>();
    private Map<String, List<String>> mKeyWords_ = new HashMap<>();


    public ProfileDisplayFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment ProfileDisplayFragment.
     */
    public static ProfileDisplayFragment newInstance() {
        return new ProfileDisplayFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View thisView = inflater.inflate(R.layout.fragment_profile_display, container, false);

        // Toolbar
        Toolbar toolbar = getActivity().findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.toolbar_profile);

        Button button = (Button) thisView.findViewById(R.id.button_profiledisplay_modify);
        button.setOnClickListener(new View.OnClickListener() {
            Intent intent = new Intent(getActivity(), ProfileSettingsActivity.class);

            @Override
            public void onClick(View v) {
                startActivity(intent);

            }
        });

        clientUniqueID_ = GoogleSignInSingleton.get().getClientUniqueID();

        if (clientUniqueID_ != null && !clientUniqueID_.equals("")) {
            loadAndShowUser(clientUniqueID_);

            //setup recyclerview for capabilities
            RecyclerView mRecyclerView_ = (RecyclerView) thisView.findViewById(R.id.recyclerview_profiledisplay_categories);

            if (mRecyclerView_ != null) {
                mRecyclerView_.setHasFixedSize(true);

                LinearLayoutManager mLayoutManager_ = new LinearLayoutManager(this.getContext());
                mRecyclerView_.setLayoutManager(mLayoutManager_);

                mAdapter_ = new CategoriesAdapterProfileActivity(mCapabilities_, mKeyWords_);
                mRecyclerView_.setAdapter(mAdapter_);
            }
        }

        // Inflate the layout for this fragment
        return thisView;
    }

    private void loadAndShowUser(String clientUniqueID) {
        //for now we use the username
        View view = getView();
        DBUtility.get().getUser(clientUniqueID, (user) -> {
            if (view != null) {
                TextView nameView =  view.findViewById(R.id.textview_profiledisplay_name);
                nameView.setText(user.getName_());

                TextView emailView = view.findViewById(R.id.textview_profiledisplay_email);
                emailView.setText(user.getEmail_());

                TextView descrView = view.findViewById(R.id.textview_profiledisplay_description);
                descrView.setText(user.getDescription_());

                TextView ratingView = view.findViewById(R.id.textview_profiledisplay_rating);
                ratingView.setText(Integer.toString(user.getRating_()));
                Picasso.get().load(user.getImageUrl_()).into((ImageView) view.findViewById(R.id.imageview_profiledisplay_picture));
            }


            //for the recyclerview
            mCapabilities_.clear();
            mCapabilities_.addAll(user.getCategories_());
            mKeyWords_.clear();
            for(Categories c : user.getCategories_()){
                mKeyWords_.put(c.toString(), user.getKeyWords(c));
            }
            if (mAdapter_ != null) {
                mAdapter_.notifyDataSetChanged();
            }

        });
    }

    @Override
    public void onResume() {
        super.onResume();
        loadAndShowUser(clientUniqueID_);

    }


}
