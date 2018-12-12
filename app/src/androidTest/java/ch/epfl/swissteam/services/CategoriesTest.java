package ch.epfl.swissteam.services;

import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Collections;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

@RunWith(AndroidJUnit4.class)
public class CategoriesTest {
    private String googleId = "1234";
    private User user1 = new User(googleId,"Jean-Marc Michel", "jmm@google.ch", "",
            null,null,null,"",0, 0,0,null,null, false);


    @Test
    public void operationWithUserWorks(){
        Categories.HOUSE.addUser(user1);
        assertEquals(Collections.singletonList(googleId), Categories.HOUSE.getUsers_());
        Categories.HOUSE.removeUser(user1);
        assertEquals(Collections.emptyList(), Categories.HOUSE.getUsers_());
    }

    @Test
    public void operationUserWithIDWorks(){
        Categories.HOUSE.addUser(googleId);
        assertEquals(Collections.singletonList(googleId), Categories.HOUSE.getUsers_());
        Categories.HOUSE.removeUser(googleId);
        assertEquals(Collections.emptyList(), Categories.HOUSE.getUsers_());
    }
    

    @Test
    public void fromStringTest(){
        assertEquals(Categories.IC, Categories.fromString("Computer"));
        assertEquals(Categories.MATHS, Categories.fromString("Maths"));
        assertEquals(Categories.HOUSE, Categories.fromString("House"));
        assertEquals(Categories.COOKING, Categories.fromString("Cooking"));
        assertEquals(Categories.TEACHING, Categories.fromString("Teaching"));
        assertEquals(Categories.GARDENING, Categories.fromString("Gardening"));
        assertEquals(Categories.MECHANICS, Categories.fromString("Mechanics"));
        assertEquals(Categories.DAILYLIFE, Categories.fromString("Daily Life"));
        assertEquals(Categories.TRANSPORTATION, Categories.fromString("Transportation"));
        assertEquals(Categories.ALL, Categories.fromString("All"));
        assertNull(Categories.fromString("Foo"));
    }

}
