package ch.epfl.swissteam.services;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;


public class MainActivity extends AppCompatActivity {
    private boolean logged = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent i = new Intent(this, NewProfileDetails.class);
        startActivity(i);

        /*
        if(!logged) {
            startLogin();
        }
        */
    }

    private void startLogin(){
        Intent intent = new Intent(this, NewProfileCapabilities.class);
        startActivity(intent);
    }
}
