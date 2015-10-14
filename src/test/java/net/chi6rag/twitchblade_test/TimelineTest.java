package net.chi6rag.twitchblade_test;

import net.chi6rag.twitchblade.*;
import test_helpers.*;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import java.util.ArrayList;

import static junit.framework.TestCase.assertNotNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class TimelineTest {
    DbConnection connection;
    Timeline timeline;
    User currentUser;

    // Objects of helper classes
    UserTestHelper userTestHelper;
    TweetTestHelper tweetTestHelper;

    @Before
    public void BeforeEach(){
        connection = new DbConnection();
        userTestHelper = new UserTestHelper(connection);
        tweetTestHelper = new TweetTestHelper(connection);
        currentUser = userTestHelper.getSavedUserObject("foo_example", "123456789",
                this.connection);
        timeline = new Timeline(currentUser, this.connection);
    }

    @After
    public void afterEach(){
        tweetTestHelper.deleteAllTweets();
        userTestHelper.deleteAllUsers();
        connection.close();
    }

    @Test
    public void testGetTweetsForUserReturnsUsersTweets(){
        tweetTestHelper.getSavedTweetObject("hello", currentUser.getId(),
                this.connection);
        ArrayList<Tweet> tweets = timeline.getTweets();
        for(int i=0; i<tweets.size(); i++){
            Tweet extractedTweet = tweets.get(i);
            assertEquals(extractedTweet.getUserId(), currentUser.getId());
            assertEquals(extractedTweet.getCreatedAt().getClass()
                            .getSimpleName(), "Date");
            assertNotNull(extractedTweet.getCreatedAt());
            assertEquals(extractedTweet.getId().getClass()
                    .getSimpleName(), "Integer");
            assertNotNull(extractedTweet.getId());
        }
    }

    @Test
    public void testGetTweetsReturnsBlankArrayListForValidUserWithNoTweets(){
        ArrayList<Tweet> tweets = timeline.getTweets();
        assertEquals(tweets.size(), 0);
    }

    @Test
    public void testGetTweetsReturnsNullForUnsavedUser(){
        User unsavedUser = new User("baz_example", "123456789",
                this.connection);
        timeline = new Timeline(unsavedUser, this.connection);
        ArrayList<Tweet> tweets = timeline.getTweets();
        assertEquals(tweets, null);
    }

}
