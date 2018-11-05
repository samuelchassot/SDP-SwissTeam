package ch.epfl.swissteam.services;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class NotificationUtilsTest extends FirebaseTest {

    @Override
    public void initialize() {
        LocationManager.get().setMock();
    }

    @Override
    public void terminate() {
        LocationManager.get().unsetMock();
    }

    @Rule
    public final ActivityTestRule<MainActivity> mainActivityRule_ =
            new ActivityTestRule<>(MainActivity.class);

    @Test
    public void testSendCustomNotification() {
        NotificationUtils.sendChatNotification(mainActivityRule_.getActivity(), "Hello", "World", "1234");
    }
}
