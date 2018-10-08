package ch.epfl.swissteam.services;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Date;
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

        swipeRefreshLayout = getView().findViewById(R.id.swiperefresh_homefragment_refresh);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refresh();
            }
        });

        swipePostsList = getView().findViewById(R.id.listview_homefragment_postslist);

        postsToStringList = new ArrayList<>();
        adapter = new ArrayAdapter<String>(
                this.getContext(), android.R.layout.simple_list_item_1, postsToStringList);
        swipePostsList.setAdapter(adapter);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View frag = inflater.inflate(R.layout.fragment_home, container, false);
        ((Button)frag.findViewById(R.id.button_homefragment_refresh)).setOnClickListener(this);
        return frag;
    }

    @Override
    public void onClick(View v) {
        refresh();
    }

    private void refresh(){
        DatabaseReference mDataBase = FirebaseDatabase.getInstance().getReference();

    }


//    @Override
//    public void onViewCreated(View view, Bundle savedInstanceState) {
//        super.onViewCreated(view, savedInstanceState);
//
//        //Create an ArrayAdapter to contain the data for the ListView. Each item in the ListView
//        //uses the system-defined simple_list_item_1 layout that contains one TextView.
//        ListAdapter adapter = new ArrayAdapter<String>(
//                getActivity(),
//                android.R.layout.simple_list_item_1,
//                android.R.id.text1,
//                Cheeses.randomList(LIST_ITEM_COUNT));
//
//        // Set the adapter between the ListView and its backing data.
//        setListAdapter(adapter);
//        setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//
//            @Override
//            public void onRefresh() {
//                Log.i(LOG_TAG, "onRefresh called from SwipeRefreshLayout");
//                initiateRefresh();
//            }
//        });
//    }
//
//    /**
//     * By abstracting the refresh process to a single method, the app allows both the
//     * SwipeGestureLayout onRefresh() method and the Refresh action item to refresh the content.
//     */
//
//    private void initiateRefresh() {
//
//        Log.i(LOG_TAG, "initiateRefresh");
//
//        //TODO retrieve posts
//        new SpontaneousPostLoad().execute();
//    }
//
//    /**
//     * When the AsyncTask finishes, it calls onRefreshComplete(), which updates the data in the
//     * ListAdapter and turns off the progress bar.
//     */
//
//    private void onRefreshComplete(List<String> result) {
//
//        Log.i(LOG_TAG, "onRefreshComplete");
//        // Remove all items from the ListAdapter, and then replace them with the new items
//
//        ArrayAdapter<String> adapter = (ArrayAdapter<String>) getListAdapter();
//        adapter.clear();
//
//        //TODO add each post to adapter
//        for (String post : result) {
//            adapter.add(post);
//        }
//
//        // Stop the refreshing indicator
//        setRefreshing(false);
//    }
//
//
//    private class SpontaneousPostLoad extends AsyncTask<Void, Void, List<String>> {
//
//        @Override
//        protected List<String> load() {
//
//            //TODO return list of posts
//            return Cheeses.randomList(LIST_ITEM_COUNT);
//
//        }
//
//        @Override
//        protected void onPostExecute(List<String> result) {
//            super.onPostExecute(result);
//
//            // Tell the Fragment that the refresh has completed
//            onRefreshComplete(result);
//        }
//    }
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//        View frag = inflater.inflate(R.layout.fragment_home, container, false);
//        ((Button)frag.findViewById(R.id.button_homefragment_refresh)).setOnClickListener(this);
//        return frag;
//
//        // Create the list fragment's content view by calling the super method
//        final View listFragmentView = super.onCreateView(inflater, container, savedInstanceState);
//
//        // Now create a SwipeRefreshLayout to wrap the fragment's content view
//        mSwipeRefreshLayout = new ListFragmentSwipeRefreshLayout(container.getContext());
//
//
//
//        // Add the list fragment's content view to the SwipeRefreshLayout, making sure that it fills
//        // the SwipeRefreshLayout
//        mSwipeRefreshLayout.addView(listFragmentView,
//                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
//
//        // Make sure that the SwipeRefreshLayout will fill the fragment
//        mSwipeRefreshLayout.setLayoutParams(
//                new ViewGroup.LayoutParams(
//                        ViewGroup.LayoutParams.MATCH_PARENT,
//                        ViewGroup.LayoutParams.MATCH_PARENT));
//
//        // Now return the SwipeRefreshLayout as this fragment's content view
//        return mSwipeRefreshLayout;
//    }

}
