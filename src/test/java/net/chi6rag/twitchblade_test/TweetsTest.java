package net.chi6rag.twitchblade_test;

import net.chi6rag.twitchblade.*;
import test_helpers.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import java.util.ArrayList;
import java.util.Hashtable;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class TweetsTest {
    Tweets allTweets;
    DbConnection connection;
    User user;

    // Objects of helper classes
    UserTestHelper userTestHelper;
    TweetTestHelper tweetTestHelper;
    AssertionTestHelper assertionTestHelper;
    RelationshipTestHelper relationshipTestHelper;
    RetweetTestHelper retweetTestHelper;

    @Before
    public void beforeEach(){
        connection = new DbConnection();
        userTestHelper = new UserTestHelper(connection);
        tweetTestHelper = new TweetTestHelper(connection);
        assertionTestHelper = new AssertionTestHelper();
        relationshipTestHelper = new RelationshipTestHelper(connection);
        retweetTestHelper = new RetweetTestHelper(connection);
        user = userTestHelper.getSavedUserObject("foo_example",
                "123456789", connection);
        allTweets = new Tweets(this.connection);
    }

    @After
    public void afterEach(){
        retweetTestHelper.deleteAllRetweets();
        tweetTestHelper.deleteAllTweets();
        relationshipTestHelper.deleteAllRelationships();
        userTestHelper.deleteAllUsers();
        this.connection.close();
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
    public void testForProfileOfWithValidUserIdReturnsTweetsAndRetweetsOfUser(){
        // ----- setup test data -----
        tweetTestHelper.createSampleTweetsFor(user, "hello!", "hello world!");
        User testUser = userTestHelper.getSavedUserObject("bar_example",
                "123456789", this.connection);
        Tweet testUserTweet = tweetTestHelper.getSavedTweetObject("hello from " +
                "testUser!", testUser.getId(), this.connection);
        Retweet retweet = new Retweet(testUserTweet.getId(), user, this.connection);
        retweet.save();
        ArrayList<Tweet> tweets = allTweets.forProfileOf(user.getId());
        // --------- testing ---------
        String[]  queriedTweetBodies  = tweetTestHelper.getTweetBodies(tweets);
        String[] expectedTweetBodies = { "hello!", "hello world!" };
        validatePresenceOfTweetBodies(expectedTweetBodies, queriedTweetBodies);
    }

    @Test
    public void testForProfileOfWithNegativeUserIdReturnsNull(){
        ArrayList<Tweet> tweets = allTweets.forProfileOf(-100);
        assertNull(tweets);
    }

    @Test
    public void testForProfileOfWithInvalidUserIdReturnsEmptyArraylist(){
        ArrayList<Tweet> tweets = allTweets.forProfileOf(2147483647);
        assertEquals(tweets.size(), 0);
    }

    @Test
    public void testForProfileOfWithValidUserIdButNoTweetsOrRetweetsReturnsEmptyArraylist(){
        ArrayList<Tweet> tweets = allTweets.forProfileOf(user.getId());
        assertEquals(tweets.size(), 0);
    }

    @Test
    public void testForTimelineOfWithInvalidUserIdReturnsEmptyArrayList(){
        ArrayList<Tweet> tweets = allTweets.forTimelineOf(2147483647);
        assertEquals(tweets.size(), 0);
    }

    @Test
    public void testForTimelineOfWithNegativeUserIdReturnsEmptyArrayList(){
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
                assertionTestHelper.containsElement(expectedTweetsBodies, tweet.getBody())
            );
        }
    }

    @Test
    public void testForTimelineOfWithValidUserIdHavingNoTweetsNorFollowersTweetsReturnsEmptyArraylist(){
        userTestHelper.getSavedUserObject("bar_example", "123456789", this.connection);
        user.follow("bar_example");
        ArrayList<Tweet> tweets = allTweets.forTimelineOf(user.getId());
        assertEquals(tweets.size(), 0);
    }

    @Test
    public void testForTimelineOfWithValidUserIdReturnsTweetsForUserAndUsersUserFollows(){
        // ------ setup data ------
        User userToFollow = userTestHelper.getSavedUserObject("bar_example",
                "123456789", this.connection);
        tweetTestHelper.createSampleTweetsFor(userToFollow, "testing_one",
                "testing_two");
        tweetTestHelper.createSampleTweetsFor(user, "testing_three");
        user.follow("bar_example");
        ArrayList<Tweet> tweets = allTweets.forTimelineOf(user.getId());
        String[]  queriedTweetBodies  = tweetTestHelper.getTweetBodies(tweets);
        Integer[] queriedTweetUserIds = getTweetUserIds(tweets);
        // --------- test ---------
        Integer[] expectedTweetsUserIds = {user.getId(), userToFollow.getId()};
        String[]  expectedTweetsBodies  = {"testing_one", "testing_two", "testing_three"};
        validatePresenceOfTweetBodies(expectedTweetsBodies, queriedTweetBodies);
        validatePresenceOfTweetUserIds(expectedTweetsUserIds, queriedTweetUserIds);
    }

    private void validatePresenceOfTweetUserIds(Integer[] expectedTweetsUserIds,
                                                Integer[] queriedTweetsUserIds) {
        Integer userId;
        for(int i=0; i<expectedTweetsUserIds.length; i++){
            userId = expectedTweetsUserIds[i];
            assertTrue(expectedTweetsUserIds + " does not contain " + userId,
                    assertionTestHelper.containsElement(queriedTweetsUserIds, userId));
        }
    }

    private void validatePresenceOfTweetBodies(String[] expectedTweetsBodies,
                                               String[] queriedTweetBodies) {
        String tweetBody;
        for(int i=0; i<expectedTweetsBodies.length; i++){
            tweetBody = expectedTweetsBodies[i];
            assertTrue("expectedTweetsBodies array does not contain " + tweetBody,
                assertionTestHelper.containsElement(queriedTweetBodies, tweetBody));
        }
    }

    private Integer[] getTweetUserIds(ArrayList<Tweet> tweets){
        Integer[] tweetUserIds = new Integer[tweets.size()];
        for(int i=0; i<tweets.size(); i++){ tweetUserIds[i] = tweets.get(i).getUserId(); }
        return tweetUserIds;
    }

}