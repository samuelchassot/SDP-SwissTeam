package ch.epfl.swissteam.services;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.Serializable;
import java.util.Date;

import ch.epfl.swissteam.services.SignInActivity;


/**
 * This class is the MainActivity of the application, this is
 * the home activity that displays the feed of local demands
 *
 * @author Samuel Chassot
 */
public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private Fragment servicesFragment, createPostFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        /*
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        */

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //TODO Load home fragment
        //showHomeFragment();


    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();


        /**
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
         **/

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        switch (id) {
            case (R.id.button_maindrawer_services) :
                showServicesFragment();
                break;
            case (R.id.button_maindrawer_createpost) :
                showCreatePostFragment();
                break;
            case (R.id.button_maindrawer_logout) :
                signOut();
                break;
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    /**
     * Sign out the user from the application.
     */
    private void signOut() {
        SignInActivity.mGoogleSignInClient_.signOut();
        Intent intent = new Intent(this, SignInActivity.class);
        startActivity(intent);
    }

    /**
     * Shows the services Fragment
     */
    private void showServicesFragment(){
        if (this.servicesFragment == null) this.servicesFragment = ServicesFragment.newInstance();
        this.startTransactionFragment(this.servicesFragment);
    }

    /**
     * Shows the create post Fragment
     */
    private void showCreatePostFragment(){
        if (this.createPostFragment == null) this.createPostFragment = CreatePostFragment.newInstance();
        this.startTransactionFragment(this.createPostFragment);
    }

    /**
     * Initiate the fragment transaction
     * @param fragment the fragment to show
     */
    private void startTransactionFragment(Fragment fragment){
        if (!fragment.isVisible()){
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.framelayout_main_fragmentcontainer, fragment).commit();
        }
    }

    /**
     * Send a post the to Firebase database.
     *
     * @param view view
     */
    public void submitPost(View view) {
        EditText titleField = ((EditText)findViewById(R.id.plaintext_createpostfragment_title));
        EditText bodyField = ((EditText)findViewById(R.id.plaintext_createpostfragment_body));
        if(TextUtils.isEmpty(titleField.getText())) {
            Toast.makeText(this, R.string.createpostfragment_titleempty, Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(bodyField.getText())) {
            Toast.makeText(this, R.string.createpostfragment_bodyempty, Toast.LENGTH_SHORT).show();
        }
        else {
            String title = titleField.getText().toString();
            String body = bodyField.getText().toString();

            //TODO: replace username by actual username once the local db works.
            Post post = new Post(title, "username", body, (new Date()).getTime());

            //DatabaseReference mDataBase = FirebaseDatabase.getInstance().getReference();
            //mDataBase.push().setValue(post);
        }
    }
}
