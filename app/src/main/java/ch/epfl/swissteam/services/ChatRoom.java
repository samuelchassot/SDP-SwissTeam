package ch.epfl.swissteam.services;

import android.os.Bundle;
import android.app.Activity;
import android.support.design.widget.TextInputEditText;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
import android.view.Gravity;
import android.view.View;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseListAdapter;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


/**
 * This activity is a chat room, it display messages and allow to write and send messages
 *
 * @author Sébastien Gachoud
 */
public class ChatRoom extends Activity {

    FirebaseRecyclerAdapter<ChatMessage, MessageHolder> adapter_;
    DatabaseReference dataBase_;
    String currentRelationId_;
    User mUser_;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_room);
        dataBase_ = DBUtility.get().getDb_();
        setCurrentRelationId_(getIntent().getExtras().getString(ChatRelation.RELATION_ID_TEXT, null));

        DBUtility.get().getUser(GoogleSignInSingleton.get().getClientUniqueID(), new MyCallBack<User>(){
            @Override
            public void onCallBack(User mUser) {
                if(mUser == null){
                    toastUser(getResources().getString(R.string.general_could_not_find_you_in_db));
                    return;
                }
                mUser_ = mUser;
                String contactId = getIntent().getExtras().getString(NewProfileDetails.GOOGLE_ID_TAG, null);

                if(currentRelationId_ == null){
                    ChatRelation cR = mUser_.relationExists(contactId);
                    if(cR == null) {
                        newRelationWith(contactId);
                    }
                    else {
                        setCurrentRelationId_(cR.getId_());
                        displayMessages();
                    }
                }
                else {
                    displayMessages();
                }
            }
        } );

        //If nothing works to establish the chat
        if(currentRelationId_ == null) {
            findViewById(R.id.message_send_button).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    toastUser(getResources().getString(R.string.general_could_not_establish_relation));
                }
            });
        }
    }

    private void setCurrentRelationId_(String relationId){
        currentRelationId_ = relationId;
    }
    /**
     * display messages retrieved form the database
     */
    private void displayMessages(){
        RecyclerView chatRoom = findViewById(R.id.recycler_view_message);
        LinearLayoutManager llm = new LinearLayoutManager(this);

        chatRoom.setHasFixedSize(true);
        chatRoom.setLayoutManager(llm);

        adapter_ = new FirebaseRecyclerAdapter<ChatMessage, MessageHolder>
                (ChatMessage.class, R.layout.chat_message_layout, MessageHolder.class, dataBase_.child(DBUtility.CHATS).child(currentRelationId_))
        {
            @Override
            protected void populateViewHolder(MessageHolder viewHolder, ChatMessage message, int position){
                viewHolder.messageText_.setText(message.getText_());
                viewHolder.timeUserText_.setText(DateFormat.format("dd-mm-yyyy (hh:mm:ss)", message.getTime_()) +
                        message.getUser_());
            }
        };
        chatRoom.setAdapter(adapter_);
    }

    /**
     * Send message to the database
     * @param view
     */
    public void sendMessage(View view){
        TextInputEditText textInput = findViewById(R.id.message_input);
        String message = textInput.getText().toString();
        if(mUser_ == null){
            toastUser(getResources().getString(R.string.general_could_not_find_you_in_db));
            return;
        }
        if(message.isEmpty()){
            return;
        }
        ChatMessage chatMessage = new ChatMessage(message, mUser_.getName_(), mUser_.getGoogleId_(), currentRelationId_);
        chatMessage.addToDB(dataBase_);

        textInput.getText().clear();
    }

    private void newRelationWith(String contactId ){

        DBUtility.get().getUser(contactId, new MyCallBack<User>(){
            @Override
            public void onCallBack(User cUser) {
                if(cUser == null){
                    toastUser(getResources().getString(R.string.general_could_not_find_this_user_in_db));
                    return;
                }
                ChatRelation newRelation = new ChatRelation(mUser_, cUser);
                newRelation.addToDB(DBUtility.get().getDb_());
                mUser_.addChatRelation(newRelation,  DBUtility.get().getDb_());
                cUser.addChatRelation(newRelation, DBUtility.get().getDb_());
                setCurrentRelationId_(newRelation.getId_());
                displayMessages();
            }
        } );
    }

    private void toastUser(String text){
        Toast.makeText(this, text, Toast.LENGTH_SHORT);
    }
    /**
     * ViewHolder class to handle the RecyclerView
     */
    public static class MessageHolder extends RecyclerView.ViewHolder{
        TextView messageText_;
        TextView timeUserText_;

        public MessageHolder(View view) {
            super(view);
            messageText_ = view.findViewById(R.id.message_message);
            timeUserText_ = view.findViewById(R.id.message_time_stamp_user);
        }

    }
}
