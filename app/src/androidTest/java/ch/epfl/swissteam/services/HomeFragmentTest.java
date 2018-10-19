package ch.epfl.swissteam.services;

import android.support.test.espresso.contrib.DrawerActions;
import android.support.test.espresso.contrib.NavigationViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.widget.ListView;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.swipeDown;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.junit.Assert.assertThat;
import static org.hamcrest.CoreMatchers.is;

@RunWith(AndroidJUnit4.class)
public class HomeFragmentTest extends FirebaseTest{

    private ArrayList<Post> postsList = new ArrayList<>();
    private final int POSTS_DISPLAY_NUMBER = 20;

    @Rule
    public final ActivityTestRule<MainActivity> mActivityRule =
            new ActivityTestRule<>(MainActivity.class);

    @Override
    public void initialize() {
        TestUtils.addTestPost();
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
    public void displayCorrectNumberOfPostsOnCreate(){
        DBUtility.get().getPostsFeed(value -> {
            postsList.clear();
            postsList.addAll(value);
        });

        ListView listview = mActivityRule.getActivity().findViewById(R.id.listview_homefragment_postslist);

        try {
            Thread.sleep(300);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        if (postsList.isEmpty()){
            assertThat(listview.getCount(), is(1));
        }else{
            assertThat(listview.getCount(), is(Math.min(postsList.size(), POSTS_DISPLAY_NUMBER)));
        }
    }

    @Test
    public void displaysPostsCorrectlyOnCreate(){
        ListView listview = mActivityRule.getActivity().findViewById(R.id.listview_homefragment_postslist);

        DBUtility.get().getPostsFeed(value -> {
            postsList.clear();
            postsList.addAll(value);
        });

        try {
            Thread.sleep(300);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        if(postsList.isEmpty()){
            assertThat(listview.getItemAtPosition(0), is(mActivityRule.getActivity().getResources().getString(R.string.homefragment_noposts)));
        }else{
            int i = 0;
            for(Post p : postsList){
                assertThat(listview.getItemAtPosition(i), is(p.getTitle_() + "\n" + p.getBody_()));
                i += 1;
            }
        }
    }

    @Test
    public void correctNumberOfPostsOnClick() {
        DBUtility.get().getPostsFeed(value -> {
            postsList.clear();
            postsList.addAll(value);
        });

        onView(withId(R.id.button_homefragment_refresh)).perform(click());

        ListView listview = mActivityRule.getActivity().findViewById(R.id.listview_homefragment_postslist);
        if (postsList.isEmpty()){
            assertThat(listview.getCount(), is(1));
        }else{
            assertThat(listview.getCount(), is(Math.min(postsList.size(), POSTS_DISPLAY_NUMBER)));
        }

    }

    @Test
    public void displaysPostsCorrectlyOnClick(){
        DBUtility.get().getPostsFeed(value -> {
            postsList.clear();
            postsList.addAll(value);
        });

        onView(withId(R.id.button_homefragment_refresh)).perform(click());

        ListView listview = mActivityRule .getActivity().findViewById(R.id.listview_homefragment_postslist);
        if(postsList.isEmpty()){
            assertThat(listview.getItemAtPosition(0), is(mActivityRule.getActivity().getResources().getString(R.string.homefragment_noposts)));
        }else{
            int i = 0;
            for(Post p : postsList){
                assertThat(listview.getItemAtPosition(i), is(p.getTitle_() + "\n" + p.getBody_()));
                i += 1;
            }
        }
    }

    @Test
    public void correctNumberOfPostsOnSwipeDown() {
        DBUtility.get().getPostsFeed(value -> {
            postsList.clear();
            postsList.addAll(value);
        });

        onView(withId(R.id.swiperefresh_homefragment_refresh)).perform(swipeDown());

        ListView listview = mActivityRule.getActivity().findViewById(R.id.listview_homefragment_postslist);
        if (postsList.isEmpty()){
            assertThat(listview.getCount(), is(1));
        }else{
            assertThat(listview.getCount(), is(Math.min(postsList.size(), POSTS_DISPLAY_NUMBER)));
        }

    }

    @Test
    public void displaysPostsCorrectlyOnSwipeDown(){
        DBUtility.get().getPostsFeed(value -> {
            postsList.clear();
            postsList.addAll(value);
        });

        onView(withId(R.id.swiperefresh_homefragment_refresh)).perform(swipeDown());

        ListView listview = mActivityRule .getActivity().findViewById(R.id.listview_homefragment_postslist);
        if(postsList.isEmpty()){
            assertThat(listview.getItemAtPosition(0), is(mActivityRule.getActivity().getResources().getString(R.string.homefragment_noposts)));
        }else{
            int i = 0;
            for(Post p : postsList){
                assertThat(listview.getItemAtPosition(i), is(p.getTitle_() + "\n" + p.getBody_()));
                i += 1;
            }
        }
    }

}
