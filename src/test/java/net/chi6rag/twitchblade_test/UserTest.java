package net.chi6rag.twitchblade_test;

import net.chi6rag.twitchblade.*;
import org.junit.Before;
import test_helpers.*;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import java.util.ArrayList;
import static org.junit.Assert.*;

public class UserTest {
    DbConnection connection;

    // Objects of helper classes
    UserTestHelper userTestHelper;
    RelationshipTestHelper relationshipTestHelper;
    TweetTestHelper tweetTestHelper;
    RetweetTestHelper retweetTestHelper;

    @Before
    public void beforeEach(){
        connection = new DbConnection("testing");
        userTestHelper = new UserTestHelper(connection);
        relationshipTestHelper = new RelationshipTestHelper(connection);
        tweetTestHelper = new TweetTestHelper(connection);
        retweetTestHelper = new RetweetTestHelper(connection);
    }

    @After
    public void afterEach(){
        retweetTestHelper.deleteAllRetweets();
        relationshipTestHelper.deleteAllRelationships();
        tweetTestHelper.deleteAllTweets();
        userTestHelper.deleteAllUsers();
        connection.close();
    }

    @Test
    public void testNewWithValidDetailsSetsIdNull(){
        User user = new User("foo_example", "123456789", this.connection);
        assertEquals(user.getId(), null);
    }

    @Test
    public void testSaveWithValidUserObjectIncreasesUserCountBy1(){
        int beforeCount = userTestHelper.getUserCount();
        User user = new User("foo_example", "123456789", this.connection);
        user.save();
        int afterCount = userTestHelper.getUserCount();
        assertNotEquals(beforeCount, afterCount);
    }

    @Test
    public void testSaveWithValidUserObjectReturnsTheSavedUser(){
        User user = new User("foo_example", "123456789", this.connection);
        User savedUser = user.save();
        Assert.assertEquals(savedUser.getId().getClass()
                .getSimpleName(), "Integer");
        Assert.assertEquals(savedUser.getUsername(), "foo_example");
        Assert.assertEquals(savedUser.getPassword(), "123456789");
        Assert.assertEquals(savedUser.getClass().getSimpleName(), "User");
    }

    @Test
    public void testSaveWithInvalidUserObjectKeepsUserCountSame(){
        int beforeCount = userTestHelper.getUserCount();
        User user = new User("ab", "123456789", this.connection);
        user.save();
        int afterCount = userTestHelper.getUserCount();
        assertEquals(beforeCount, afterCount);
    }

    @Test
    public void testSaveWithInvalidUserObjectReturnsNull(){
        User user = new User("ab", "123456789", this.connection);
        Assert.assertEquals(user.save(), null);
    }

    @Test
    public void testFollowersForValidUserWithoutFollowersReturnsEmptyArray(){
        User user = userTestHelper.getSavedUserObject("foo_example",
                "123456789", connection);
        ArrayList<User> followers = user.followers();
        assertEquals(followers.size(), 0);
    }

    @Test
    public void testFollowersForUnsavedUserReturnsEmptyArray(){
        User user = new User("ab", "123456789", this.connection);
        ArrayList<User> followers = user.followers();
        assertEquals(followers.size(), 0);
    }

    @Test
    public void testFollowersForValidUserWithExistentFollowersReturnsFollowers(){
        User user = userTestHelper.getSavedUserObject("foo_example",
                "123456789", connection);
        relationshipTestHelper.createSampleFollowersFor(user);
        ArrayList<User> followers = user.followers();
        assertEquals(followers.size(), 2);
        relationshipTestHelper.validateFollowers(user, followers);
    }

    @Test
    public void testFollowForUnsavedUserWithValidArgumentsReturnsFalse(){
        User userOne = new User("foo_example", "123456789", connection);
        User userTwo = userTestHelper.getSavedUserObject("bar_example",
                "123456789", connection);
        boolean hasFollowed = userOne.follow("bar_example");
        relationshipTestHelper.validateNonFollower(userOne, userTwo);
        assertFalse("Unsaved user foo_example followed saved user " +
                "bar_example", hasFollowed);
    }

    @Test
    public void testFollowForSavedUserWithInvalidUsernameArgumentReturnsFalse(){
        User user = userTestHelper.getSavedUserObject("foo_example",
                "123456789", connection);
        User userToFollow = userTestHelper.getSavedUserObject("bar_example",
                "123456789", connection);
        boolean hasFollowed = user.follow("baz_example");
        relationshipTestHelper.validateNonFollower(user, userToFollow);
        assertFalse("foo_example followed inexistent User bax_example", hasFollowed);
    }

    // test follow for saved user with invalid arguments returns false
    @Test
    public void testFollowForSavedUserWithInvalidArgumentsReturnsFalse(){
        User user = userTestHelper.getSavedUserObject("foo_example",
                "123456789", connection);
        boolean hasFollowed = user.follow("a");
        assertFalse("foo_example followed inexistent User a", hasFollowed);
    }

