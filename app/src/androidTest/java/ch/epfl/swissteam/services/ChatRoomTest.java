package ch.epfl.swissteam.services;

import android.support.test.espresso.intent.rule.IntentsTestRule;

import org.junit.Rule;
import org.junit.Test;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

public class ChatRoomTest {
    @Rule
    public final IntentsTestRule<ChatRoom> mActivityRule =
            new IntentsTestRule<>(ChatRoom.class);

    @Test
    public void sendMessage() {
        String text = "ablablajsgdaliu";
        onView(withId(R.id.message_input)).perform(typeText(text)).check(matches(withText(text)));
    }
}
