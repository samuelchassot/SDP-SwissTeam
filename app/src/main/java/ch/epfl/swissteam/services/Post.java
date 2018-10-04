package ch.epfl.swissteam.services;

import java.util.Date;

/**
 * Class reprensenting posts of search of services
 *
 * @author Adrian Baudat
 * @author Julie Giunta
 */
public class Post implements DatabaseObject {

    private final String title, username, body;
    private final Date date;

    /**
     * Construct an immutable post for searching services
     *
     * @param title the title of the post
     * @param username the username of the person who post the post
     * @param body the body of the post
     * @param date the date at which the post was submitted
     */
    public Post(String title, String username, String body, Date date) {
        this.title = title;
        this.username = username;
        this.body = body;
        this.date = date;
    }

    @Override
    public DatabaseObject load() {
        return null;
    }

    @Override
    public void store() {

    }
}
