package net.chi6rag.twitchblade_test;

import net.chi6rag.twitchblade.*;
import test_helpers.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import java.util.ArrayList;
import java.util.Hashtable;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

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
    public void testWhereMethodWithValidUserIDButNoTweetsReturnsEmptyArrayList(){
        Hashtable queryHash = new Hashtable();
        queryHash.put("userId", user.getId());
        ArrayList<Tweet> tweets = allTweets.where(queryHash);
        assertEquals(tweets.size(), 0);
    }

    @Test
    public void testForTimelineOfWithInvalidUserIdReturnsEmptyArrayList(){
        ArrayList<Tweet> tweets = allTweets.forTimelineOf(2147483647);
        assertEquals(tweets.size(), 0);
    }

    @Test
    public void testForTimelineOfWithValidUserIdHavingNoFollowersReturnsTweetsOfUserOnly(){
        tweetTestHelper.createSampleTweetsFor(user, "testing_one", "testing_two");
        ArrayList<Tweet> tweets = allTweets.forTimelineOf(user.getId());
        String[] expectedTweetsBodies = {"testing_one", "testing_two"};
        Tweet tweet;
        for(int i=0; i<tweets.size(); i++){
            tweet = tweets.get(i);
            assertEquals(tweet.getUserId(), user.getId());
            assertTrue(tweet.getBody() + " not a part of " + expectedTweetsBodies,
                    containsElement(expectedTweetsBodies, tweet.getBody())
            );
        }
    }

    @Test
    public void testForTimelineOfWithValidUserIdReturnsTweetsForUserAndUsersUserFollows(){
        User userToFollow = userTestHelper.getSavedUserObject("bar_example",
                "123456789", this.connection);
        tweetTestHelper.createSampleTweetsFor(userToFollow, "testing_one",
                "testing two");
        tweetTestHelper.createSampleTweetsFor(user, "testing_three");
        ArrayList<Tweet> tweets = allTweets.forTimelineOf(user.getId());

        Integer[] expectedTweetsUserIds = {user.getId(), userToFollow.getId()};
        String[] expectedTweetsBodies = {"testing_one", "testing_two", "testing_three"};
        for(int i=0; i<tweets.size(); i++){
            assertTrue(containsElement(expectedTweetsUserIds, tweets.get(i).getUserId()));
            assertTrue(containsElement(expectedTweetsBodies, tweets.get(i).getBody()));
        }
    }

    private <T> boolean containsElement(T[] array, T element){
        for(int i=0; i<array.length; i++) {
            if(array[i].equals(element) || array[i] == element) return true;
        }
        return false;
    }

}