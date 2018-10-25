package ch.epfl.swissteam.services;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class NavigationDrawer extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    private Fragment profileShowerFragment_, homeFragment_,
            servicesFragment_, createPostFragment_, settingsFragment_,
            onlineChatFragment_, myPostsFragment_;

    private DBUtility util = DBUtility.get();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    protected void onCreateDrawer() {
        Log.e("NAVDRAWER", "BEGIN CREATE DRAWER");
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //TextView navHeaderName = (TextView) findViewById(R.id.nav_header_name);
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
        setNavUserName();
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

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        switch (id) {
            case (R.id.button_maindrawer_home) :
                showHomeFragment();
                break;
            case (R.id.button_maindrawer_services) :
                showServicesFragment();
                break;
            case (R.id.button_maindrawer_profile) :
                showProfileShowerFragment();
                break;
            case (R.id.button_maindrawer_createpost) :
                showCreatePostFragment();
                break;
            case (R.id.button_maindrawer_myposts) :
                showMyPostsFragment();
                break;
            case (R.id.button_maindrawer_settings) :
                showSettingsFragment();
                break;
            case (R.id.button_maindrawer_logout) :
                signOut();
                break;
            case (R.id.button_maindrawer_chats) :
                showChatsFragment();
                break;
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    /**
     * Set the user name and email in the nav
     */
    private void setNavUserName() {
        util.getUser(GoogleSignInSingleton.get().getClientUniqueID(), user -> {
            if(user != null){
                ((TextView) findViewById(R.id.nav_header_name)).setText(user.getName_());
                ((TextView) findViewById(R.id.nav_header_email)).setText(user.getEmail_());}
            Picasso.get().load(user.getImageUrl_()).into((ImageView)findViewById(R.id.nav_header_profileimage));
        });
    }

    /**
     * Sign out the user from the application.
     */
    private void signOut() {
        GoogleSignInSingleton.get().getClient().signOut();
        Intent intent = new Intent(this, SignInActivity.class);
        startActivity(intent);
    }

    /**
     * Shows the services Fragment
     */
    private void showServicesFragment(){
        if (this.servicesFragment_ == null) this.servicesFragment_ = ServicesFragment.newInstance();
        this.startTransactionFragment(this.servicesFragment_);
    }

    /**
     * Shows the create post Fragment
     */
    private void showCreatePostFragment(){
        if (this.createPostFragment_ == null) this.createPostFragment_ = CreatePostFragment.newInstance();
        this.startTransactionFragment(this.createPostFragment_);
    }

    /**
     * Shows the my posts Fragment, where the user can edit and delete his posts
     */
    private void showMyPostsFragment(){
        if (this.myPostsFragment_ == null) this.myPostsFragment_ = MyPostsFragment.newInstance();
        this.startTransactionFragment(this.myPostsFragment_);
    }

    /**
     * Shows the profile shower fragment
     */
    private void showProfileShowerFragment(){
        if(this.profileShowerFragment_ == null){
            this.profileShowerFragment_ = ProfileDisplayFragment.newInstance();
        }

        this.startTransactionFragment(this.profileShowerFragment_);
    }


    /**
     * Shows the home Fragment, with the feed of spontaneous posts
     */
    public void showHomeFragment(){
        if (this.homeFragment_ == null) this.homeFragment_ = HomeFragment.newInstance();
        this.startTransactionFragment(this.homeFragment_);
    }


    private void showChatsFragment(){
        if (this.onlineChatFragment_ == null) this.onlineChatFragment_ = OnlineChatFragment.newInstance();
        this.startTransactionFragment(this.onlineChatFragment_);
    }

    /**
     * Show the settings Fragment
     */
    private void showSettingsFragment() {
        if (this.settingsFragment_ == null) this.settingsFragment_ = SettingsFragment.newInstance();
        this.startTransactionFragment(this.settingsFragment_);
    }

    /**
     * Initiate the fragment transaction
     *
     * @param fragment the fragment to show
     */
    private void startTransactionFragment(Fragment fragment){
        if (!fragment.isVisible()){
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.framelayout_main_fragmentcontainer, fragment).commit();
        }
    }

}
