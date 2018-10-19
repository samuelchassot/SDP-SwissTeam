package ch.epfl.swissteam.services;

import java.util.ArrayList;
import java.util.Date;

public class TestUtils {
    public static User getATestUser(){
        ArrayList<Categories> cat = new ArrayList<>();
        cat.add(Categories.IC);
        User testUser = new User("1234", "testuser", "test@gmail.com", "I am a test user", cat, "https://lh5.googleusercontent.com/-SYTkc6TIZHI/AAAAAAAAAAI/AAAAAAAAABc/EBrA4sSVWQc/photo.jpg");
        return testUser;
    }

    public static void addTestPost() {
        long timestamp = (new Date()).getTime();
        String key = "1234" + "_" + timestamp;
        DBUtility.get().setPost(new Post(key, "Hello there", "1234", "General Kenobi", timestamp));

    }
    public static Post getTestPost() {
        long timestamp = (new Date()).getTime();
        String key = "1234" + "_" + timestamp;
        return new Post(key, "Hello there", "1234", "General Kenobi", timestamp);
    }


    public static void setMock(){
        DBUtility.get().getDb_().getDatabase().goOffline();
    }

}

