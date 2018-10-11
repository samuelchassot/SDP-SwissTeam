package ch.epfl.swissteam.services;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class PostTest {

    static String title = "Hello", username = "Jean-Charles", body = "World";
    static long timestamp = 42;

    @Test
    public void creationWorks() {
        Post p = new Post(title, username, body, timestamp);
        assertEquals(title, p.getTitle_());
        assertEquals(username, p.getUsername_());
        assertEquals(body, p.getBody_());
        assertEquals(timestamp, p.getTimestamp_());
    }
}
