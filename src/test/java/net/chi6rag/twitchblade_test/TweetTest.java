package net.chi6rag.twitchblade_test;

import net.chi6rag.twitchblade.*;
import test_helpers.*;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;

public class TweetTest {
    DbConnection connection = new DbConnection();
    User user;

    // Objects of helper classes
    UserTestHelper userTestHelper = new UserTestHelper(connection);
    TweetTestHelper tweetTestHelper = new TweetTestHelper(connection);

    @Before
    public void beforeEach(){
        user = userTestHelper.getSavedUserObject("foo_example",
                "123456789", connection);
    }

    @After
    public void afterEach(){
        tweetTestHelper.deleteAllTweets();
        userTestHelper.deleteAllUsers();
    }

    @Test
    public void newCreatesATweetButDoesNotSaveInDatabase(){
        int countBefore = tweetTestHelper.getTweetsCount();
        new Tweet("hello", user.getId(), this.connection);
        int countAfter  = tweetTestHelper.getTweetsCount();
        assertEquals(countBefore, countAfter);
    }

    @Test
    public void newCreatesATweetButSetsIdNull(){
        Tweet tweet = new Tweet("hello", user.getId(), this.connection);
        assertEquals(tweet.getId(), null);
    }

    @Test
    public void newCreatesATweetButSetsCreatedAtNull(){
        Tweet tweet = new Tweet("hello", user.getId(), this.connection);
        assertEquals(tweet.getCreatedAt(), null);
    }

    @Test
    public void saveOnValidTweetIncreasesTweetCountBy1(){
        int countBefore = tweetTestHelper.getTweetsCount();
        Tweet tweet = new Tweet("hello", user.getId(), this.connection);
        tweet.save();
        int countAfter  = tweetTestHelper.getTweetsCount();
        assertNotEquals(countBefore, countAfter);
    }

    @Test
    public void saveOnInvalidTweetKeepsUserCountSame(){
        int countBefore = tweetTestHelper.getTweetsCount();
        String tweetBody = tweetTestHelper.getInvalidTweetBody();
        Tweet tweet = new Tweet(tweetBody, user.getId(), this.connection);
        tweet.save();
        int countAfter  = tweetTestHelper.getTweetsCount();
        assertEquals(countBefore, countAfter);
    }

    @Test
    public void saveOnValidTweetReturnsTweet(){
        Tweet tweet = new Tweet("hello", user.getId(), this.connection);
        Tweet savedTweet = tweet.save();
        assertEquals(savedTweet.getClass().getSimpleName(),
                "Tweet");
        assertEquals(savedTweet.getId().getClass()
                .getSimpleName(), "Integer");
        assertEquals(savedTweet.getBody(), "hello");
        assertEquals(savedTweet.getUserId(), user.getId());
        assertEquals(savedTweet.getCreatedAt().getClass()
                .getSimpleName(), "Date");
        assertNotNull(savedTweet.getCreatedAt());
    }

    @Test
    public void saveOnInvalidTweetReturnsNull(){
        String tweetBody = tweetTestHelper.getInvalidTweetBody();
        Tweet tweet = new Tweet(tweetBody, user.getId(), this.connection);
        Assert.assertEquals(tweet.save(), null);
    }

    @Test
    public void saveOnTweetWithInvalidUserIdReturnsNull(){
        Tweet tweet = new Tweet("hello", 99911223, this.connection);
        Assert.assertEquals(tweet.save(), null);
    }

    @Test
    public void saveOnTweetWithInvalidBodyReturnsNull(){
        String invalidBody = tweetTestHelper.getInvalidTweetBody();
        Tweet tweet = new Tweet(invalidBody, 99911223, this.connection);
        Assert.assertEquals(tweet.save(), null);
    }

}