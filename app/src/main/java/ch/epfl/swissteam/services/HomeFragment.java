package ch.epfl.swissteam.services;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;
import java.util.List;

/**
 * A fragment to display spontaneous posts on the main page
 *
 * @author Julie Giunta
 */
public class HomeFragment extends Fragment implements View.OnClickListener{

    private SwipeRefreshLayout swipeRefreshLayout;
    private ListView swipePostsList;
    private ListAdapter adapter;
    private List<String> postsToStringList;
    private List<Post> postsList = new ArrayList<>();

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Creates a new <code>HomeFragment</code>.
     * @return new instance of <code>HomeFragment</code>
     */
    public static HomeFragment newInstance() {
        HomeFragment fragment = new HomeFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        DBUtility.get().getPostsFeed(new MyCallBack<ArrayList<Post>>() {
            @Override
            public void onCallBack(ArrayList<Post> value) {
                postsList.addAll(value);
            }
        });

        swipeRefreshLayout = getView().findViewById(R.id.swiperefresh_homefragment_refresh);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refresh();
            }
        });

        swipePostsList = getView().findViewById(R.id.listview_homefragment_postslist);

        postsToStringList = new ArrayList<>();
        adapter = new ArrayAdapter<>(
                this.getContext(), android.R.layout.simple_list_item_1, postsToStringList);
        swipePostsList.setAdapter(adapter);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View frag = inflater.inflate(R.layout.fragment_home, container, false);
        (frag.findViewById(R.id.button_homefragment_refresh)).setOnClickListener(this);
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
        postsToStringList.clear();
        for(Post p : postsList){
            postsToStringList.add(p.getTitle() + "\n" + p.getBody());
        }
        swipePostsList.invalidateViews();
        swipeRefreshLayout.setRefreshing(false);
    }

}
