package ch.epfl.swissteam.services;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

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
