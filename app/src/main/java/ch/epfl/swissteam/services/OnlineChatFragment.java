package ch.epfl.swissteam.services;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

/**
 * Fragment to display.
 *
 * @author Sébastien Gachoud
 */
public class OnlineChatFragment extends Fragment {

    private ProfileDisplayFragment.OnFragmentInteractionListener mListener;
    private ChatRelationAdapter adapter_;

    public OnlineChatFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment OnlineChatFragment.
     */
    public static OnlineChatFragment newInstance() {
        OnlineChatFragment fragment = new OnlineChatFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Toolbar
        Toolbar toolbar = getActivity().findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.toolbar_chats);

        // Inflate the layout for this fragment
        View thatView = inflater.inflate(R.layout.fragment_online_chat, container, false);
        DBUtility.get().getUser(GoogleSignInSingleton.get().getClientUniqueID(), new DBCallBack<User>() {
            @Override
            public void onCallBack(User user) {
                if (user != null) {
                    displayChats(thatView, user);
                }
            }
        });

        return thatView;
    }

    //TODO find out why this does not work anymore
    private void displayChats(View view, User user) {

        ArrayList<ChatRelation> relations = user.getChatRelations_();
        RecyclerView mRecyclerView_ = view.findViewById(R.id.fragment_online_chats_recycler_view);

        if (mRecyclerView_ != null) {
            mRecyclerView_.setLayoutManager(new LinearLayoutManager(this.getContext()));

            adapter_ = new ChatRelationAdapter(relations, GoogleSignInSingleton.get().getClientUniqueID());
            mRecyclerView_.setAdapter(adapter_);
        }
    }
}
