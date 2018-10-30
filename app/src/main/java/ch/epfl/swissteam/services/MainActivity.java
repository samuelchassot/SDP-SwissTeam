package ch.epfl.swissteam.services;

import android.content.Intent;
import android.content.IntentFilter;
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

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.squareup.picasso.Picasso;

/**
 * This class is the MainActivity of the application, this is
 * the home activity that displays the feed of local demands
 *
 * @author Samuel Chassot
 */
public class MainActivity extends NavigationDrawer {
    private Fragment profileShowerFragment_, homeFragment_,
            servicesFragment_, createPostFragment_, settingsFragment_,
            onlineChatFragment_, myPostsFragment_;

    private NetworkStatusReceiver br;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        super.onCreateDrawer(MAIN);

        br = new NetworkStatusReceiver();
        br.setActivity_(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Intent intent = getIntent();
        chooseFragment(intent.getIntExtra(NAVIGATION_TAG, -1));
    }

    @Override
    public void onResume() {
        super.onResume();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        registerReceiver(br, intentFilter);
    }

    @Override
    public void onPause() {
        super.onPause();
        unregisterReceiver(br);
    }
    
      
    public void chooseFragment(int id) {
        switch (id) {
            case (R.id.button_maindrawer_home):
                showHomeFragment();
                break;
            case (R.id.button_maindrawer_services):
                showServicesFragment();
                break;
            case (R.id.button_maindrawer_profile):
                showProfileShowerFragment();
                break;
            case (R.id.button_maindrawer_myposts):
                showMyPostsFragment();
                break;
            case (R.id.button_maindrawer_settings):
                showSettingsFragment();
                break;
            case (R.id.button_maindrawer_logout):
                signOut();
                break;
            case (R.id.button_maindrawer_chats):
                showChatsFragment();
                break;
            default :
                showHomeFragment();
                break;
        }
    }
    

    /**
     * Initiate the fragment transaction
     *
     * @param fragment the fragment to show
     */
    protected void startTransactionFragment(Fragment fragment) {
        if (!fragment.isVisible()){
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.framelayout_main_fragmentcontainer, fragment).commit();
        }
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
    private void showServicesFragment() {
        if (this.servicesFragment_ == null) {
            this.servicesFragment_ = ServicesFragment.newInstance();
        }
        this.startTransactionFragment(this.servicesFragment_);
    }

    /**
     * Shows the my posts Fragment, where the user can edit and delete his posts
     */
    private void showMyPostsFragment() {
        if (this.myPostsFragment_ == null) {
            this.myPostsFragment_ = MyPostsFragment.newInstance();
        }
        this.startTransactionFragment(this.myPostsFragment_);
    }

    /**
     * Shows the create post Fragment, where the user can create or edit a post
     */
    public void showCreatePostFragment() {
        if (this.createPostFragment_ == null) {
            this.createPostFragment_ = CreatePostFragment.newInstance();
        }
        this.startTransactionFragment(this.createPostFragment_);
    }

    /**
     * Shows the profile shower fragment
     */
    private void showProfileShowerFragment() {
        if (this.profileShowerFragment_ == null) {
            this.profileShowerFragment_ = ProfileDisplayFragment.newInstance();
        }
        this.startTransactionFragment(this.profileShowerFragment_);
    }

    /**
     * Shows the home Fragment, with the feed of spontaneous posts
     */
    public void showHomeFragment() {
        if (this.homeFragment_ == null) {
            this.homeFragment_ = HomeFragment.newInstance();
        }
        this.startTransactionFragment(this.homeFragment_);
    }


    private void showChatsFragment() {
        if (this.onlineChatFragment_ == null) {
            this.onlineChatFragment_ = OnlineChatFragment.newInstance();
        }
        this.startTransactionFragment(this.onlineChatFragment_);
    }

    /**
     * Show the settings Fragment
     */
    private void showSettingsFragment() {
        if (this.settingsFragment_ == null) this.settingsFragment_ = SettingsFragment.newInstance();
        this.startTransactionFragment(this.settingsFragment_);
    }

    @Override
    public void onUserInteraction() {
        LocationManager.get().refresh(this);
    }
}
