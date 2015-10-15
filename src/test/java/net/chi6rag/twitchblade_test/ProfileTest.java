package net.chi6rag.twitchblade_test;

import net.chi6rag.twitchblade.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import test_helpers.TweetTestHelper;
import test_helpers.UserTestHelper;
import java.util.ArrayList;
import static org.junit.Assert.assertEquals;

public class ProfileTest {
    DbConnection connection;
    Profile profile;
    User user;

    // Objects of helper classes
    UserTestHelper userTestHelper;
    TweetTestHelper tweetTestHelper;

    @Before
    public void BeforeEach(){
        connection = new DbConnection();
        userTestHelper = new UserTestHelper(connection);
        tweetTestHelper = new TweetTestHelper(connection);
        user = userTestHelper.getSavedUserObject("foo_example", "123456789",
                this.connection);
        profile = new Profile(user, this.connection);
    }

    @After
    public void afterEach(){
        tweetTestHelper.deleteAllTweets();
        userTestHelper.deleteAllUsers();
        connection.close();
    }

    @Test
    public void testGetTweetsForValidUserReturnsUsersTweets(){
        tweetTestHelper.createSampleTweetsFor(user, "hello!", "hello world!");
        ArrayList<Tweet> tweets = profile.getTweets();
        for(int i=0; i<tweets.size(); i++){
            assertEquals(tweets.get(i).getUserId(), user.getId());
        }
    }

    @Test
    public void testGetTweetsReturnsBlankArrayListForValidUserWithNoTweets(){
        ArrayList<Tweet> tweets = profile.getTweets();
        assertEquals(tweets.size(), 0);
    }

    @Test
    public void testGetTweetsReturnsNullForUnsavedUser(){
        User unsavedUser = new User("baz_example", "123456789",
                this.connection);
        profile = new Profile(unsavedUser, this.connection);
        ArrayList<Tweet> tweets = profile.getTweets();
        assertEquals(tweets, null);
    }

}
