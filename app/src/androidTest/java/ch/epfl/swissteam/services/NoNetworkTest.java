package ch.epfl.swissteam.services;

import android.content.Context;
import android.net.wifi.WifiManager;
import android.support.test.runner.AndroidJUnit4;
import android.telephony.TelephonyManager;

import org.junit.Test;
import org.junit.runner.RunWith;

import ch.epfl.swissteam.services.view.activities.MainActivity;


@RunWith(AndroidJUnit4.class)
public class NoNetworkTest extends SocializeTest<MainActivity> {

    public NoNetworkTest(){
        setTestRule(MainActivity.class);
    }

    @Test
    public void cutNetwork() {
        WifiManager wifi = (WifiManager) testRule_.getActivity().getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        //wifi.setWifiEnabled(false);
        TelephonyManager tm = (TelephonyManager) testRule_.getActivity().getApplicationContext().getSystemService(Context.TELEPHONY_SERVICE);
        //tm.setDataEnabled(false);
    }
}
