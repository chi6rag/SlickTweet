package net.chi6rag.twitchblade_test;

import net.chi6rag.twitchblade.*;
import test_helpers.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import java.util.ArrayList;
import java.util.Hashtable;
import static org.junit.Assert.assertEquals;

public class TweetsTest {
    Tweets allTweets;
    DbConnection connection;
    User user;

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
        allTweets = new Tweets(this.connection);
    }

    @After
    public void afterEach(){
        tweetTestHelper.deleteAllTweets();
        userTestHelper.deleteAllUsers();
    }

    @Test
    public void testWhereMethodWithValidUserIdReturnsTweetsForUser(){
        tweetTestHelper.getSavedTweetObject("hello", user.getId(), this.connection);
        Hashtable queryHash = new Hashtable();
        queryHash.put("userId", user.getId());
        ArrayList<Tweet> tweets = allTweets.where(queryHash);
        for(int i=0; i<tweets.size(); i++){
            assertEquals(tweets.get(i).getUserId(), user.getId());
            assertEquals(tweets.get(i).getCreatedAt().getClass()
                            .getSimpleName(), "Date");
            assertEquals(tweets.get(i).getClass().getSimpleName(),"Tweet");
        }
    }

    @Test
    public void testWhereMethodWithNegativeUserIdReturnsNull(){
        Hashtable queryHash = new Hashtable();
        queryHash.put("userId", -999);
        ArrayList<Tweet> tweets = allTweets.where(queryHash);
        assertEquals(tweets, null);
    }

    @Test
    public void testWhereMethodWithInvalidUserIdReturnsEmptyArrayList(){
        Hashtable queryHash = new Hashtable();
        queryHash.put("userId", 987654);
        ArrayList<Tweet> tweets = allTweets.where(queryHash);
        assertEquals(tweets.size(), 0);
    }

    @Test
    public void testWhereMethodWithValidUserButNoTweetsReturnsEmptyArrayList(){
        Hashtable queryHash = new Hashtable();
        queryHash.put("userId", user.getId());
        ArrayList<Tweet> tweets = allTweets.where(queryHash);
        assertEquals(tweets.size(), 0);
    }

}