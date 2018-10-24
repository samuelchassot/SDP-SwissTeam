package ch.epfl.swissteam.services;

import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class CategoriesTest extends FirebaseTest{

    @Test
    public void testFromString() {
        Categories.fromString("Computer");
        Categories.fromString("Maths");
        Categories.fromString("House");
        Categories.fromString("Cooking");
        Categories.fromString("Teaching");
        Categories.fromString("Gardening");
        Categories.fromString("Mechanics");
        Categories.fromString("Daily Life");
        Categories.fromString("Transportation");
        Categories.fromString("All");
    }

    @Test
    public void addToDbTest() {
        Categories.fromString("Computer").addToDB(DBUtility.get().getDb_());
        Categories.fromString("All").addToDB(DBUtility.get().getDb_());
    }
}
