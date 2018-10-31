package ch.epfl.swissteam.services;

import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;

@RunWith(AndroidJUnit4.class)
public class PostTest {

    static String title = "Hello", id = "Jean-Charles", body = "World";
    static long timestamp = 42;
    static String key = id + "_" + timestamp;

    @Test
    public void creationWorks() {
        Post p = new Post(key, title, id, body, timestamp, 10, 20);
        assertEquals(p.getTitle_(), title);
        assertEquals(p.getGoogleId_(), id);
        assertEquals(p.getBody_(), body);
        assertEquals(p.getTimestamp_(), timestamp);
        assertEquals(p.getKey_(), key);
    }

    @Test
    public void setTitle(){
        Post p = new Post(key, title, id, body, timestamp, 10, 20);
        String test = "testTitle";
        p.setTitle_(test);
        assertEquals(p.getTitle_(), test);
    }

    @Test
    public void setBody(){
        Post p = new Post(key, title, id, body, timestamp, 10, 20);
        String test = "testBody";
        p.setBody_(test);
        assertEquals(p.getBody_(), test);
    }
}
