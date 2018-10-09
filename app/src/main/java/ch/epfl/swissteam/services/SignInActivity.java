package ch.epfl.swissteam.services;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

import java.io.Serializable;

/**
 * This class is an activity created to make the user authenticate with Google.
 * This activity sends then the user either to set up a new profile if it is the
 * first time they sign in, either to the main board.
 *
 * @author Julie Giunta
 */
public class SignInActivity extends AppCompatActivity implements View.OnClickListener {
    //Request code for startActivityForResult
    private static final int RC_SIGN_IN = 42;

    private GoogleSignInClient mGoogleSignInClient_;
    public static final String ACCOUNT_TAG = "ch.epfl.swissteam.services.account";
    private final String ERROR_TAG = "SignInActivity";
    private final String ERROR_MSG = "signInResult:failed code=";




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
    }

    @Override
    protected void onStart(){
        super.onStart();
        // Check for existing Google Sign In account, if the user is already signed in
        // the GoogleSignInAccount will be non-null.
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        if(account != null ){
            // Launch main

            // put uniqueID in the singleton
            GoogleSignInSingleton.putUniqueID(account.getId());
            Intent mainIntent = new Intent(this, MainActivity.class);
            mainIntent.putExtra(ACCOUNT_TAG , account);
            startActivity(mainIntent);
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

            // Signed in successfully, show authenticated UI
            Intent newProfileIntent = new Intent(this, NewProfileDetails.class);
            newProfileIntent.putExtra(ACCOUNT_TAG , account);
            startActivity(newProfileIntent);

        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w(ERROR_TAG, ERROR_MSG + e.getStatusCode());
            recreate();
        }
    }
}
