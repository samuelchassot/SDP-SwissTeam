package ch.epfl.swissteam.services;

import android.content.Intent;
import android.support.test.espresso.contrib.DrawerActions;
import android.support.test.espresso.contrib.NavigationViewActions;
import android.support.test.espresso.intent.Intents;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.google.firebase.database.FirebaseDatabase;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withEffectiveVisibility;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static ch.epfl.swissteam.services.NewProfileDetails.GOOGLE_ID_TAG;
import static ch.epfl.swissteam.services.TestUtils.M_USER;
import static ch.epfl.swissteam.services.TestUtils.O_USER;
import static ch.epfl.swissteam.services.TestUtils.recyclerScrollToItemWithTextAndPerformClickItem;
import static ch.epfl.swissteam.services.TestUtils.sleep;

/**
 * A class to test elements of the ProfileActivity
 *
 * @author Ghali Chraibi
 */
@RunWith(AndroidJUnit4.class)
public class ProfileActivityTest extends SocializeTest<ProfileActivity> {
    
    private static final int SLEEP_TIME = 500;

    public ProfileActivityTest(){
        setTestRule(ProfileActivity.class);
    }

    @Override
    public void initialize() {
        GoogleSignInSingleton.putUniqueID(TestUtils.M_GOOGLE_ID);
        TestUtils.O_USER.addToDB(DBUtility.get().getDb_());
        M_USER.addToDB(DBUtility.get().getDb_());
        sleep(SLEEP_TIME);
    }

    @Test
    public void isProfileCorrectlyDisplayed(){
        startIntentWith(O_USER.getGoogleId_());
        onView(withId(R.id.textView_profile_nameTag)).check(matches(withText(TestUtils.O_USER.getName_())));
        onView(withId(R.id.textView_profile_email)).check(matches(withText(TestUtils.O_USER.getEmail_())));
        onView(withId(R.id.textView_profile_description)).check(matches(withText(TestUtils.O_USER.getDescription_())));
    }

    @Test
    public void canAccessToChatButtonIfOtherProfile() {
        startIntentWith(O_USER.getGoogleId_());
        onView(withId(R.id.button_profile_toChat)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));
    }

    @Test
    public void cantAccessToChatButtonIfMyProfile() {
        startIntentWith(M_USER.getGoogleId_());
        onView(withId(R.id.button_profile_toChat)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.INVISIBLE)));
    }

    @Test
    public void votesWorks(){
        startIntentWith(O_USER.getGoogleId_());
        onView(withId(R.id.button_profile_upvote)).perform(click());
        onView(withId(R.id.button_profile_downvote)).perform(click());
    }

    @Override
    public Intent getActivityIntent(){
        Intent intent = new Intent();
        intent.putExtra(GOOGLE_ID_TAG, O_USER.getGoogleId_());
        return intent;
    }

    private void startIntentWith(String id){
        Intent intent = new Intent();
        intent.putExtra(GOOGLE_ID_TAG, id);
        testRule_.finishActivity();
        testRule_.launchActivity(intent);
        sleep(SLEEP_TIME);
    }
}
