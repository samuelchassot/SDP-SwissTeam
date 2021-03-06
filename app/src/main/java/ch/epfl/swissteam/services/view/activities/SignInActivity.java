package ch.epfl.swissteam.services.view.activities;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

import ch.epfl.swissteam.services.R;
import ch.epfl.swissteam.services.providers.DBUtility;
import ch.epfl.swissteam.services.providers.GoogleSignInSingleton;
import ch.epfl.swissteam.services.providers.LocationManager;
import ch.epfl.swissteam.services.providers.SettingsDBUtility;
import ch.epfl.swissteam.services.utils.NotificationUtils;
import ch.epfl.swissteam.services.utils.SettingsDbHelper;

/**
 * This class is an activity created to make the user authenticate with Google.
 * This activity sends then the user either to set up a new profile if it is the
 * first time they sign in, either to the main board.
 *
 * @author Julie Giunta
 */
public class SignInActivity extends AppCompatActivity implements View.OnClickListener {

    public static final String ACCOUNT_TAG = "ACCOUNT";
    //Request code for startActivityForResult
    private static final int RC_SIGN_IN = 42;

    private GoogleSignInClient mGoogleSignInClient_;
    private SettingsDbHelper settingsDbHelper_;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);

        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient_ = GoogleSignIn.getClient(this, gso);

        //put the GoogleSignInClient in the singleton
        GoogleSignInSingleton.putGoogleSignInClient(mGoogleSignInClient_);


        //Listen to clicks on the signIn button
        findViewById(R.id.button_signin_googlesignin).setOnClickListener(this);

        settingsDbHelper_ = new SettingsDbHelper(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        // Check for existing Google Sign In account, if the user is already signed in
        // the GoogleSignInAccount will be non-null.
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        initializeNotifications();

        if (account != null) {
            endSetUp(account, MainActivity.class);
        }
    }

    //Starting the intent prompts the user to select a Google account to sign in with
    @Override
    public void onClick(View v) {
        Intent signInIntent = mGoogleSignInClient_.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    /**
     * Sends the new user with the account he signed with in the new profile set up page
     * after the completion of the sign in task of Google.
     * If the task fails, provide a log of the error and then recreate the activity.
     *
     * @param completedTask the task of signing in, completed
     */
    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);

            DBUtility.get().getUser(account.getId(), user -> {
                if (user != null) {
                    endSetUp(account, MainActivity.class);
                } else {
                    // Signed in successfully, show authenticated UI
                    endSetUp(account, NewProfileDetailsActivity.class);
                }
            });

        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w("SignInActivity", "signInResult:failed code=" + e.getStatusCode());
            recreate();
        }
    }

    /**
     * End of the set up of the user in the app, start the wanted activity
     *
     * @param account      the account of the user
     * @param nextActivity the activity to start after the set up.
     */
    private void endSetUp(GoogleSignInAccount account, Class nextActivity) {
        SettingsDBUtility.addRowIfNeeded(settingsDbHelper_, account.getId());
        GoogleSignInSingleton.putUniqueID(account.getId());
        Intent intent = new Intent(this, nextActivity);
        intent.putExtra(ACCOUNT_TAG, account);
        startActivity(intent);
    }

    @Override
    public void onUserInteraction() {
        LocationManager.get().refresh(this);
    }

    /**
     * Initializes the notifications and attaches the listeners.
     */
    private void initializeNotifications() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.signin_channel_name);
            String description = getString(R.string.signin_channel_description);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(NotificationUtils.CUSTOM_NOTIFICATION_CHANNEL_ID, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

}
