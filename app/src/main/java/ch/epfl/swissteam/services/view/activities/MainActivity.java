package ch.epfl.swissteam.services.view.activities;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import ch.epfl.swissteam.services.view.fragments.CreatePostFragment;
import ch.epfl.swissteam.services.view.fragments.MyPostsFragment;
import ch.epfl.swissteam.services.view.fragments.NearbyFragment;
import ch.epfl.swissteam.services.view.fragments.OnlineChatFragment;
import ch.epfl.swissteam.services.view.fragments.ProfileDisplayFragment;
import ch.epfl.swissteam.services.R;
import ch.epfl.swissteam.services.view.fragments.ServicesFragment;
import ch.epfl.swissteam.services.view.fragments.SettingsFragment;
import ch.epfl.swissteam.services.view.fragments.TodoListFragment;
import ch.epfl.swissteam.services.providers.DBUtility;
import ch.epfl.swissteam.services.providers.GoogleSignInSingleton;
import ch.epfl.swissteam.services.providers.LocationManager;
import ch.epfl.swissteam.services.providers.NetworkStatusReceiver;

/**
 * This class is the MainActivity of the application, this is
 * the home activity that displays the feed of local demands
 *
 * @author Samuel Chassot
 */
public class MainActivity extends NavigationDrawerActivity {
    private Fragment profileShowerFragment_, homeFragment_,
            servicesFragment_, createPostFragment_, settingsFragment_,
            onlineChatFragment_, myPostsFragment_, todoListFragment_;

    private NetworkStatusReceiver br_;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        super.onCreateDrawer(ToogleState.MAIN);

        br_ = new NetworkStatusReceiver();
        br_.setActivity_(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        DBUtility.get().notifyNewMessages(this, GoogleSignInSingleton.get().getClientUniqueID());
        Intent intent = getIntent();
        chooseFragment(intent.getIntExtra(ToogleState.NAVIGATION_TAG.toString(), -1));
    }

    @Override
    public void onResume() {
        super.onResume();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        registerReceiver(br_, intentFilter);
    }

    @Override
    public void onPause() {
        super.onPause();
        unregisterReceiver(br_);
    }

    /**
     * Switch to a fragment depending on the selected button in the maindrawer
     *
     * @param id the id of the button
     */
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
            case (R.id.button_maindrawer_todoList):
                showTodoListFragment();
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
    public void showMyPostsFragment() {
        if (this.myPostsFragment_ == null) {
            this.myPostsFragment_ = MyPostsFragment.newInstance();
        }
        this.startTransactionFragment(this.myPostsFragment_);
    }

    /**
     * Shows the TodoList Fragment, where the user can see a list of the post he is involved in
     */
    public void showTodoListFragment() {
        if (this.todoListFragment_ == null) {
            this.todoListFragment_ = TodoListFragment.newInstance();
        }
        this.startTransactionFragment(this.todoListFragment_);
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
            this.homeFragment_ = NearbyFragment.newInstance();
        }
        this.startTransactionFragment(this.homeFragment_);
    }

    /**
     * Shows the chat Fragment, with the current relations with whom the user can communicate
     */
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
