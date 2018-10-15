package ch.epfl.swissteam.services;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class MyPostsFragment extends Fragment implements View.OnClickListener{

    public MyPostsFragment() {
        // Required empty public constructor
    }

    /**
     * Creates a new {@link MyPostsFragment}.
     * @return new instance of <code>MyPostsFragment</code>
     */
    public static MyPostsFragment newInstance() {
        MyPostsFragment fragment = new MyPostsFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View frag = inflater.inflate(R.layout.fragment_my_posts, container, false);
        return frag;
    }
}
