package ch.epfl.swissteam.services;

import android.content.Intent;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

@RunWith(AndroidJUnit4.class)
public class ChatRoomTest {
    @Rule
    public final IntentsTestRule<ChatRoom> mActivityRule =
            new IntentsTestRule<ChatRoom>(ChatRoom.class){
                @Override
                protected Intent getActivityIntent() {
                    /*added predefined intent data*/
                    Intent intent = new Intent();
                    intent.putExtra(ChatRelation.RELATION_ID_TEXT, "1234");
                    return intent;
                }
            };


    @Test
    public void sendMessage() {
        String text = "ablablajsgdaliu";
        onView(withId(R.id.message_input)).perform(typeText(text)).check(matches(withText(text)));
    }
}
