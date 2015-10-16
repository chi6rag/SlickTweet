package net.chi6rag.twitchblade_test;

import net.chi6rag.twitchblade.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import test_helpers.RetweetTestHelper;
import test_helpers.TweetTestHelper;
import test_helpers.UserTestHelper;
import static junit.framework.TestCase.assertFalse;
import static org.junit.Assert.assertTrue;

public class RetweetTest {
    DbConnection connection;
    User user;
    Tweet tweet;

    // Objects of helper classes
    UserTestHelper userTestHelper;
    TweetTestHelper tweetTestHelper;
    RetweetTestHelper retweetTestHelper;

    @Before
    public void beforeEach(){
        connection = new DbConnection();
        userTestHelper = new UserTestHelper(connection);
        tweetTestHelper = new TweetTestHelper(connection);
        retweetTestHelper = new RetweetTestHelper(connection);
        user = userTestHelper.getSavedUserObject("foo_example",
                "123456789", connection);
        tweet = tweetTestHelper.getSavedTweetObject("hello!", user.getId(), connection);
    }

    @After
    public void afterEach(){
        retweetTestHelper.deleteAllRetweets();
        tweetTestHelper.deleteAllTweets();
        userTestHelper.deleteAllUsers();
        connection.close();
    }

    @Test
    public void testSaveWithValidTweetIdAndValidUserReturnsTrue(){
        User secondUser = userTestHelper.getSavedUserObject("bar_example",
                "123456789", this.connection);
        Retweet retweet = new Retweet(tweet.getId(), secondUser, this.connection);
        boolean isSaved = retweet.save();
        assertTrue("#save with valid tweet id and user id " +
                "returned false", isSaved);
    }

    @Test
    public void testSaveWithValidTweetIdAndUnsavedUserReturnsFalse(){
        User unsavedUser = new User("bar_example", "123456789", this.connection);
        Retweet retweet = new Retweet(tweet.getId(), unsavedUser, this.connection);
        boolean isSaved = retweet.save();
        assertFalse("#save with valid tweet id and invalid user id" +
                " returned true", isSaved);
    }

    @Test
    public void testSaveWithInvalidTweetIdAndValidUserReturnsFalse(){
        Retweet retweet = new Retweet(2147483647, user, this.connection);
        boolean isSaved = retweet.save();
        assertFalse("#save with invalid tweet id and valid user id" +
                " returned true", isSaved);
    }

    @Test
    public void testSaveWithTweetIdIfAlreadyRetweetedByUserReturnsFalse(){
        Retweet retweet = new Retweet(tweet.getUserId(), user, this.connection);
        retweet.save();
        boolean isRetweetedAgain = retweet.save();
        assertFalse("#save with valid tweet id and valid user id" +
                " returned false if executed twice on same tweet", isRetweetedAgain);
    }

    @Test
    public void testSaveWithValidDetailsAndRetweeterIdSameAsTweetsUserIdReturnsFalse(){
        Retweet retweet = new Retweet(tweet.getId(), user, this.connection);
        boolean isSaved = retweet.save();
        assertFalse("\n#save with valid tweet_id but retweeter_id same as\n" +
                "tweet's user_id returned true but must return false", isSaved);
    }

}
