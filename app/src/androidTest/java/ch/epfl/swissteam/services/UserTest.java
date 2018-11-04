package ch.epfl.swissteam.services;

import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;

@RunWith(AndroidJUnit4.class)
public class UserTest {

    public static String googleID = "1337", email = "a@b.c", name = "Jean", description = "45 ans.", imageUrl = "www.com";
    public static String googleID2 = "1frr", email2 = "b@a.c", name2 = "Jeanne", description2 = "45 ans.";
    public static String googleID3 = "145r", email3 = "d@a.c", name3 = "Jeannette", description3 = "45 ans.";
    public int rating = 42;

    public static ArrayList<Categories> categories = new ArrayList<Categories>();

    @Before
    public void setCats() {
        categories.add(Categories.COOKING);
        categories.add(Categories.DAILYLIFE);
    }

    @Test
    public void testUserWorks() {
        User user = new User(googleID, name,email, description, categories, null, imageUrl,rating, 0, 0);
        assertEquals(googleID, user.getGoogleId_());
        assertEquals(email, user.getEmail_());
        assertEquals(name, user.getName_());
        assertEquals(description, user.getDescription_());
        assertEquals(categories, user.getCategories_());
        assertEquals(imageUrl, user.getImageUrl_());
        assertEquals(rating, user.getRating_());
        assertEquals(true, user.equals(user));
    }

    @Test
    public void upDownVoteTest(){
        User user = new User(googleID, name,email, description, categories, null, imageUrl, rating, 0, 0);
        user.upvote();
        assertEquals(rating +1, user.getRating_());
        user.downvote();
        assertEquals(rating, user.getRating_());


    }

    @Test
    public void setAndGetChatRelationWorks() {
        User user1 = new User(googleID, name, email, description, categories, null, imageUrl,rating, 0, 0);
        User user2 = new User(googleID2, name2,  email2, description2, categories, null, imageUrl,0,0,0);
        String id = "aksdjh287364ksdjbf";
        ChatRelation cR = new ChatRelation(user1, user2);
        cR.setId_(id);
        user1.addChatRelation(cR);
        assertEquals(1, user1.getChatRelations_().size());
        assertEquals(cR, user1.getChatRelations_().get(0));
    }

    @Test
    public void relationExistsWorksForInexistentRelation(){
        User user1 = new User(googleID, name, email, description, categories, null, imageUrl,rating, 0, 0);
        User user2 = new User(googleID2, name2,  email2, description2, categories, null, imageUrl,rating, 0, 0);
        User user3 = new User(googleID3, name3,  email3, description3, categories, null, imageUrl,rating, 0, 0);
        ChatRelation cR = new ChatRelation(user1, user2);
        user1.addChatRelation(cR);
        user2.addChatRelation(cR);
        assertEquals(null, user1.relationExists(user3));
    }

    @Test
    public void relationExistsWorksForExistentRelation(){
        User user1 = new User(googleID, name, email, description, categories, null, imageUrl,rating, 0, 0);
        User user2 = new User(googleID2, name2, email2, description2, categories, null, imageUrl,rating, 0, 0);
        User user2bis = new User("1frr", name2, email2, description2, categories, null, imageUrl,rating, 0, 0);
        ChatRelation cR = new ChatRelation(user1, user2);
        user1.addChatRelation(cR);
        user2.addChatRelation(cR);
        assertEquals(cR, user1.relationExists(user2bis));
    }

    @Test
    public void idRelationExistsWorksForInexistentRelation(){
        User user1 = new User(googleID, name, email, description, categories, null, imageUrl,0,0,0);
        User user2 = new User(googleID2, name2,  email2, description2, categories, null, imageUrl,0,0,0);
        User user3 = new User(googleID3, name3,  email3, description3, categories, null, imageUrl,0,0,0);
        ChatRelation cR = new ChatRelation(user1, user2);
        user1.addChatRelation(cR);
        user2.addChatRelation(cR);
        assertEquals(null, user1.relationExists(googleID3));
    }

    @Test
    public void idRelationExistsWorksForExistentRelation(){
        User user1 = new User(googleID, name, email, description, categories, null, imageUrl,0,0,0);
        User user2 = new User(googleID2, name2, email2, description2, categories, null, imageUrl,0,0,0);
        ChatRelation cR = new ChatRelation(user1, user2);
        user1.addChatRelation(cR);
        user2.addChatRelation(cR);
        assertEquals(cR, user1.relationExists(googleID2));
    }
}
