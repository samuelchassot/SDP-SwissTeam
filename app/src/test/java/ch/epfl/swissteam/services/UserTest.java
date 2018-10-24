package ch.epfl.swissteam.services;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class UserTest {

    public static String googleID = "1337", email = "a@b.c", name = "Jean", description = "45 ans.", imageUrl = "www.com";
    public static String googleID2 = "1frr", email2 = "b@a.c", name2 = "Jeanne", description2 = "45 ans.";
    public static String googleID3 = "145r", email3 = "d@a.c", name3 = "Jeannette", description3 = "45 ans.";
    public static int rating = 69, rating2 = 24, rating3 = 42;

    public static ArrayList<Categories> categories = new ArrayList<Categories>();

    @Before
    public void setCats() {
        categories.add(Categories.COOKING);
        categories.add(Categories.DAILYLIFE);
    }

    @Test
    public void testUserWorks() {
        User user = new User(googleID, name,email, description, categories, imageUrl,rating);
        assertEquals(googleID, user.getGoogleId_());
        assertEquals(email, user.getEmail_());
        assertEquals(name, user.getName_());
        assertEquals(description, user.getDescription_());
        assertEquals(categories, user.getCategories_());
        assertEquals(imageUrl, user.getImageUrl_());
        assertEquals(rating, user.getRating_());
    }

    @Test
    public void setAndGetChatRelationWorks() {
        User user1 = new User(googleID, name, email, description, categories, imageUrl,rating);
        User user2 = new User(googleID2, name2,  email2, description2, categories, imageUrl,rating2);
        String id = "aksdjh287364ksdjbf";
        ChatRelation cR = new ChatRelation(user1, user2);
        cR.setId_(id);
        user1.addChatRelation(cR);
        assertEquals(1, user1.getChatRelations_().size());
        assertEquals(cR, user1.getChatRelations_().get(0));
    }

    @Test
    public void relationExistsWorksForInexistentRelation(){
        User user1 = new User(googleID, name, email, description, categories, imageUrl,rating);
        User user2 = new User(googleID2, name2,  email2, description2, categories, imageUrl,rating2);
        User user3 = new User(googleID3, name3,  email3, description3, categories, imageUrl,rating3);
        ChatRelation cR = new ChatRelation(user1, user2);
        user1.addChatRelation(cR);
        user2.addChatRelation(cR);
        assertEquals(null, user1.relationExists(user3));
    }

    @Test
    public void relationExistsWorksForExistentRelation(){
        User user1 = new User(googleID, name, email, description, categories, imageUrl,rating);
        User user2 = new User(googleID2, name2, email2, description2, categories, imageUrl, rating2);
        User user2bis = new User("1frr", name2, email2, description2, categories, imageUrl,rating3);
        ChatRelation cR = new ChatRelation(user1, user2);
        user1.addChatRelation(cR);
        user2.addChatRelation(cR);
        assertEquals(cR, user1.relationExists(user2bis));
    }
}
