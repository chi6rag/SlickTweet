package net.chi6rag.twitchblade_test;

import net.chi6rag.twitchblade.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import test_helpers.TweetTestHelper;
import test_helpers.UserTestHelper;

public class RetweetTest {
    DbConnection connection;
    User user;
    Tweet tweet;

    // Objects of helper classes
    UserTestHelper userTestHelper;
    TweetTestHelper tweetTestHelper;

    @Before
    public void beforeEach(){
        connection = new DbConnection();
        userTestHelper = new UserTestHelper(connection);
        tweetTestHelper = new TweetTestHelper(connection);
        user = userTestHelper.getSavedUserObject("foo_example",
                "123456789", connection);
        tweet = tweetTestHelper.getSavedTweetObject("hello!", user.getId(), connection);
    }

    @After
    public void afterEach(){
        tweetTestHelper.deleteAllTweets();
        userTestHelper.deleteAllUsers();
        connection.close();
    }

    @Test
    public void testSaveWithValidTweetIdAndValidUserIdReturnsTrue(){
        Retweet retweet = new Retweet(tweet.getId(), user.getId());
        boolean isSaved = retweet.save();
        assertTrue("#retweet with valid tweet id and user id " +
                "returned false", isSaved);
    }

}
