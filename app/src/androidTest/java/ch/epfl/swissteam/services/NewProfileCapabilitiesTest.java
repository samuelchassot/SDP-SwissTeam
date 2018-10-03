package ch.epfl.swissteam.services;

import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.widget.LinearLayout;

import org.junit.Rule;
import org.junit.Test;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

public class NewProfileCapabilitiesTest {
    @Rule
    public final IntentsTestRule<NewProfileDetails> mActivityRule =
            new IntentsTestRule<>(NewProfileDetails.class);

    @Test
    public void createNewCapabilitySelector() {
        /*onView(withId(R.id.new_capability_button)).perform(click());
        onView(allOf(is(instanceOf(CapabilitySelection.class)))).check()*/
        //LinearLayout ll = withId(R.id.main_scroll_view_layout);
    }
}
