package net.chi6rag.twitchblade_test;

import net.chi6rag.twitchblade.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import test_helpers.RetweetTestHelper;
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
    RetweetTestHelper retweetTestHelper;

    @Before
    public void BeforeEach(){
        connection = new DbConnection();
        userTestHelper = new UserTestHelper(connection);
        tweetTestHelper = new TweetTestHelper(connection);
        retweetTestHelper = new RetweetTestHelper(connection);
        user = userTestHelper.getSavedUserObject("foo_example", "123456789",
                this.connection);
        profile = new Profile(user, this.connection);
    }

    @After
    public void afterEach(){
        retweetTestHelper.deleteAllRetweets();
        tweetTestHelper.deleteAllTweets();
        userTestHelper.deleteAllUsers();
        connection.close();
    }

    @Test
    public void testGetTweetsForValidUserReturnsUsersTweetsAndRetweets(){
        // ----- setup test data -----
        tweetTestHelper.createSampleTweetsFor(user, "hello!", "hello world!");
        User testUser = userTestHelper.getSavedUserObject("bar_example",
                "123456789", this.connection);
        Tweet testUserTweet = tweetTestHelper.getSavedTweetObject("hello from " +
                "testUser!", testUser.getId(), this.connection);
        Retweet retweet = new Retweet(testUserTweet.getId(), user, this.connection);
        retweet.save();
        ArrayList<Tweet> tweets = profile.getTweets();
        // --------- testing ---------
        String[]  queriedTweetBodies  = tweetTestHelper.getTweetBodies(tweets);
        String[] expectedTweetBodies = { "hello!", "hello world!", "hello from testUser!"};
        tweetTestHelper.validatePresenceOfTweetBodies(expectedTweetBodies,
                queriedTweetBodies);
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
