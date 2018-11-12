package ch.epfl.swissteam.services;

import android.content.Intent;
import android.support.test.espresso.Espresso;
import android.support.test.espresso.contrib.DrawerActions;
import android.support.test.espresso.contrib.NavigationViewActions;
import android.support.test.rule.ActivityTestRule;

import com.google.firebase.database.FirebaseDatabase;

import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.longClick;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.doesNotExist;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.hasDescendant;
import static android.support.test.espresso.matcher.ViewMatchers.isClickable;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.isEnabled;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static ch.epfl.swissteam.services.NewProfileDetails.GOOGLE_ID_TAG;
import static ch.epfl.swissteam.services.TestUtils.O_USER;
import static ch.epfl.swissteam.services.TestUtils.personalClick;
import static ch.epfl.swissteam.services.TestUtils.recyclerScrollToItemWithTextAndPerformClickItem;
import static ch.epfl.swissteam.services.TestUtils.sleep;
import static org.hamcrest.CoreMatchers.allOf;

/**
 * Tests for ChatRoom
 *
 * @author Sébastien Gachoud
 */
@RunWith(AndroidJUnit4.class)
public class ChatRoomTest extends FirebaseTest{

    @Rule
    public final ActivityTestRule<ChatRoom> mActivityRule =
            new ActivityTestRule<>(ChatRoom.class, true, false);

    @Override
    public void initialize(){
        GoogleSignInSingleton.putUniqueID(TestUtils.M_GOOGLE_ID);
        TestUtils.O_USER.addToDB(DBUtility.get().getDb_());
        TestUtils.M_USER.addToDB(DBUtility.get().getDb_());
        sleep(100);
        Intent intent = new Intent();
        intent.putExtra(GOOGLE_ID_TAG, O_USER.getGoogleId_());
        mActivityRule.launchActivity(intent);
    }

    @Test
    public void sendMessageWorksWithNonEmpty() {
        String text = "Le roi est mort ! Vive le roi !";
        sendMessage(text);
        onView(withId(R.id.recycler_view_message)).check(matches(hasDescendant(withText(text))));
    }

    @Test
    public void canChooseToDeleteMessage() {
        String text = "I can be deleted";
        sendMessage(text);
        onView(withText(text)).perform(longClick());
        sleep(100);
        onView(withText(mActivityRule.getActivity().getResources().getString(R.string.chat_delete_alert_text)))
                .check(matches(isDisplayed()));

    }

    @Test
    public void canClickDeleteMessage() {
        String text = "I can be deleted";
        sendMessage(text);
        deleteMessageWithText(text);
        onView(withText(text)).check(doesNotExist());

    }

    @Test
    public void canClickCancelDeleteMessage() {
        String text = "I can be deleted";
        sendMessage(text);
        onView(withText(text)).perform(longClick());
        sleep(100);
        onView(withText(mActivityRule.getActivity().getResources().getString(R.string.general_cancel))).perform(click());
        onView(withText(text)).check(matches(isDisplayed()));
    }

    @Test
    public void canDeleteMessagesInDisorder() {
        String text = "I can be deleted";
        String textToo = "I can be deleted too";
        sendMessage(text);
        sendMessage(textToo);
        deleteMessageWithText(textToo);
        deleteMessageWithText(text);
    }

    private void sendMessage(String text){
        Espresso.closeSoftKeyboard();
        sleep(100);
        onView(withId(R.id.message_input)).perform(typeText(text), closeSoftKeyboard()).check(matches(withText(text)));
        sleep(100);
        onView(withId(R.id.message_send_button)).perform(click());
        sleep(100);
    }

    private void deleteMessageWithText(String text){
        onView(withText(text)).perform(longClick());
        sleep(100);
        onView(withText(mActivityRule.getActivity().getResources().getString(R.string.general_delete))).perform(click());
    }

    /* Examples
        - onView(nthChildOf(withId(R.id.recycleview), 0).check(matches(hasDescendant(withText("Some text"))))
        - onView(withId(R.id.recycleview)).check(matches(hasDescendant(withText("Some text"))))
     */
}
