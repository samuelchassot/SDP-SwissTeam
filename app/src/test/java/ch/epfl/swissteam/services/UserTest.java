package ch.epfl.swissteam.services;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;

public class UserTest {

    public static String googleID = "1337", email = "a@b.c", name = "Jean", description = "45 ans.";
    public static ArrayList<Categories> categories = new ArrayList<Categories>();

    @Before
    public void setCats() {
        categories.add(Categories.COOKING);
        categories.add(Categories.DAILYLIFE);
    }

    @Test
    public void testUserWorks() {
        User user = new User(googleID, name, email, description, categories);
        assertEquals(user.getGoogleId_(), googleID);
        assertEquals(user.getEmail_(), email);
        assertEquals(user.getName_(), name);
        assertEquals(user.getDescription_(), description);
        assertEquals(user.getCategories_(), categories);
    }
}
