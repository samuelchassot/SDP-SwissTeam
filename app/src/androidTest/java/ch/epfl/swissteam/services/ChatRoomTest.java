package ch.epfl.swissteam.services;

import android.support.test.espresso.contrib.DrawerActions;
import android.support.test.espresso.contrib.NavigationViewActions;
import android.support.test.rule.ActivityTestRule;

import com.google.firebase.database.FirebaseDatabase;

import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.scrollTo;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.hasDescendant;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static ch.epfl.swissteam.services.TestUtils.recyclerScrollToItemWithTextAndPerformClickItem;
import static ch.epfl.swissteam.services.TestUtils.sleep;

/**
 * Tests for ChatRoom
 *
 * @author Sébastien Gachoud
 */
@RunWith(AndroidJUnit4.class)
public class ChatRoomTest extends FirebaseTest{

    @Rule
    public final ActivityTestRule<MainActivity> mActivityRule =
            new ActivityTestRule<>(MainActivity.class);

    @Override
    public void initialize(){
        GoogleSignInSingleton.putUniqueID(TestUtils.M_GOOGLE_ID);
        TestUtils.O_USER.addToDB(FirebaseDatabase.getInstance().getReference());
        TestUtils.M_USER.addToDB(FirebaseDatabase.getInstance().getReference());
        sleep(100);
        onView(withId(R.id.drawer_layout)).perform(DrawerActions.open());
        sleep(100);
        onView(withId(R.id.nav_view)).perform(NavigationViewActions.navigateTo(R.id.button_maindrawer_services));
        sleep(2000);
        recyclerScrollToItemWithTextAndPerformClickItem(R.id.services_recycler, TestUtils.O_USER.getName_());
        sleep(1000);
        onView(withId(R.id.button_profile_toChat)).perform(click());
    }

    @Test
    public void sendMessageWorksWithNonEmpty() {
       String text = "Le roi est mort ! Vive le roi !";
        sleep(100);
        onView(withId(R.id.message_input)).perform(typeText(text)).check(matches(withText(text)));
        sleep(100);
        onView(withId(R.id.message_send_button)).perform(click());
        sleep(100);
        onView(withId(R.id.recycler_view_message)).check(matches(hasDescendant(withText(text))));
    }

    /* Examples
        - onView(nthChildOf(withId(R.id.recycleview), 0).check(matches(hasDescendant(withText("Some text"))))
        - onView(withId(R.id.recycleview)).check(matches(hasDescendant(withText("Some text"))))
     */
}
