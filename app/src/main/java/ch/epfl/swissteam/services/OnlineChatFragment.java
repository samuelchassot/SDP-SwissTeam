package ch.epfl.swissteam.services;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

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
                createChatWithInputUserId(view);
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
        User mUser = new User("123t18", "Mami","gateau", "mg@ggmail.com", "description", new ArrayList<>());
        User pUser = new User(eT.getText().toString(), "Papi","gateau", "pg@ggmail.com", "description", new ArrayList<>());

        assert(mUser != null);
        assert(pUser != null);


        ChatRelation cR = new ChatRelation(mUser, pUser);
        cR.addToDB(DBUtility.get().getDb_());
    }
}
