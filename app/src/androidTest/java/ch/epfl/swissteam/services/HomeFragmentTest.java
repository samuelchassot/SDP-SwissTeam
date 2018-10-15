package ch.epfl.swissteam.services;

import android.support.test.espresso.contrib.DrawerActions;
import android.support.test.espresso.contrib.NavigationViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.widget.ListView;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.swipeDown;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.junit.Assert.assertThat;
import static org.hamcrest.CoreMatchers.is;

@RunWith(AndroidJUnit4.class)
public class HomeFragmentTest {

    private ArrayList<Post> postsList = new ArrayList<>();
    private final int POSTS_DISPLAY_NUMBER = 20;

    @Rule
    public final ActivityTestRule<MainActivity> mActivityRule =
            new ActivityTestRule<>(MainActivity.class);

    @Test
    public void canOpenCreatePostFragment() {
        onView(withId(R.id.drawer_layout)).perform(DrawerActions.open());
        onView(withId(R.id.nav_view)).perform(NavigationViewActions.navigateTo(R.id.button_maindrawer_home));
    }

    @Test
    public void displayOneMessageOnCreate(){
        ListView listview = mActivityRule.getActivity().findViewById(R.id.listview_homefragment_postslist);

        assertThat(listview.getCount(), is(1));
    }

    @Test
    public void displaysCorrectMessageOnCreate(){
        ListView listview = mActivityRule.getActivity().findViewById(R.id.listview_homefragment_postslist);

        assertThat((String)listview.getItemAtPosition(0), is("Refresh for the latest posts!"));
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
    public void correctNumberOfPosts() {
        DBUtility.get().getPostsFeed(new MyCallBack<ArrayList<Post>>() {
            @Override
            public void onCallBack(ArrayList<Post> value) {
                postsList.clear();
                postsList.addAll(value);
            }
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
    public void displaysPostsCorrectly(){
        DBUtility.get().getPostsFeed(new MyCallBack<ArrayList<Post>>() {
            @Override
            public void onCallBack(ArrayList<Post> value) {
                postsList.clear();
                postsList.addAll(value);
            }
        });

        onView(withId(R.id.button_homefragment_refresh)).perform(click());

        ListView listview = mActivityRule .getActivity().findViewById(R.id.listview_homefragment_postslist);
        if(postsList.isEmpty()){
            assertThat((String)listview.getItemAtPosition(0), is(mActivityRule.getActivity().getResources().getString(R.string.homefragment_noposts)));
        }else{
            int i = 0;
            for(Post p : postsList){
                assertThat((String)listview.getItemAtPosition(i), is(p.getTitle_() + "\n" + p.getBody_()));
                i += 1;
            }
        }


    }

}
