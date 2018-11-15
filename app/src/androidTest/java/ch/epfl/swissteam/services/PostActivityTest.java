package ch.epfl.swissteam.services;

import android.content.Intent;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.uiautomator.UiDevice;
import android.support.test.uiautomator.UiObject;
import android.support.test.uiautomator.UiSelector;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;

import org.junit.Rule;
import org.junit.Test;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.swipeDown;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static ch.epfl.swissteam.services.PostAdapter.POST_TAG;
import static ch.epfl.swissteam.services.TestUtils.sleep;
import static org.junit.Assert.assertEquals;

public class PostActivityTest extends FirebaseTest{

    private Post post;
    private User user;

    @Rule
    public final ActivityTestRule<PostActivity> mActivityRule =
            new ActivityTestRule<>(PostActivity.class, true, false);

    @Override
    public void initialize() {
        LocationManager.get().setMock();
        TestUtils.addTestPost();
        user = TestUtils.getTestUser();
        post = TestUtils.getTestPost();
        GoogleSignInSingleton.putUniqueID(user.getGoogleId_());
        post.addToDB(DBUtility.get().getDb_());
        user.addToDB(DBUtility.get().getDb_());
        sleep(400);
        startIntent();
    }

    @Override
    public void terminate() {
        LocationManager.get().unsetMock();
    }

    @Test
    public void infoAboutPostCorrespondToTheGivenPost(){
        onView(withId(R.id.textview_postactivity_title)).check(matches(withText(post.getTitle_())));
        onView(withId(R.id.textview_postactivity_body)).check(matches(withText(post.getBody_())));
        onView(withId(R.id.textview_postactivity_date)).check(matches(withText(
                (new SimpleDateFormat("dd/MM/yyyy", Locale.FRENCH)).format(new Date(post.getTimestamp_()).getTime()))));
        onView(withId(R.id.textview_postactivity_username)).check(matches(withText(user.getName_())));
    }

    @Test
    public void theLocationOfThePostOnTheMapCorrespondToThePost(){
        sleep(1000);
        Marker marker = mActivityRule.getActivity().getMarker();
        mActivityRule.getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                LatLng markerPos = marker.getPosition();
                assertEquals(markerPos.latitude, post.getLatitude_(), 0.01);
                assertEquals(markerPos.longitude, post.getLongitude_(), 0.01);
                assertEquals(marker.getTitle(), post.getTitle_());
            }
        });

    }

    private void startIntent(){
        Intent intent = new Intent();
        intent.putExtra(POST_TAG, post);
        mActivityRule.launchActivity(intent);
        sleep(500);
    }

}
