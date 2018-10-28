package ch.epfl.swissteam.services;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

/**
 * This class is the MainActivity of the application, this is
 * the home activity that displays the feed of local demands
 *
 * @author Samuel Chassot
 */
public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private Fragment profileShowerFragment_;
    private Fragment homeFragment_, createPostFragment_, servicesFragment_, settingsFragment_, onlineChatFragment_, myPostsFragment_;
    private NetworkStatusReceiver br;

    private DBUtility util = DBUtility.get();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        TextView navHeaderName = (TextView) findViewById(R.id.nav_header_name);
        br = new NetworkStatusReceiver();
        br.setActivity_(this);

        showHomeFragment();
    }

    @Override
    public void onResume() {
        super.onResume();
        IntentFilter intentFilter= new IntentFilter();
        intentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        registerReceiver(br, intentFilter);
    }

    @Override
    public void onPause() {
        super.onPause();
        unregisterReceiver(br);
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
            if(user != null) {
                ((TextView) findViewById(R.id.nav_header_name)).setText(user.getName_());
                ((TextView) findViewById(R.id.nav_header_email)).setText(user.getEmail_());
                Picasso.get().load(user.getImageUrl_()).into((ImageView) findViewById(R.id.nav_header_profileimage));
            }
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
     * Shows the my posts Fragment, where the user can edit and delete his posts
     */
    private void showMyPostsFragment(){
        if (this.myPostsFragment_ == null) this.myPostsFragment_ = MyPostsFragment.newInstance();
        this.startTransactionFragment(this.myPostsFragment_);
    }

    /**
     * Shows the create post Fragment, where the user can create or edit a post
     */
    public void showCreatePostFragment(){
        if (this.createPostFragment_ == null) this.createPostFragment_ = CreatePostFragment.newInstance();
        this.startTransactionFragment(this.createPostFragment_);
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
