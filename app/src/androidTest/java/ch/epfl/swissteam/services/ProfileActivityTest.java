package ch.epfl.swissteam.services;

import android.support.test.espresso.contrib.DrawerActions;
import android.support.test.espresso.contrib.NavigationViewActions;
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
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

@RunWith(AndroidJUnit4.class)
public class ProfileActivityTest extends FirebaseTest {

    private static final String mGoogleId_ = "1234";
    private static final String oGoogleId_ = "456";
    private static final String url_ = TestUtils.getATestUser().getImageUrl_();
    private static final String name_ = "Mark", email_ = "mtSucre@fb.com", description_ = "Hi there, I'm using Socialize!";
    private static final ArrayList<Categories> cats = new ArrayList<>(Arrays.asList(Categories.COOKING));
    private static final User mUser_ = new User(mGoogleId_,"Bear", "polar@north.nth","",null, url_);
    private static final User oUser_ = new User(oGoogleId_, "Raeb", "hairy@north.nth", "", cats, url_);

    @Rule
    public final ActivityTestRule<MainActivity> mActivityRule =
            new ActivityTestRule<>(MainActivity.class);

    @Override
    public void initialize() {
        GoogleSignInSingleton.putUniqueID(mGoogleId_);
        oUser_.addToDB(FirebaseDatabase.getInstance().getReference());
        mUser_.addToDB(FirebaseDatabase.getInstance().getReference());
        onView(withId(R.id.drawer_layout)).perform(DrawerActions.open());
        try {
            Thread.sleep(300);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        onView(withId(R.id.nav_view)).perform(NavigationViewActions.navigateTo(R.id.button_maindrawer_services));
    }

    @Test
    public void isTheNameInTheAssociatedTextView() {
        onView(withText(oUser_.getName_())).perform(click());
        try {
            Thread.sleep(300);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        onView(withId(R.id.textView_profile_nameTag)).check(matches(withText(oUser_.getName_())));
    }

    @Test
    public void isTheEmailInTheAssociatedTextView() {
        onView(withText(oUser_.getName_())).perform(click());
        try {
            Thread.sleep(300);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        onView(withId(R.id.textView_profile_email)).check(matches(withText(oUser_.getEmail_())));
    }

    @Test
    public void isTheDescriptionInTheAssociatedTextView() {
        try {
            Thread.sleep(300);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        onView(withText(oUser_.getName_())).perform(click());
        try {
            Thread.sleep(300);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        onView(withId(R.id.textView_profile_description)).check(matches(withText(oUser_.getDescription_())));
    }

    @Test
    public void isTheImageInTheAssociatedTextView() {
        try {
            Thread.sleep(300);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        onView(withText(oUser_.getName_())).perform(click());
        try {
            Thread.sleep(300);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        onView(withId(R.id.imageview_profile_picture)).check(matches(withText(oUser_.getImageUrl_())));
    }
/*
    @Test
    public void canAccessToChatButtonIfOtherProfile() {
        onView(withText(oUser_.getName_())).perform(click());

    }

    @Test
    public void cantAccessToChatButtonIfMyProfile() {
        onView(withText(mUser_.getName_())).perform(click());

    }*/
}
