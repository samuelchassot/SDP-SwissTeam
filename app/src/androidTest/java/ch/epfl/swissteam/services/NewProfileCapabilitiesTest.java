package ch.epfl.swissteam.services;

import android.support.test.espresso.intent.rule.IntentsTestRule;

import org.junit.Rule;
import org.junit.Test;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;

public class NewProfileCapabilitiesTest {
    @Rule
    public final IntentsTestRule<NewProfileDetails> mActivityRule =
            new IntentsTestRule<>(NewProfileDetails.class);

    @Test
    public void noNewCapabilitySelector() {
        onData(allOf(is(instanceOf(CapabilitySelection.class))));
    }

    @Test
    public void createNewCapabilitySelector() {
        onData(allOf(is(instanceOf(CapabilitySelection.class))));
    }
}
