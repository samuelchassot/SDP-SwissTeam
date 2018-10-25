package ch.epfl.swissteam.services;

import android.support.test.espresso.ViewAction;
import android.support.test.espresso.ViewInteraction;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.view.View;
import android.view.ViewGroup;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

import java.util.ArrayList;
import java.util.Date;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.matcher.ViewMatchers.hasDescendant;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

public class TestUtils {
    public static User getATestUser(){
        ArrayList<Categories> cat = new ArrayList<>();
        cat.add(Categories.IC);
        User testUser = new User("1234", "testuser", "test@gmail.com", "I am a test user", cat, "https://lh5.googleusercontent.com/-SYTkc6TIZHI/AAAAAAAAAAI/AAAAAAAAABc/EBrA4sSVWQc/photo.jpg");
        return testUser;
    }

    public static void addTestPost() {
        long timestamp = (new Date()).getTime();
        String key = "1234" + "_" + timestamp;
        DBUtility.get().setPost(new Post(key, "Hello there", "1234", "General Kenobi", timestamp));


    }

    public static Post getTestPost() {
        long timestamp = (new Date()).getTime();
        String key = "1234" + "_" + timestamp;
        return new Post(key, "Hello there", "1234", "General Kenobi", timestamp);
    }

    public static void sleep(int ms){
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static Matcher<View> nthChildOf(final Matcher<View> parentMatcher, final int childPosition) {
        return new TypeSafeMatcher<View>() {
            @Override
            public void describeTo(Description description) {
                description.appendText("with "+childPosition+" child view of type parentMatcher");
            }

            @Override
            public boolean matchesSafely(View view) {
                if (!(view.getParent() instanceof ViewGroup)) {
                    return parentMatcher.matches(view.getParent());
                }

                ViewGroup group = (ViewGroup) view.getParent();
                return parentMatcher.matches(view.getParent()) && group.getChildAt(childPosition).equals(view);
            }
        };
    }




    public static void setMock(){
        DBUtility.get().getDb_().getDatabase().goOffline();
    }

    public static ViewInteraction recyclerScrollToItemWithTextAndPerformOnItem(int recyclerViewId, String text, ViewAction perform){
        return onView(withId(recyclerViewId)).
                perform(RecyclerViewActions.scrollTo(hasDescendant(withText(text)))).
                perform(RecyclerViewActions.actionOnItem(hasDescendant(withText(text)), perform));
    }

    public static ViewInteraction recyclerScrollToItemWithTextAndPerformClickItem(int recyclerViewId, String text){
        return recyclerScrollToItemWithTextAndPerformOnItem(recyclerViewId, text, click());
    }

}

