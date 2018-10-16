package ch.epfl.swissteam.services;

import android.support.test.espresso.contrib.DrawerActions;
import android.support.test.espresso.contrib.NavigationViewActions;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.v7.widget.RecyclerView;
import android.widget.ListView;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.swipeRight;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

@RunWith(AndroidJUnit4.class)
public class MyPostFragmentTest {
    private ArrayList<Post> posts;
    private String id;

    @Rule
    public final ActivityTestRule<MainActivity> mActivityRule =
            new ActivityTestRule<>(MainActivity.class);

    @Before
    public void initialize(){
        id = "1234";
        GoogleSignInSingleton.putUniqueID(id);
        posts = new ArrayList<>();
    }

    @Test
    public void canOpenMyPostsFragment() {
        onView(withId(R.id.drawer_layout)).perform(DrawerActions.open());
        onView(withId(R.id.nav_view)).perform(NavigationViewActions.navigateTo(R.id.button_maindrawer_myposts));
    }

    @Test
    public void canSwipeRight(){
        DBUtility.get().getUsersPosts(id, new MyCallBack<ArrayList<Post>>() {
            @Override
            public void onCallBack(ArrayList<Post> value) {
                posts.clear();
                posts.addAll(value);
            }
        });

        onView(withId(R.id.drawer_layout)).perform(DrawerActions.open());
        onView(withId(R.id.nav_view)).perform(NavigationViewActions.navigateTo(R.id.button_maindrawer_myposts));

        if(!posts.isEmpty()){
            onView(withId(R.id.recyclerview_mypostsfragment)).perform(RecyclerViewActions.actionOnItemAtPosition(0,swipeRight()));
        }
    }

    @Test
    public void canDelete(){
        DBUtility.get().getUsersPosts(id, new MyCallBack<ArrayList<Post>>() {
            @Override
            public void onCallBack(ArrayList<Post> value) {
                posts.clear();
                posts.addAll(value);
            }
        });

        onView(withId(R.id.drawer_layout)).perform(DrawerActions.open());
        onView(withId(R.id.nav_view)).perform(NavigationViewActions.navigateTo(R.id.button_maindrawer_myposts));

        if(!posts.isEmpty()){
            onView(withId(R.id.recyclerview_mypostsfragment)).perform(RecyclerViewActions.actionOnItemAtPosition(0,swipeRight()));
            onView(withId(R.id.button_postadapter_delete)).perform(click());
        }
    }

    @Test
    public void canEdit(){
        DBUtility.get().getUsersPosts(id, new MyCallBack<ArrayList<Post>>() {
            @Override
            public void onCallBack(ArrayList<Post> value) {
                posts.clear();
                posts.addAll(value);
            }
        });

        onView(withId(R.id.drawer_layout)).perform(DrawerActions.open());
        onView(withId(R.id.nav_view)).perform(NavigationViewActions.navigateTo(R.id.button_maindrawer_myposts));

        if(!posts.isEmpty()){
            onView(withId(R.id.recyclerview_mypostsfragment)).perform(RecyclerViewActions.actionOnItemAtPosition(0,swipeRight()));
            onView(withId(R.id.button_postadapter_edit)).perform(click());
            onView(withId(R.id.edittext_mypostedit_title)).check(matches(withText("Title")));
            onView(withId(R.id.edittext_mypostedit_body)).check(matches(withText("Body")));
            onView(withId(R.id.edittext_mypostedit_title)).perform(typeText("Title from unit test")).perform(closeSoftKeyboard());
            onView(withId(R.id.edittext_mypostedit_body)).perform(typeText("Body from unit test")).perform(closeSoftKeyboard());
            onView(withId(R.id.button_mypostedit_edit)).perform(click());
        }
    }


}