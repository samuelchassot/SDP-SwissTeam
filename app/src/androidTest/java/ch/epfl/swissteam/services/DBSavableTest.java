package ch.epfl.swissteam.services;

import android.support.test.runner.AndroidJUnit4;

import com.google.firebase.database.DatabaseReference;

import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class DBSavableTest {
    private class TestSavable implements DBSavable{

        @Override
        public void addToDB(DatabaseReference databaseReference) {
        }
    }

    @Test(expected = Utility.IllegalCallException.class)
    public void removeFromDbThrowsIfNotImplemented() throws Utility.IllegalCallException {
        (new TestSavable()).removeFromDB(null);
    }

    @Test(expected = Utility.IllegalCallException.class)
    public void removeFromDbWithChildNonNullThrowsWhenRemoveFromDbNotImpl() throws Utility.IllegalCallException {
        (new TestSavable()).removeFromDB(null, "");
    }

    @Test(expected = Utility.IllegalCallException.class)
    public void removeFromDbWithChildNullThrowsWhenRemoveFromDbNotImpl() throws Utility.IllegalCallException {
        (new TestSavable()).removeFromDB(null, null);
    }
}
