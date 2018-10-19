package ch.epfl.swissteam.services;

import java.util.ArrayList;

public class TestUtils {
    public static User getATestUser(){
        ArrayList<Categories> cat = new ArrayList<>();
        cat.add(Categories.IC);
        User testUser = new User("1234", "testuser", "test@gmail.com", "I am a test user", cat);
        return testUser;
    }

    public static Post getATestPost(){
        Post testPost = new Post("1234_1539885363773", "TestTitle", "1234", "TestBody", 1539885363773l);
        return testPost;
    }


    public static void setMock(){DBUtility.get().getDb_().getDatabase().goOffline();
    }

}