    // test follow for saved user with valid arguments returns true
    @Test
    public void testFollowForSavedUserWithValidArgumentsReturnsTrue(){
        User user = userTestHelper.getSavedUserObject("foo_example",
                "123456789", connection);
        userTestHelper.getSavedUserObject("bar_example",
                "123456789", connection);
        boolean hasFollowed = user.follow("bar_example");
        assertTrue("foo_example did not follow valid user bar_example", hasFollowed);
    }

    @Test
    public void testFollowForSavedUserWithValidArgumentsMakesUserFollowArgumentUser(){
        User user = userTestHelper.getSavedUserObject("foo_example",
                "123456789", connection);
        User userToFollow = userTestHelper.getSavedUserObject("bar_example",
                "123456789", connection);
        user.follow("bar_example");
        relationshipTestHelper.validateFollowers(userToFollow, user);
    }

    // test follow for saved user with valid arguments returns false on refollow
    @Test
    public void testFollowForSavedUserWithValidArgumentsReturnsFalseOnRefollow(){
        User user = userTestHelper.getSavedUserObject("foo_example",
                "123456789", connection);
        userTestHelper.getSavedUserObject("bar_example", "123456789", connection);
        user.follow("bar_example");
        boolean hasFollowedAgain = user.follow("bar_example");
        assertFalse("foo_example followed bar_example again", hasFollowedAgain);
    }

    @Test
    public void testHasTweetByIdWithValidTweetIdArgumentToReturnTrueIfUsersTweetsIncludesIt(){
        User user = userTestHelper.getSavedUserObject("foo_example",
                "123456789", connection);
        Tweet usersTweet = tweetTestHelper.getSavedTweetObject("hello!",
                user.getId(), this.connection);
        assertTrue(user.getUsername() + " has a tweet by tweet id " +
                usersTweet.getId() + " but #hasTweetByID fails to fetch it"
                , user.hasTweetByID(usersTweet.getId())
        );
    }

    @Test
    public void testHasTweetByIdWithValidTweetIdArgumentToReturnFalseIfUsersTweetsDoNotIncludeIt(){
        // ----- setup test -----
        User user = userTestHelper.getSavedUserObject("foo_example",
                "123456789", connection);
        User secondUser = userTestHelper.getSavedUserObject("bar_example",
                "123456789", connection);
        Tweet secondUsersTweet = tweetTestHelper.getSavedTweetObject("hello!",
                secondUser.getId(), this.connection);
        // -------- test --------
        assertFalse(user.getUsername() + " does not have a tweet by tweet id " +
                        secondUsersTweet.getId() + " but #hasTweetByID fetches it"
                , user.hasTweetByID(secondUsersTweet.getId())
        );
    }

    @Test
    public void testHasTweetByIdWithInvalidTweetIdArgumentToReturnFalse(){
        User user = userTestHelper.getSavedUserObject("foo_example",
                "123456789", connection);
        assertFalse(user.getUsername() + " does not have a tweet by id 123456",
                user.hasTweetByID(123456)
        );
    }

    @Test
    public void testHasTweetByIdOnInvalidUserToReturnFalse(){
        User user = new User("foo_example", "123456789", connection);
        assertFalse(user.getUsername() + " does not have a tweet by id 123456",
                user.hasTweetByID(123456)
        );
    }

    @Test
    public void testIsValidOnSavedUserToReturnTrue(){
        User user = userTestHelper.getSavedUserObject("foo_example", "123456789",
                this.connection);
        assertTrue(user.getUsername() + " is a valid user", user.isValid());
    }

    @Test
    public void testIsValidOnUnsavedUserToReturnFalse(){
        User user = new User("foo_example", "123456789", this.connection);
        assertFalse(user.getUsername() + " is not a valid user", user.isValid());
    }

    @Test
    public void testRetweetWithValidTweetIdReturnsTrue(){
        User user = userTestHelper.getSavedUserObject("foo_example",
                "123456789", this.connection);
        User secondUser = userTestHelper.getSavedUserObject("bar_example",
                "123456789", this.connection);
        Tweet tweet = tweetTestHelper.getSavedTweetObject("hello!",
                secondUser.getId(), this.connection);
        boolean isSuccessful = user.retweet(tweet.getId());
        assertTrue(user.getUsername() + " could not retweet " +
                secondUser.getUsername() + "'s tweet", isSuccessful);
    }

    @Test
    public void testRetweetWithInvalidTweetIdReturnsFalse(){
        User user = userTestHelper.getSavedUserObject("foo_example",
                "123456789", this.connection);
        boolean isSuccessful = user.retweet(2147483647);
        assertFalse(user.getUsername() + " retweeted an inexistent " +
                "tweet ", isSuccessful);
    }

}
