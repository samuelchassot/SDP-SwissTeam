package ch.epfl.swissteam.services.view.fragments;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import ch.epfl.swissteam.services.R;
import ch.epfl.swissteam.services.models.Post;
import ch.epfl.swissteam.services.providers.DBUtility;
import ch.epfl.swissteam.services.providers.GoogleSignInSingleton;
import ch.epfl.swissteam.services.view.activities.MainActivity;
import ch.epfl.swissteam.services.view.activities.MyPostEditActivity;
import ch.epfl.swissteam.services.view.builders.MyPostAdapter;

/**
 * MyPostsFragment, a fragment that display to the currently logged in user
 * his posts. Depending on his interaction with his posts, sends him to
 * {@link MyPostEditActivity}.
 *
 * @author Julie Giunta
 */
public class MyPostsFragment extends Fragment {

    private String clientUniqueID_;
    private RecyclerView.Adapter mAdapter_;
    private List<Post> mPosts_ = new ArrayList<>();

    public MyPostsFragment() {
        // Required empty public constructor
    }

    /**
     * Creates a new {@link MyPostsFragment}.
     *
     * @return new instance of <code>MyPostsFragment</code>
     */
    public static MyPostsFragment newInstance() {
        return new MyPostsFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View frag = inflater.inflate(R.layout.fragment_my_posts, container, false);

        // Toolbar
        Toolbar toolbar = getActivity().findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.toolbar_my_posts);

        clientUniqueID_ = GoogleSignInSingleton.get().getClientUniqueID();
        loadAndShowPostsFromUser();

        //setup recyclerview for posts
        RecyclerView mRecyclerView_ = frag.findViewById(R.id.recyclerview_mypostsfragment);

        if (mRecyclerView_ != null) {
            LinearLayoutManager layoutManager = new LinearLayoutManager(this.getContext());
            mRecyclerView_.setLayoutManager(layoutManager);

            mAdapter_ = new MyPostAdapter(mPosts_);
            mRecyclerView_.setAdapter(mAdapter_);
        }

        //set the button to add new post
        FloatingActionButton addPost = frag.findViewById(R.id.floatingbutton_addpost);
        addPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity) getActivity()).showCreatePostFragment();
            }
        });

        return frag;
    }

    /**
     * Load the posts from a user and display them in the recycler view
     */
    private void loadAndShowPostsFromUser() {
        DBUtility.get().getUsersPosts(clientUniqueID_, (posts) -> {
            mPosts_.clear();
            Date today = Calendar.getInstance().getTime();
            for (Post p : posts) {
                if(!p.deleteIfTooOld(today)){
                    mPosts_.add(p);
                }
            }
            mAdapter_.notifyDataSetChanged();
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        loadAndShowPostsFromUser();
    }

}

