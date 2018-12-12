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
import java.util.Calendar;
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
public class NearbyFragmentTest extends SocializeTest{

    public NearbyFragmentTest(){
        setTestRule(MainActivity.class);
    }

    private Post post;
    private User user;
    private Post outDatedPost;

    @Override
    public void initialize() {
        TestUtils.addTestPost();
        user = TestUtils.getTestUser();
        post = TestUtils.getTestPost();
        Calendar cal  = Calendar.getInstance();
        cal.add(Calendar.MONTH, -1);
        long timestamp = (new Date()).getTime();
        String key = "1234" + "_" + timestamp;
        outDatedPost = new Post(key, "Hello there", "1234",
                "General Kenobi", timestamp, 0, 0, Post.dateToString(cal.getTime()));
        outDatedPost.addToDB(DBUtility.get().getDb_());
        post.addToDB(DBUtility.get().getDb_());
        user.addToDB(DBUtility.get().getDb_());
        sleep(400);
    }
        
    @Test
    public void canOpenHomeFragment() {
        onView(withId(R.id.drawer_layout)).perform(DrawerActions.open());
        onView(withId(R.id.nav_view)).perform(NavigationViewActions.navigateTo(R.id.button_maindrawer_home));
    }

    @Test
    public void canRefreshWithMenuItem() {
        onView(withId(R.id.action_refresh)).perform(click());
    }

    @Test
    public void canSwipeDown() {
        outDatedPost.addToDB(DBUtility.get().getDb_());
        sleep(1000);
        onView(withId(R.id.swiperefresh_homefragment_refresh)).perform(swipeDown());
    }

    @Test
    public void canClickOnPost(){
        onView(withId(R.id.swiperefresh_homefragment_refresh)).perform(swipeDown());
        sleep(300);
        onView(withId(R.id.recyclerview_homefragment_posts)).perform(RecyclerViewActions.actionOnItemAtPosition(0,click()));
    }

    @Test
    public void canClickOnPostAndThenComeBack(){
        onView(withId(R.id.swiperefresh_homefragment_refresh)).perform(swipeDown());
        sleep(300);
        onView(withId(R.id.recyclerview_homefragment_posts)).perform(RecyclerViewActions.actionOnItemAtPosition(0,click()));
        sleep(500);
        onView(TestUtils.navigationHomeMatcher()).perform(click());
    }

}
