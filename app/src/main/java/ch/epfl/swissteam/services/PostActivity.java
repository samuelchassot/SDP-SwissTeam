package ch.epfl.swissteam.services;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Date;

public class PostActivity extends AppCompatActivity {
    private Post post_;
    private TextView username_, title_, body_, date_;
    private ImageView picture_;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        Intent intent = getIntent();
        post_ = intent.getParcelableExtra(PostAdapter.POST_TAG);

        username_ = findViewById(R.id.textview_postactivity_username);
        title_ = findViewById(R.id.textview_postactivity_title);
        body_ = findViewById(R.id.textview_postactivity_body);
        date_ = findViewById(R.id.textview_postactivity_date);
        picture_ = findViewById(R.id.imageview_postactivity_picture);

        //TODO retrieve username
        username_.setText(post_.getGoogleId_());
        title_.setText(post_.getTitle_());
        body_.setText(post_.getBody_());
        date_.setText("Posted : " + (new Date(post_.getTimestamp_()).toString());
    }
}
