package ch.epfl.swissteam.services;

import android.os.Bundle;
import android.app.Activity;
import android.support.design.widget.TextInputEditText;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseListAdapter;

/**
 * This activity is a chat room, it display messages and allow to write and send messages
 *
 * @author Sébastien Gachoud
 */
public class ChatRoom extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_room);

        displayMessages();
    }

    /**
     * display messages retrieved form the database
     */
    private void displayMessages(){
        /*ListView chatRoom = findViewById(R.id.list_view_message);
        adapter_ = new FirebaseListAdapter<ChatMessage>(
                this, ChatMessage.class, R.layout.chat_message_layout, dataBase.getInstance().getReference())
        {
            @Override
            protected void populateView(View view, ChatMessage message, int position){
                ((TextView)findViewById(R.id.message_message)).setText(message.getText());
                ((TextView)findViewById(R.id.message_user)).setText(message.getUser());
                ((TextView)findViewById(R.id.message_time_stamp)).setText(
                        DateFormat.format("dd-mm-yyyy (hh:mm:ss)",
                        message.getTime()));
            }
        };
        chatRoom.setAdapter(adapter_);*/
    }

    /**
     * Send message to the database
     * @param view
     */
    public void sendMessage(View view){
        TextInputEditText textInput = findViewById(R.id.message_input);
        String message = textInput.getText().toString();
        /*ChatMessage chatMessage = new ChatMessage(message, dataBase.getUser());
        dataBase.getInstance().getReference().push().setValue(chatMessage);
        */
        textInput.getText().clear();
    }

    FirebaseListAdapter<ChatMessage> adapter_;
}
