package ch.epfl.swissteam.services;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class PostTest {

    static String title = "Hello", username = "Jean-Charles", body = "World";
    static long timestamp = 42;

    @Test
    public void creationWorks() {
        Post p = new Post(title, username, body, timestamp);
        assertEquals(p.getTitle(), title);
        assertEquals(p.getUsername(), username);
        assertEquals(p.getBody(), body);
        assertEquals(p.getTimestamp(), timestamp);
    }
}
