package ch.epfl.swissteam.services;

import android.support.test.espresso.intent.rule.IntentsTestRule;

import org.junit.Rule;
import org.junit.Test;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.Intents.intending;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;

public class NewProfileCapabilitiesTest {
    @Rule
    public final IntentsTestRule<NewProfileCapabilities> mActivityRule =
            new IntentsTestRule<>(NewProfileCapabilities.class);

    @Test
    public void nextPageWorks() {
        onView(withId(R.id.button_newprofilecapabilites_done)).perform(click());
        intended(hasComponent(MainActivity.class.getName()));
    }

    @Test
    public void addNewCapabilityWorks() {
        onView(withId(R.id.button_newprofilecapabilities_newservice)).perform(click());
    }
}
