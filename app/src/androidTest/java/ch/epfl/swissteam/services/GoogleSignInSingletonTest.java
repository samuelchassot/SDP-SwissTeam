package ch.epfl.swissteam.services;

import android.Manifest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.rule.GrantPermissionRule;

import org.junit.Rule;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class GoogleSignInSingletonTest extends FirebaseTest {

    private User testUser = TestUtils.getTestUser();

    @Rule
    public final ActivityTestRule<MainActivity> mActivityRule =
            new ActivityTestRule<>(MainActivity.class);
    @Rule
    public GrantPermissionRule mRuntimePermissionRule =
                GrantPermissionRule.grant(android.Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION);

    @Override
    public void initialize() {
        super.initialize();
        testUser.addToDB(DBUtility.get().getDb_());
        GoogleSignInSingleton.putUniqueID(testUser.getGoogleId_());
    }

    @Test
    public void getLocationInSingletonAndCheckNotNull(){
        GoogleSignInSingleton.putCurrentLocation(mActivityRule.getActivity());
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        assertTrue((GoogleSignInSingleton.get().getLastLocation() != null));
    }

    @Test
    public void getLocationAndPutInDB(){
        GoogleSignInSingleton.putCurrentLocation(mActivityRule.getActivity());
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        GoogleSignInSingleton.updateLastLocationUserInDB();
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        DBUtility.get().getUser(GoogleSignInSingleton.get().getClientUniqueID(), (u)->{
            assertTrue(u != null);
            if(u!= null){
                assertTrue(u.getLatitude_() == GoogleSignInSingleton.get().getLastLocation().getLatitude());
                assertTrue(u.getLongitude_() == GoogleSignInSingleton.get().getLastLocation().getLongitude());
            }
        });
    }


}
