package ch.epfl.swissteam.services;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

/**
 * Activity to edit a selected Post from the currently connected User
 *
 * @author Julie Giunta
 */
public class MyPostEdit extends NavigationDrawer{
    private Post post_;
    private EditText title_, body_;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_post_edit);
        super.onCreateDrawer(CANCEL);

        Intent intent = getIntent();
        post_ = intent.getParcelableExtra(MyPostAdapter.MYPOST_TAG);

        title_ = findViewById(R.id.edittext_mypostedit_title);
        body_ = findViewById(R.id.edittext_mypostedit_body);

        title_.setText(post_.getTitle_());
        body_.setText(post_.getBody_());
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if(id == R.id.action_save){
            post_.setTitle_(title_.getText().toString());
            post_.setBody_(body_.getText().toString());
            DBUtility.get().setPost(post_);
            finish();
        }

        /**
         //noinspection SimplifiableIfStatement
         if (id == R.id.action_settings) {
         return true;
         }
         **/

        return super.onOptionsItemSelected(item);
    }
}
