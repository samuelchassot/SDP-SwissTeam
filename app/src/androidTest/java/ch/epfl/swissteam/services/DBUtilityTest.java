package ch.epfl.swissteam.services;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;

@RunWith(AndroidJUnit4.class)
public class DBUtilityTest extends FirebaseTest {

    User testUser;

    @Override
    public void initialize() {
        LocationManager.get().setMock();
        testUser = TestUtils.getTestUser();
        testUser.addToDB(DBUtility.get().getDb_());
    }

    @Override
    public void terminate() {
        LocationManager.get().unsetMock();
    }

    @Rule
    public final ActivityTestRule<MainActivity> mainActivityRule_ =
            new ActivityTestRule<>(MainActivity.class);

    @Test
    public void testSingleton() {
        assertEquals(DBUtility.get(), DBUtility.get());
    }

    @Test
    public void getCatTest(){
        DBUtility.get().getCategory(Categories.fromString("Computer"), c->{

        });
    }

    @Test
    public void getUserfromCatTest(){
        DBUtility.get().getUsersFromCategory(Categories.COOKING, c->{

        });
        DBUtility.get().getUsersFromCategory(Categories.ALL,c->{

        });
    }

    @Test
    public void testNotifyNewMessages() {
        DBUtility.get().notifyNewMessages(mainActivityRule_.getActivity(), testUser.getGoogleId_());
    }
}
