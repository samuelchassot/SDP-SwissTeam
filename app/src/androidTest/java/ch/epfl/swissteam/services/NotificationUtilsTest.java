package ch.epfl.swissteam.services;

import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import ch.epfl.swissteam.services.utils.NotificationUtils;
import ch.epfl.swissteam.services.view.activities.MainActivity;

@RunWith(AndroidJUnit4.class)
public class NotificationUtilsTest extends SocializeTest<MainActivity> {

    public NotificationUtilsTest(){
        setTestRule(MainActivity.class);
    }

    @Test
    public void testSendCustomNotification() {
        NotificationUtils.sendChatNotification(testRule_.getActivity(), "Hello", "World", "1234");
    }
}
