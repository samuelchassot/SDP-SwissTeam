package ch.epfl.swissteam.services;

import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;

@RunWith(AndroidJUnit4.class)
public class DBUtilityTest {

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
}
