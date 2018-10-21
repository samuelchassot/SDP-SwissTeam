package ch.epfl.swissteam.services;

import android.content.Intent;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.v7.widget.RecyclerView;

import com.google.firebase.database.FirebaseDatabase;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.hasDescendant;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

public class ChatRoomTest extends FirebaseTest{

    private static final String mGoogleId = "1234";
    private static final String oGoogleId = "5678";
    private static final User mUser = new User(mGoogleId,"Bear", "polar@north.nth","",null,"");
    private static final User oUser = new User(oGoogleId, "Raeb", "hairy@north.nth", "", null, "");

    @Rule
    public final IntentsTestRule<ChatRoom> mActivityRule =
            new IntentsTestRule<ChatRoom>(ChatRoom.class){
                @Override
                protected Intent getActivityIntent() {
                    /*added predefined intent data*/
                    Intent intent = new Intent();
                    intent.putExtra(NewProfileDetails.GOOGLE_ID_TAG, oGoogleId);
                    return intent;
                }
            };

    @Override
    public void initialize(){
        GoogleSignInSingleton.putUniqueID(mGoogleId);
        mUser.addToDB(DBUtility.get().getDb_());
        oUser.addToDB(DBUtility.get().getDb_());
    }

    @Test
    public void sendMessageWorksWithNonEmpty() {
        String text = "ablablajsgdaliu";
        onView(withId(R.id.message_input)).perform(typeText(text)).check(matches(withText(text)));
        onView(withId(R.id.message_send_button)).perform(click());
        //onView(withId(R.id.recycler_view_message)).check(matches(hasDescendant(withText(text))));

    }

    /* Examples
        - onView(nthChildOf(withId(R.id.recycleview), 0).check(matches(hasDescendant(withText("Some text"))))
        - onView(withId(R.id.recycleview)).check(matches(hasDescendant(withText("Some text"))))
     */
}
