package ch.epfl.swissteam.services;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class UserTest {

    public static String googleID = "1337", email = "a@b.c", name = "Jean", surname = "Claude", description = "45 ans.";
    public static String googleID2 = "1frr", email2 = "b@a.c", name2 = "Jeanne", surname2 = "Claudette", description2 = "45 ans.";
    public static ArrayList<String> categories = new ArrayList<String>();

    @Before
    public void setCats() {
        categories.add("IT");
        categories.add("jardinage");
    }

    @Test
    public void testUserWorks() {
        User user = new User(googleID, name, surname,email, description, categories);
        assertEquals(googleID, user.getGoogleId_());
        assertEquals(email, user.getEmail_());
        assertEquals(name, user.getName_());
        assertEquals(surname, user.getSurname_());
        assertEquals(description, user.getDescription_());
        assertEquals(categories, user.getCategories_());
    }

    @Test
    public void setAndGetChatRelationWorks() {
        User user1 = new User(googleID, name, surname,email, description, categories);
        User user2 = new User(googleID2, name2, surname2, email2, description2, categories);
        String id = "aksdjh287364ksdjbf";
        ChatRelation cR = new ChatRelation(user1, user2);
        cR.setId_(id);
        user1.addChatRelation(cR);
        assertEquals(1, user1.getChatRelations_().size());
        assertEquals(cR, user1.getChatRelations_().get(0));
    }
}
