package ch.epfl.swissteam.services;


import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;


@RunWith(AndroidJUnit4.class)
public class MainActivityTest {
    @Rule
    public final ActivityTestRule<MainActivity> mainActivityRule_ =
            new ActivityTestRule<>(MainActivity.class);

    @Test
    public void test() {
        // Test for the MainActivity ...
    }
}
