package ch.epfl.swissteam.services;

import android.support.test.espresso.contrib.DrawerActions;
import android.support.test.espresso.contrib.NavigationViewActions;
import android.support.test.rule.ActivityTestRule;

import com.google.firebase.database.FirebaseDatabase;

import org.junit.Rule;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.hasDescendant;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static ch.epfl.swissteam.services.UtilityTest.nthChildOf;
import static ch.epfl.swissteam.services.UtilityTest.sleep;

/**
 * Tests for ChatRoom
 *
 * @author Sébastien Gachoud
 */
public class ChatRoomTest extends FirebaseTest{

    private static final String mGoogleId = "1234";
    private static final String oGoogleId = "5678";
    private static final String url = TestUtils.getATestUser().getImageUrl_();
    private static final ArrayList<Categories> cats = new ArrayList<>(Arrays.asList(Categories.COOKING));
    private static final User mUser = new User(mGoogleId,"Bear", "polar@north.nth","",null,url);
    private static final User oUser = new User(oGoogleId, "Raeb", "hairy@north.nth", "", cats, url);

    @Rule
    public final ActivityTestRule<MainActivity> mActivityRule =
            new ActivityTestRule<>(MainActivity.class);

    @Override
    public void initialize(){
        GoogleSignInSingleton.putUniqueID(mGoogleId);
        oUser.addToDB(FirebaseDatabase.getInstance().getReference());
        mUser.addToDB(FirebaseDatabase.getInstance().getReference());
        sleep(100);
        onView(withId(R.id.drawer_layout)).perform(DrawerActions.open());
        sleep(100);
        onView(withId(R.id.nav_view)).perform(NavigationViewActions.navigateTo(R.id.button_maindrawer_services));
        sleep(500);
        onView(withText(oUser.getName_())).perform(click());
        sleep(100);
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
