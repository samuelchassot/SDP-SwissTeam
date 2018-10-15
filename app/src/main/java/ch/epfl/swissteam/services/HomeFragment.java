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
        return new HomeFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        postsToStringList = new ArrayList<>();
        postsToStringList.add(getContext().getString(R.string.homefragment_asktorefresh));
        adapter = new ArrayAdapter<>(
                this.getContext(), android.R.layout.simple_list_item_1, postsToStringList);

        DBUtility.get().getPostsFeed(value -> {
            postsList.clear();
            postsList.addAll(value);
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View frag = inflater.inflate(R.layout.fragment_home, container, false);
        (frag.findViewById(R.id.button_homefragment_refresh)).setOnClickListener(this);

        swipeRefreshLayout = frag.findViewById(R.id.swiperefresh_homefragment_refresh);
        swipeRefreshLayout.setOnRefreshListener(() -> refresh());

        swipePostsList = frag.findViewById(R.id.listview_homefragment_postslist);

        swipePostsList.setAdapter(adapter);
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
        if(postsList.isEmpty()){
            postsToStringList.add(getContext().getString(R.string.homefragment_noposts));
        }else{
            for(Post p : postsList){
                postsToStringList.add(p.getTitle_() + "\n" + p.getBody_());
            }
        }
        swipePostsList.invalidateViews();
        swipeRefreshLayout.setRefreshing(false);
    }
}
