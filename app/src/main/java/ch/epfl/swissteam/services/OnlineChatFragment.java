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

    /*public void createChatWithInputUserId(View view){
        EditText eT = view.findViewById(R.id.fragment_online_chat_text);

        DBUtility dbU = DBUtility.get();
        User mUser = dbU.getCurrentUser_();
        try{
            dbU.getUser(eT.getText().toString(), new MyCallBack<User>(){
                @Override
                public void onCallBack(User value) {
                    openChatWith(DBUtility.get().getCurrentUser_(), value);
                }
            } );
        }
        catch (NullPointerException e) {
            chatErrorAlertDialog(getResources().getString(R.string.general_could_not_find_this_user_in_db));
        }

    }

    private void openChatWith(User mUser, User other){
        DatabaseReference db = DBUtility.get().getDb_();
        ChatRelation cR;

        if(mUser != null && other != null){
            cR = mUser.relationExists(other);

            if(cR == null){
                cR = new ChatRelation(mUser, other);
                cR.addToDB(db);
                mUser.addChatRelation(cR, db);
                other.addChatRelation(cR, db);
            }

            Intent chatIntent = new Intent(getActivity(), ChatRoom.class);
            chatIntent.putExtra(ChatRelation.RELATION_ID_TEXT, cR.getId_());
            startActivity(chatIntent);
        }

        if(mUser == null){
            chatErrorAlertDialog(getResources().getString(R.string.general_could_not_find_you_in_db));
        }

        if(other == null){
            chatErrorAlertDialog(getResources().getString(R.string.general_could_not_find_this_user_in_db));

        }
    }*/

    private void chatErrorAlertDialog(String message){
        AlertDialog aD = new AlertDialog.Builder(getActivity()).create();
        aD.setTitle(getResources().getString(R.string.general_error));
        aD.setMessage(message);
        aD.setButton(AlertDialog.BUTTON_NEUTRAL, getResources().getString(R.string.general_ok), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        aD.show();
    }

}
