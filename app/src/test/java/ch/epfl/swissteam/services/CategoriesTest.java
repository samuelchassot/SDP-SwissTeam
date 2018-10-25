package ch.epfl.swissteam.services;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class CategoriesTest {
    String googleId = "1234";
    User user1 = new User(googleId,"Jean-Marc Michel", "jmm@google.ch", "",null,"",0);


    @Test
    public void operationWithUserWorks(){
        Categories.HOUSE.addUser(user1);
        assertEquals(Arrays.asList(googleId), Categories.HOUSE.getUsers_());
        Categories.HOUSE.removeUser(user1);
        assertEquals(Arrays.asList(), Categories.HOUSE.getUsers_());
    }

    @Test
    public void operationUserWithIDworks(){
        Categories.HOUSE.addUser(googleId);
        assertEquals(Arrays.asList(googleId), Categories.HOUSE.getUsers_());
        Categories.HOUSE.removeUser(googleId);
        assertEquals(Arrays.asList(), Categories.HOUSE.getUsers_());
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
        assertEquals(null, Categories.fromString("Foo"));
    }

}
