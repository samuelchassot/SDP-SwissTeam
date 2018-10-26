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

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.scrollTo;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withEffectiveVisibility;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static ch.epfl.swissteam.services.TestUtils.recyclerScrollToItemWithTextAndPerformClickItem;
import static ch.epfl.swissteam.services.TestUtils.sleep;

/**
 * A class to test elements of the ProfileActivity
 *
 * @author Ghali Chraibi
 */
@RunWith(AndroidJUnit4.class)
public class ProfileActivityTest extends FirebaseTest {

    
    private static final int SLEEP_TIME = 1000;

    @Rule
    public final ActivityTestRule<MainActivity> mActivityRule =
            new ActivityTestRule<>(MainActivity.class);

    @Override
    public void initialize() {
        GoogleSignInSingleton.putUniqueID(TestUtils.M_GOOGLE_ID);
        TestUtils.O_USER.addToDB(FirebaseDatabase.getInstance().getReference());
        TestUtils.M_USER.addToDB(FirebaseDatabase.getInstance().getReference());
        onView(withId(R.id.drawer_layout)).perform(DrawerActions.open());
        onView(withId(R.id.nav_view)).perform(NavigationViewActions.navigateTo(R.id.button_maindrawer_services));
        sleep(SLEEP_TIME);
        Intents.init();
    }

    @Test
    public void isProfileCorrectlyDisplayed(){
        /*sleep(SLEEP_TIME);
        recyclerScrollToItemWithTextAndPerformClickItem(R.id.services_recycler, TestUtils.O_USER.getName_());
        sleep(SLEEP_TIME);
        onView(withId(R.id.textView_profile_nameTag)).check(matches(withText(TestUtils.O_USER.getName_())));
        onView(withId(R.id.textView_profile_email)).check(matches(withText(TestUtils.O_USER.getEmail_())));
        onView(withId(R.id.textView_profile_description)).check(matches(withText(TestUtils.O_USER.getDescription_())));*/
        //onView(withId(R.id.imageview_profile_picture)).check(matches(withText(oUser_.getImageUrl_())));
    }

    @Test
    public void canAccessToChatButtonIfOtherProfile() {
        /*sleep(SLEEP_TIME);
        recyclerScrollToItemWithTextAndPerformClickItem(R.id.services_recycler, TestUtils.O_USER.getName_());
        sleep(SLEEP_TIME);
        onView(withId(R.id.button_profile_toChat)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));*/
    }

    @Test
    public void cantAccessToChatButtonIfMyProfile() {
        sleep(SLEEP_TIME);
        recyclerScrollToItemWithTextAndPerformClickItem(R.id.services_recycler, TestUtils.M_USER.getName_());
        sleep(SLEEP_TIME);
        onView(withId(R.id.button_profile_toChat)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.INVISIBLE)));
    }



    @Override
    public void terminate(){
        Intents.release();
    }
}
