package ch.epfl.swissteam.services;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;


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

        // Inflate the layout for this fragment
        View thatView = inflater.inflate(R.layout.fragment_online_chat, container, false);
        DBUtility.get().getUser(GoogleSignInSingleton.get().getClientUniqueID(),new MyCallBack<User>() {
            @Override
            public void onCallBack(User user) {
                if(user != null){
                    displayChats(thatView, user);
                }
            }
        });

        return thatView;
    }

    private void displayChats(View view, User user){

        ArrayList<ChatRelation> relations = user.getChatRelations_();
        RecyclerView mRecyclerView_ = view.findViewById(R.id.fragment_online_chats_recycler_view);

        if (mRecyclerView_ != null) {
            mRecyclerView_.setLayoutManager(new LinearLayoutManager(this.getContext()));

            adapter_ = new ChatRelationAdapter(relations);
            mRecyclerView_.setAdapter(adapter_);
        }
    }
}
