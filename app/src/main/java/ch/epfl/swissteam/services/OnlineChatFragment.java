package ch.epfl.swissteam.services;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;


public class OnlineChatFragment extends Fragment {

    private ProfileDisplayFragment.OnFragmentInteractionListener mListener;

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

        View thatView = inflater.inflate(R.layout.fragment_online_chat, container, false);
        Button nextButt = thatView.findViewById(R.id.fragment_online_chat_confirme_button);
        nextButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createChatWithInputUserId(thatView);
            }
        });
        displayChats();
        
        // Inflate the layout for this fragment
        return thatView;
    }

    private void displayChats(){
    }

    public void createChatWithInputUserId(View view){
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
    }

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
