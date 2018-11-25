package ch.epfl.swissteam.services;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(AndroidJUnit4.class)
public class PostsMapActivityTest extends SocializeTest<PostsMapActivity>{

    public PostsMapActivityTest(){
        setTestRule(PostsMapActivity.class);
    }

    @Test
    public void canOpenActivity() {

    }
}
