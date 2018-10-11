package ch.epfl.swissteam.services;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class UserTest {

    public static String googleID = "1337", email = "a@b.c", name = "Jean", surname = "Claude", description = "45 ans.";
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
        User user = new User(googleID, name, surname,email, description, categories);
        String id = "aksdjh287364ksdjbf";
        ChatRelation cR = new ChatRelation(name, surname);
        cR.setId_(id);
        user.addChatRelation(cR);
        assertEquals(1, user.getChatRelationIds_().size());
        assertEquals(id, user.getChatRelationIds_().get(0));
    }
}
