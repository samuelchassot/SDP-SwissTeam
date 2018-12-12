package ch.epfl.swissteam.services.view.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.EditText;

import ch.epfl.swissteam.services.view.builders.MyPostAdapter;
import ch.epfl.swissteam.services.R;
import ch.epfl.swissteam.services.models.Post;
import ch.epfl.swissteam.services.providers.DBUtility;

/**
 * Activity to edit a selected Post from the currently connected User
 *
 * @author Julie Giunta
 */
public class MyPostEditActivity extends NavigationDrawerActivity {

    private Post post_;
    private EditText title_, body_;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_my_post_edit);
        super.onCreateDrawer(ToogleState.CANCEL);

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
            return true;
        }

        /*
         //noinspection SimplifiableIfStatement
         if (id == R.id.action_settings) {
         return true;
         }
         */

        return super.onOptionsItemSelected(item);
    }
}
