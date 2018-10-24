package ch.epfl.swissteam.services;

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

import java.util.ArrayList;
import java.util.Arrays;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withEffectiveVisibility;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static ch.epfl.swissteam.services.TestUtils.sleep;

/**
 * A class to test elements of the ProfileActivity
 *
 * @author Ghali Chraibi
 */
@RunWith(AndroidJUnit4.class)
public class ProfileActivityTest extends FirebaseTest {

    private static final String M_GOOGLE_ID_ = "1234";
    private static final String O_GOOGLE_ID_ = "456";
    private static final String URL_ = TestUtils.getATestUser().getImageUrl_();
    private static final ArrayList<Categories> cats = new ArrayList<>(Arrays.asList(Categories.COOKING));
    private static final User mUser_ = new User(M_GOOGLE_ID_,"Bear", "polar@north.nth","",null, URL_);
    private static final User oUser_ = new User(O_GOOGLE_ID_, "Raeb", "hairy@north.nth", "", cats, URL_);
    private static final int SLEEP_TIME = 8000;

    @Rule
    public final ActivityTestRule<MainActivity> mActivityRule =
            new ActivityTestRule<>(MainActivity.class);

    @Override
    public void initialize() {
        GoogleSignInSingleton.putUniqueID(M_GOOGLE_ID_);
        oUser_.addToDB(FirebaseDatabase.getInstance().getReference());
        mUser_.addToDB(FirebaseDatabase.getInstance().getReference());
        onView(withId(R.id.drawer_layout)).perform(DrawerActions.open());
        onView(withId(R.id.nav_view)).perform(NavigationViewActions.navigateTo(R.id.button_maindrawer_services));
        sleep(SLEEP_TIME);
        Intents.init();
    }

    @Test
    public void isProfileCorrectlyDisplayed(){
        sleep(SLEEP_TIME);
        onView(withText(oUser_.getName_())).perform(click());
        sleep(SLEEP_TIME);
        onView(withId(R.id.textView_profile_nameTag)).check(matches(withText(oUser_.getName_())));
        onView(withId(R.id.textView_profile_email)).check(matches(withText(oUser_.getEmail_())));
        onView(withId(R.id.textView_profile_description)).check(matches(withText(oUser_.getDescription_())));
        //onView(withId(R.id.imageview_profile_picture)).check(matches(withText(oUser_.getImageUrl_())));
    }

    /*@Test
    public void isTheNameInTheAssociatedTextView() {
        onView(withText(oUser_.getName_())).perform(click());
        sleep(SLEEP_TIME);
        onView(withId(R.id.textView_profile_nameTag)).check(matches(withText(oUser_.getName_())));
    }

    @Test
    public void isTheEmailInTheAssociatedTextView() {
        onView(withText(oUser_.getName_())).perform(click());
        sleep(SLEEP_TIME);
        onView(withId(R.id.textView_profile_email)).check(matches(withText(oUser_.getEmail_())));
    }

    @Test
    public void isTheDescriptionInTheAssociatedTextView() {
        onView(withText(oUser_.getName_())).perform(click());
        sleep(SLEEP_TIME);
        onView(withId(R.id.textView_profile_description)).check(matches(withText(oUser_.getDescription_())));
    }*/
/*
    @Test
    public void isTheImageInTheAssociatedTextView() {
        onView(withText(oUser_.getName_())).perform(click());
        sleep(SLEEP_TIME);
        onView(withId(R.id.imageview_profile_picture)).check(matches(withText(oUser_.getImageUrl_())));
    }
*/
    @Test
    public void canAccessToChatButtonIfOtherProfile() {
        sleep(SLEEP_TIME);
        onView(withText(oUser_.getName_())).perform(click());
        sleep(SLEEP_TIME);
        onView(withId(R.id.button_profile_toChat)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));
    }

    @Test
    public void cantAccessToChatButtonIfMyProfile() {
        sleep(SLEEP_TIME);
        onView(withText(mUser_.getName_())).perform(click());
        sleep(SLEEP_TIME);
        onView(withId(R.id.button_profile_toChat)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.INVISIBLE)));
    }



    @Override
    public void terminate(){
        Intents.release();
    }
}
