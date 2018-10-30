package ch.epfl.swissteam.services;

import android.support.test.espresso.contrib.DrawerActions;
import android.support.test.espresso.contrib.NavigationViewActions;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.swipeDown;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static ch.epfl.swissteam.services.TestUtils.sleep;

@RunWith(AndroidJUnit4.class)
public class HomeFragmentTest extends FirebaseTest{

    private Post post;
    private User user;

    @Rule
    public final ActivityTestRule<MainActivity> mActivityRule =
            new ActivityTestRule<>(MainActivity.class);

    @Override
    public void initialize() {
        LocationManager.get().setMock();
        TestUtils.addTestPost();
        user = TestUtils.getATestUser();
        post = TestUtils.getTestPost();
        post.addToDB(DBUtility.get().getDb_());
        user.addToDB(DBUtility.get().getDb_());
        sleep(400);
    }

    @Override
    public void terminate() {
        LocationManager.get().unsetMock();
    }
        
    @Test
    public void canOpenHomeFragment() {
        onView(withId(R.id.drawer_layout)).perform(DrawerActions.open());
        onView(withId(R.id.nav_view)).perform(NavigationViewActions.navigateTo(R.id.button_maindrawer_home));
    }

    @Test
    public void canRefreshButton() {
        (mActivityRule.getActivity().findViewById(R.id.button_homefragment_refresh)).setOnClickListener(null);
        onView(withId(R.id.button_homefragment_refresh)).perform(click());
    }

    @Test
    public void canSwipeDown() {
        onView(withId(R.id.swiperefresh_homefragment_refresh)).perform(swipeDown());
    }

    @Test
    public void canClickOnPost(){
        onView(withId(R.id.swiperefresh_homefragment_refresh)).perform(swipeDown());
        sleep(300);
        onView(withId(R.id.recyclerview_homefragment_posts)).perform(RecyclerViewActions.actionOnItemAtPosition(0,click()));
    }

    @Test
    public void canClickOnPostAndSeeInfoAboutPost(){
        onView(withId(R.id.swiperefresh_homefragment_refresh)).perform(swipeDown());
        sleep(300);
        onView(withId(R.id.recyclerview_homefragment_posts)).perform(RecyclerViewActions.actionOnItemAtPosition(0,click()));
        sleep(500);
        onView(withId(R.id.textview_postactivity_title)).check(matches(withText(post.getTitle_())));
        onView(withId(R.id.textview_postactivity_body)).check(matches(withText(post.getBody_())));
        onView(withId(R.id.textview_postactivity_date)).check(matches(withText(
                (new SimpleDateFormat("dd/MM/yyyy", Locale.FRENCH)).format(new Date(post.getTimestamp_()).getTime()))));
        onView(withId(R.id.textview_postactivity_username)).check(matches(withText(user.getName_())));
    }
    

}
