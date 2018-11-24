package ch.epfl.swissteam.services;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.v4.view.GravityCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import static ch.epfl.swissteam.services.DBUtility.get;


/**
 * This activity is a chat room, it display messages and allow to write and send messages
 *
 * @author Sébastien Gachoud
 */
public class ChatRoom extends NavigationDrawer {

    private FirebaseRecyclerAdapter<ChatMessage, MessageHolder> adapter_;
    private DatabaseReference dataBase_;
    private String currentRelationId_;
    private User mUser_;
    private boolean isDeletedRelation = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_chat_room);
        super.onCreateDrawer(BACK);
        dataBase_ = DBUtility.get().getDb_();

        setCurrentRelationId_(getIntent().getExtras().getString(ChatRelation.RELATION_ID_TEXT, null));
        checkAndSetIfDeletedByPartner();
        retrieveUserAndSetRelationId();
    }

    @Override
    public void onBackPressed() {
        if (isDrawerOpened()) {
            super.onBackPressed();
        } else {
            Intent intent = new Intent(this, MainActivity.class);
            intent.putExtra(NAVIGATION_TAG, R.id.button_maindrawer_chats);
            startActivity(intent);
        }
    }

    private void retrieveUserAndSetRelationId(){
        DBUtility.get().getUser(GoogleSignInSingleton.get().getClientUniqueID(), mUser -> {
            if(mUser == null){
                toastUser(getResources().getString(R.string.database_could_not_find_you_in_db));
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
        });
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
                viewHolder.parentLayout_.setOnLongClickListener(new View.OnLongClickListener(){
                    private String ref_ = getRef(position).getKey();
                    @Override
                    public boolean onLongClick(View view) {
                        askToDeleteMessage(message, ref_);
                        return true;
                    }
                });
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
            toastUser(getResources().getString(R.string.database_could_not_find_you_in_db));
            return;
        }
        if(message.isEmpty()){
            return;
        }
        //If nothing works to establish the chat
        if(currentRelationId_ == null) {
            toastUser(getResources().getString(R.string.database_could_not_establish_relation));
            return;
        }
        if(isDeletedRelation){
            toastUser(getResources().getString(R.string.chat_deleted_chat));
        }

        ChatMessage chatMessage = new ChatMessage(message, mUser_.getName_(), mUser_.getGoogleId_(), currentRelationId_);
        chatMessage.addToDB(dataBase_);

        textInput.getText().clear();
    }

    private void newRelationWith(String contactId ){
        DBUtility.get().getUser(contactId, cUser -> {
            if(cUser == null){
                toastUser(getResources().getString(R.string.database_could_not_find_this_user_in_db));
                return;
            }

            ChatRelation newRelation = new ChatRelation(mUser_, cUser);
            newRelation.addToDB(DBUtility.get().getDb_());
            mUser_.addChatRelation(newRelation,  DBUtility.get().getDb_());
            cUser.addChatRelation(newRelation, DBUtility.get().getDb_());
            setCurrentRelationId_(newRelation.getId_());
            displayMessages();
        });
    }

    private void askToDeleteMessage(ChatMessage message, String key){
        Resources res = getResources();
        Utility.askToDeleteAlertDialog(this, res.getString(R.string.chat_delete_alert_title),
                res.getString(R.string.chat_delete_alert_text), isDeletionSelected -> {
                    if(isDeletionSelected) {
                        message.removeFromDB(get().getDb_(), key);
                    }
                });
    }

    private void checkAndSetIfDeletedByPartner(){
        if(currentRelationId_ != null)
            DBUtility.get().getChatRelation(currentRelationId_, chatRelation ->{
                DBUtility.get().getUser(chatRelation.getOtherId(GoogleSignInSingleton.get().getClientUniqueID()),
                        user-> isDeletedRelation = user == null || user.relationExists(GoogleSignInSingleton.get().getClientUniqueID()) == null);
            });
    }

    private void toastUser(String text){
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }

    /**
     * ViewHolder class to handle the RecyclerView
     */
    public static class MessageHolder extends RecyclerView.ViewHolder{
      
        protected TextView messageText_;
        protected TextView timeUserText_;
        protected LinearLayout parentLayout_;

        /**
         * Create a MessageViewHolder
         *
         * @param view the current View
         */
        public MessageHolder(View view) {
            super(view);
            messageText_ = view.findViewById(R.id.message_message);
            timeUserText_ = view.findViewById(R.id.message_time_stamp_user);
            parentLayout_ = view.findViewById(R.id.chat_message_parent_layout);
        }

    }
}
