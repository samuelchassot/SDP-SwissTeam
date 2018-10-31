package ch.epfl.swissteam.services;

import android.content.Context;
import android.net.wifi.WifiManager;
import android.os.SystemClock;
import android.support.test.rule.ActivityTestRule;
import android.support.test.rule.GrantPermissionRule;
import android.support.test.runner.AndroidJUnit4;
import android.telephony.TelephonyManager;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;



@RunWith(AndroidJUnit4.class)
public class NoNetworkTest {

    @Rule
    public final ActivityTestRule<MainActivity> mainActivityRule_ =
            new ActivityTestRule<>(MainActivity.class);

    //@Rule
    //public GrantPermissionRule gpr = GrantPermissionRule.grant("android.permission.MODIFY_PHONE_STATE");

    //@Rule
    //public GrantPermissionRule gpr2 = GrantPermissionRule.grant("android.permission.CHANGE_WIFI_STATE");

    @Before
    public void initialize() {
        LocationManager.get().setMock();
    }

    @After
    public void terminate() {
        LocationManager.get().unsetMock();
    }

    @Test
    public void cutNetwork() {
        WifiManager wifi = (WifiManager) mainActivityRule_.getActivity().getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        //wifi.setWifiEnabled(false);
        TelephonyManager tm = (TelephonyManager) mainActivityRule_.getActivity().getApplicationContext().getSystemService(Context.TELEPHONY_SERVICE);
        //tm.setDataEnabled(false);
    }
}
