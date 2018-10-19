package ch.epfl.swissteam.services;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * A fragment to display spontaneous posts on the main page
 *
 * @author Julie Giunta
 */
public class HomeFragment extends Fragment implements View.OnClickListener{

    private SwipeRefreshLayout swipeRefreshLayout_;
    private RecyclerView.Adapter adapter_;
    private List<Post> posts_ = new ArrayList<>();

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Creates a new <code>HomeFragment</code>.
     * @return new instance of <code>HomeFragment</code>
     */
    public static HomeFragment newInstance() {
        return new HomeFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Toolbar toolbar = getActivity().findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.toolbar_home);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View frag = inflater.inflate(R.layout.fragment_home, container, false);
        (frag.findViewById(R.id.button_homefragment_refresh)).setOnClickListener(this);

        swipeRefreshLayout_ = frag.findViewById(R.id.swiperefresh_homefragment_refresh);
        swipeRefreshLayout_.setOnRefreshListener(() -> refresh());
        swipeRefreshLayout_.setColorSchemeResources(R.color.colorAccent);

        //setup recyclerview for posts
        RecyclerView mRecyclerView_ = frag.findViewById(R.id.recyclerview_homefragment_posts);

        if (mRecyclerView_ != null) {
            mRecyclerView_.setLayoutManager(new LinearLayoutManager(this.getContext()));

            adapter_ = new PostAdapter(posts_);
            mRecyclerView_.setAdapter(adapter_);
        }

        refresh();
        return frag;
    }

    @Override
    public void onClick(View v) {
        refresh();
    }

    /**
     * Refresh the feed of post shown on the main board
     */
    private void refresh(){
        DBUtility.get().getPostsFeed(value -> {
            posts_.clear();
            posts_.addAll(value);
            adapter_.notifyDataSetChanged();
            swipeRefreshLayout_.setRefreshing(false);
        });

    }

    @Override
    public void onResume() {
        super.onResume();
        refresh();
    }
}
