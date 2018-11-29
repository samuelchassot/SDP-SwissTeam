package ch.epfl.swissteam.services;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * A fragment that display for the user a list of the post he is involved in.
 *
 * @author Ghali Chraïbi
 */
public class TodoListFragment extends Fragment {


    public TodoListFragment() {
        // Required empty public constructor
    }

    /**
     * Creates a new {@link TodoListFragment}.
     *
     * @return new instance of <code>TodoListFragment</code>
     */
    public static TodoListFragment newInstance() {
        return new TodoListFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View frag = inflater.inflate(R.layout.fragment_todo_list, container, false);

        return frag;
    }
}
