package ch.epfl.swissteam.services;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.Intent;
import android.support.test.espresso.intent.rule.IntentsTestRule;

import android.support.test.runner.AndroidJUnit4;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.intent.Intents.intending;
import static android.support.test.espresso.intent.matcher.IntentMatchers.anyIntent;
import static android.support.test.espresso.matcher.ViewMatchers.withId;


@RunWith(AndroidJUnit4.class)
public class SignInActivityTest {
    @Rule
    public final IntentsTestRule<SignInActivity> signInActivityRule_ =
            new IntentsTestRule<>(SignInActivity.class);

    @Test
    public void connectWithNonexistentAccount() {
        if(GoogleSignIn.getLastSignedInAccount(signInActivityRule_.getActivity()) != null) {
            GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestEmail()
                    .build();
            GoogleSignIn.getClient(signInActivityRule_.getActivity().getApplicationContext(), gso).signOut();
            signInActivityRule_.finishActivity();
            signInActivityRule_.launchActivity(new Intent());
        }
        Intent intent = new Intent();
        Instrumentation.ActivityResult result = new Instrumentation.ActivityResult(Activity.RESULT_OK, intent);
        intending(anyIntent()).respondWith(result);
        onView(withId(R.id.button_signin_googlesignin)).perform(click());
    }
}
