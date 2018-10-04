package ch.epfl.swissteam.services;

import android.os.Bundle;
import android.app.Activity;

import java.util.Date;

public class OnlineChatActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_online_chat);

        displayChats();
    }

    private void displayChats(){

    }
}