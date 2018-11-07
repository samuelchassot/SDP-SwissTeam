package ch.epfl.swissteam.services;

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
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ProfileDisplayFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ProfileDisplayFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileDisplayFragment extends Fragment {

    private OnFragmentInteractionListener mListener;
    private String clientUniqueID_;
    private RecyclerView mRecyclerView_;
    private LinearLayoutManager mLayoutManager_;
    private CapabilitiesAdapter mAdapter_;
    private List<Categories> mCapabilities_ = new ArrayList<Categories>();

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
        ProfileDisplayFragment fragment = new ProfileDisplayFragment();
        return fragment;
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
            Intent intent = new Intent(getActivity(), ProfileSettings.class);

            @Override
            public void onClick(View v) {
                startActivity(intent);

            }
        });

        clientUniqueID_ = GoogleSignInSingleton.get().getClientUniqueID();

        if (clientUniqueID_ != null && !clientUniqueID_.equals("")) {
            loadAndShowUser(clientUniqueID_);

            //setup recyclerview for capabilities
            mRecyclerView_ = (RecyclerView) thisView.findViewById(R.id.recyclerview_profiledisplay_categories);

            if (mRecyclerView_ != null) {
                mRecyclerView_.setHasFixedSize(true);

                mLayoutManager_ = new LinearLayoutManager(this.getContext());
                mRecyclerView_.setLayoutManager(mLayoutManager_);

                mAdapter_ = new CapabilitiesAdapter(mCapabilities_);
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
                TextView nameView = (TextView) view.findViewById(R.id.textview_profiledisplay_name);
                nameView.setText(user.getName_());

                TextView emailView = (TextView) view.findViewById(R.id.textview_profiledisplay_email);
                emailView.setText(user.getEmail_());

                TextView descrView = (TextView) view.findViewById(R.id.textview_profiledisplay_description);
                descrView.setText(user.getDescription_());

                TextView ratingView = (TextView) view.findViewById(R.id.textview_profiledisplay_rating);
                ratingView.setText(Integer.toString(user.getRating_()));
            }

            Picasso.get().load(user.getImageUrl_()).into((ImageView) getView().findViewById(R.id.imageview_profiledisplay_picture));

            //for the recyclerview
            mCapabilities_.clear();
            mCapabilities_.addAll(user.getCategories_());
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

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }


}
