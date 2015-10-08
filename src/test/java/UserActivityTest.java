import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import java.io.ByteArrayOutputStream;
import static org.junit.Assert.assertEquals;

public class UserActivityTest {
    DbConnection connection = new DbConnection();
    User currentUser;
    UserActivity userActivity;

    // Objects of helper classes
    UserTestHelper userTestHelper = new UserTestHelper(connection);
    TweetTestHelper tweetTestHelper = new TweetTestHelper(connection);
    IOTestHelper ioTestHelper = new IOTestHelper();
    AssertionTestHelper assertionTestHelper = new AssertionTestHelper();

    @Before
    public void beforeEach(){
        currentUser = userTestHelper.getSavedUserObject("foo_example",
                "123456789", connection);
        userActivity = new UserActivity(currentUser);
    }

    @After
    public void afterEach(){
        tweetTestHelper.deleteAllTweets();
        userTestHelper.deleteAllUsers();
    }

    @Test
    public void testGetsActivityOptions(){
        String activityOptions =
            "\nWelcome foo_example" +
            "\n\n"                  +
            "1. Tweet\n"            +
            "2. Your Timeline\n"    +
            "3. Logout\n"           +
            "Choose: ";
        assertEquals(userActivity.getActivityOptions(),
                activityOptions);
    }

    @Test
    public void testAsksForTweet(){
        String question = "What's in your mind?";
        assertEquals(userActivity.askForTweet(), question);
    }

    @Test
    public void testTweetWithValidBodyReturnsTweet(){
        String validTweetBody = "hello";
        Tweet tweet = userActivity.tweet(validTweetBody);
        assertEquals(tweet.getClass().getName(), "Tweet");
        assertEquals(tweet.getId().getClass()
                .getSimpleName(), "Integer");
        assertEquals(tweet.getBody(), "hello");
        assertEquals(tweet.getUserId(), currentUser.getId());
    }

    @Test
    public void testTweetWithInvalidBodyReturnsNull(){
        String invalidTweetBody = tweetTestHelper.getInvalidTweetBody();
        Tweet tweet = userActivity.tweet(invalidTweetBody);
        assertEquals(tweet, null);
    }

    @Test
    public void testTweetWithInvalidBodyPrintsErrorOnStdOut(){
        ByteArrayOutputStream consoleOutput = ioTestHelper.mockStdOut();
        String invalidTweetbody = tweetTestHelper.getInvalidTweetBody();
        userActivity.tweet(invalidTweetbody);
        CharSequence errorMessage = "Tweet cannot be saved";
        assertionTestHelper.assertContains(consoleOutput.toString(),
                (String) errorMessage);
        ioTestHelper.setStdOutToDefault();
    }

    @Test
    public void testPrintsUserTimelineOnStdOutForUserWithTweets(){
        ByteArrayOutputStream consoleOutput = ioTestHelper.mockStdOut();
        Tweet firstValidTweet = tweetTestHelper.getSavedTweetObject("testing one",
                this.currentUser.id, this.connection);
        Tweet secondValidTweet = tweetTestHelper.getSavedTweetObject("testing two",
                this.currentUser.id, this.connection);
        userActivity.printTimeline();
        assertionTestHelper.assertContains(consoleOutput.toString(), firstValidTweet.getBody());
        assertionTestHelper.assertContains(consoleOutput.toString(), secondValidTweet.getBody());
        ioTestHelper.setStdOutToDefault();
    }

}