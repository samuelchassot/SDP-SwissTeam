package ch.epfl.swissteam.services;

import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import ch.epfl.swissteam.services.view.activities.PostsMapActivity;

@RunWith(AndroidJUnit4.class)
public class PostsMapActivityTest extends SocializeTest<PostsMapActivity>{

    public PostsMapActivityTest(){
        setTestRule(PostsMapActivity.class);
    }

    @Test
    public void canOpenActivity() {

    }
}
