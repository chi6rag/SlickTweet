package net.chi6rag.twitchblade_test;

import net.chi6rag.twitchblade.*;
import test_helpers.*;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import java.io.ByteArrayOutputStream;
import java.lang.reflect.Field;
import static junit.framework.TestCase.assertNotNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class UserActivityTest {
    DbConnection connection;
    User currentUser;
    UserActivity userActivity;

    // Objects of helper classes
    UserTestHelper userTestHelper;
    TweetTestHelper tweetTestHelper;
    IOTestHelper ioTestHelper;
    AssertionTestHelper assertionTestHelper;
    RelationshipTestHelper relationshipTestHelper;
    RetweetTestHelper retweetTestHelper;

    @Before
    public void beforeEach(){
//        try {
//            connection.setAutoCommit(false);
//        } catch (SQLException e) {
//            // Do nothing
//        }
        connection = new DbConnection("testing");
        tweetTestHelper = new TweetTestHelper(connection);
        userTestHelper = new UserTestHelper(connection);
        ioTestHelper = new IOTestHelper();
        assertionTestHelper = new AssertionTestHelper();
        relationshipTestHelper = new RelationshipTestHelper(connection);
        retweetTestHelper = new RetweetTestHelper(connection);
        currentUser = userTestHelper.getSavedUserObject("foo_example",
                "123456789", connection);
        userActivity = new UserActivity(currentUser, connection);
    }

    @After
    public void afterEach(){
//        try {
//            connection.rollback();
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
        retweetTestHelper.deleteAllRetweets();
        tweetTestHelper.deleteAllTweets();
        relationshipTestHelper.deleteAllRelationships();
        userTestHelper.deleteAllUsers();
        connection.close();
    }

    @Test
    public void testPrintsActivityOptions(){
        String activityOptions =
            "\nWelcome " + currentUser.getUsername() +
            "\n\n"                                   +
            "1. Tweet\n"                             +
            "2. Your Timeline\n"                     +
            "3. See someone's profile page\n"        +
            "4. Your Followers\n"                    +
            "5. Follow User\n"                       +
            "6. Retweet Tweet\n"                     +
            "7. Logout\n"                            +
            "Choose: ";
        ByteArrayOutputStream consoleOutput = ioTestHelper.mockStdOut();
        userActivity.printActivityOptions();
        assertionTestHelper.assertContains(consoleOutput.toString(), activityOptions);
    }

    @Test
    public void testAsksForTweet(){
        String question = "What's in your mind?";
        assertEquals(userActivity.askForTweet(), question);
    }

    @Test
    public void testPrintUsernameQuestionPrintsUsernameQuestionOnStdOut(){
        ByteArrayOutputStream consoleOutput = ioTestHelper.mockStdOut();
        String usernameQuestion = "Enter username: ";
        userActivity.printUsernameQuestion();
        assertionTestHelper.assertContains(consoleOutput.toString(), usernameQuestion);
    }


    @Test
    public void testTweetWithValidBodyReturnsTweet(){
        String validTweetBody = "hello";
        Tweet tweet = userActivity.tweet(validTweetBody);
        assertEquals(tweet.getClass().getSimpleName(), "Tweet");
        assertNotNull(tweet.getId());
        assertNotNull(tweet.getCreatedAt());
        assertEquals(tweet.getId().getClass().getSimpleName(), "Integer");
        assertEquals(tweet.getCreatedAt().getClass().getSimpleName(), "Date");
        assertEquals(tweet.getBody(), "hello");
        assertEquals(tweet.getUserId(), currentUser.getId());
    }

    @Test
    public void testTweetWithInvalidBodyReturnsNull(){
        String invalidTweetBody = tweetTestHelper.getInvalidTweetBody();
        Tweet tweet = userActivity.tweet(invalidTweetBody);
        Assert.assertEquals(tweet, null);
    }

    @Test
    public void testTweetWithInvalidBodyPrintsErrorOnStdOut(){
        ByteArrayOutputStream consoleOutput = ioTestHelper.mockStdOut();
        String invalidTweetbody = tweetTestHelper.getInvalidTweetBody();
        userActivity.tweet(invalidTweetbody);
        CharSequence errorMessage = tweetTestHelper.getTweetErrorMessage();
        assertionTestHelper.assertContains(consoleOutput.toString(),
                (String) errorMessage);
        ioTestHelper.setStdOutToDefault();
    }

    @Test
    public void testTweetWithValidBodyPrintsSavedStatus(){
        ByteArrayOutputStream consoleOutput = ioTestHelper.mockStdOut();
        String validTweetBody = "hello world";
        userActivity.tweet(validTweetBody);
        CharSequence savedStatusMessage = "\nTweet posted\n";
        assertionTestHelper.assertContains(consoleOutput.toString(),
                (String) savedStatusMessage);
        ioTestHelper.setStdOutToDefault();
    }

    @Test
    public void testTweetWithBlankBodyPrintsErrorOnStdOut(){
        ByteArrayOutputStream consoleOutput = ioTestHelper.mockStdOut();
        userActivity.tweet("");
        CharSequence errorMessage = tweetTestHelper.getTweetErrorMessage();
        assertionTestHelper.assertContains(consoleOutput.toString(),
                (String) errorMessage);
        ioTestHelper.setStdOutToDefault();
    }

    @Test
    public void testPrintTimelinePrintsUsersTimelineOnStdOutForUserWithTweets(){
        Tweet firstValidTweet = tweetTestHelper.getSavedTweetObject("testing one",
                this.currentUser.getId(), this.connection);
        Tweet secondValidTweet = tweetTestHelper.getSavedTweetObject("testing two",
                this.currentUser.getId(), this.connection);
        ByteArrayOutputStream consoleOutput = ioTestHelper.mockStdOut();
        userActivity.printTimeline();
        validateTimeline(consoleOutput, firstValidTweet, secondValidTweet);
        ioTestHelper.setStdOutToDefault();
    }

    @Test
    public void testPrintTimelinePrintsNotificationIfNoTweetsPresent(){
        ByteArrayOutputStream consoleOutput = ioTestHelper.mockStdOut();
        userActivity.printTimeline();
        assertionTestHelper.assertContains(consoleOutput.toString(),
                "\nNo Tweets\n");
        ioTestHelper.setStdOutToDefault();
    }

    @Test
    public void testPrintProfilePageOfWithValidUserArgumentPrintsUsersTweetsAndRetweets(){
        // ------ prepare test data ------
        tweetTestHelper.createSampleTweetsFor(currentUser, "hello by currentUser");
        ByteArrayOutputStream consoleOutput = ioTestHelper.mockStdOut();
        User testUser = userTestHelper.getSavedUserObject("bar_example", "123456789",
                this.connection);
        Tweet testUserTweet = tweetTestHelper.getSavedTweetObject("hello by testUser",
                testUser.getId(), this.connection);
        Retweet retweet = new Retweet(testUserTweet.getId(), currentUser, this.connection);
        retweet.save();
        // ------------- test -------------
        userActivity.printProfilePageOf("foo_example");
        assertionTestHelper.assertContains(consoleOutput.toString(), "hello by currentUser");
        assertionTestHelper.assertContains(consoleOutput.toString(), "hello by testUser");
        ioTestHelper.setStdOutToDefault();
    }

    @Test
    public void testLogoutSetsDatabaseConnectionToNull(){
        userActivity.logout();
        try {
            Object connection = getPrivateField(userActivity, "connection");
            assertEquals(connection, null);
        } catch (NoSuchFieldException e) {
            System.out.println("UserActivityTest: " +
                    "Private Field Connection not Found");
        } catch (IllegalAccessException e) {
            System.out.println("UserActivityTest: " +
                    "Private Field not Accessible");
        }
    }

    @Test
    public void testLogoutSetsCurrentUserToNull(){
        userActivity.logout();
        try {
            Object currentUser = getPrivateField(userActivity, "currentUser");
            assertEquals(currentUser, null);
        } catch (NoSuchFieldException e) {
            System.out.println("UserActivityTest: " +
                    "Private Field CurrentUser not Found");
        } catch (IllegalAccessException e) {
            System.out.println("UserActivityTest: " +
                    "Private Field not Accessible");
        }
    }

    @Test
    public void testPrintTimelineWithValidUsernameToPrintTimelineForSelectedUser(){
        ByteArrayOutputStream consoleOutput = ioTestHelper.mockStdOut();
        User testUser = userTestHelper.getSavedUserObject("bar_example", "123456789",
                this.connection);
        Tweet userTweetOne = tweetTestHelper.getSavedTweetObject("hello world!",
                testUser.getId(), this.connection);
        Tweet userTweetTwo = tweetTestHelper.getSavedTweetObject("hello world!",
                testUser.getId(), this.connection);
        userActivity.printTimeline(testUser.getUsername());
        validateTimeline(consoleOutput, userTweetOne, userTweetTwo);
        ioTestHelper.setStdOutToDefault();
    }

    @Test
    public void testPrintTimelineWithInvalidUsernameToPrintErrorMessage(){
        ByteArrayOutputStream consoleOutput = ioTestHelper.mockStdOut();
        userActivity.printTimeline("random_user");
        assertionTestHelper.assertContains(consoleOutput.toString(),
                "Username does not exist");
        ioTestHelper.setStdOutToDefault();
    }

    @Test
    public void testPrintTimelineForValidUserWithNoTweetsPrintsNoTweets(){
        ByteArrayOutputStream consoleOutput = ioTestHelper.mockStdOut();
        userActivity.printTimeline("foo_example");
        assertionTestHelper.assertContains(consoleOutput.toString(), "No Tweets");
        ioTestHelper.setStdOutToDefault();
    }

    @Test
    public void testPrintFollowersForUserWithNoFollowersPrintsNoFollowersNotice(){
        ByteArrayOutputStream consoleOutput = ioTestHelper.mockStdOut();
        userActivity.printFollowers();
        assertionTestHelper.assertContains(consoleOutput.toString(), "No Followers");
        ioTestHelper.setStdOutToDefault();
    }

    @Test
    public void testPrintFollowersForUserWithExistentFollowers(){
        ByteArrayOutputStream consoleOutput = ioTestHelper.mockStdOut();
        relationshipTestHelper.createSampleFollowersFor(currentUser,
                "bar_example", "baz_example");
        userActivity.printFollowers();
        assertionTestHelper.assertContains(consoleOutput.toString(), "bar_example");
        assertionTestHelper.assertContains(consoleOutput.toString(), "baz_example");
        ioTestHelper.setStdOutToDefault();
    }

    @Test
    public void testFollowUserWithValidUsernameFollowsUser(){
        User userToFollow = userTestHelper.getSavedUserObject("bar_example",
                "123456789", connection);
        userActivity.followUser("bar_example");
        relationshipTestHelper.validateFollowers(userToFollow, currentUser);
    }

    @Test
    public void testFollowUserWithValidUsernamePrintsFollowedUsernameOnStdOut(){
        ByteArrayOutputStream consoleOutput = ioTestHelper.mockStdOut();
        userTestHelper.createTestUser("bar_example", "123456789");
        userActivity.followUser("bar_example");
        assertionTestHelper.assertContains(consoleOutput.toString(),
                "Followed bar_example");
        ioTestHelper.setStdOutToDefault();
    }

    @Test
    public void testFollowUserWithInvalidUsernamePrintsErrorOnStdOut(){
        ByteArrayOutputStream consoleOutput = ioTestHelper.mockStdOut();
        userActivity.followUser("a");
        assertionTestHelper.assertContains(consoleOutput.toString(),
                "Cannot follow a");
        ioTestHelper.setStdOutToDefault();
    }

    @Test
    public void testReFollowUserWithValidUsernamePrintsErrorOnStdOut(){
        ByteArrayOutputStream consoleOutput = ioTestHelper.mockStdOut();
        userTestHelper.createTestUser("bar_example", "123456789");
        userActivity.followUser("bar_example");
        userActivity.followUser("bar_example");
        assertionTestHelper.assertContains(consoleOutput.toString(),
                "Cannot follow bar_example");
        ioTestHelper.setStdOutToDefault();
    }

    @Test
    public void testRetweetWithValidTweetIdToPrintSuccessOnStdOut(){
        ByteArrayOutputStream consoleOutput = ioTestHelper.mockStdOut();
        User testUser = userTestHelper.getSavedUserObject("bar_example",
                "123456789", this.connection);
        Tweet testTweet = tweetTestHelper.getSavedTweetObject("hello", testUser.getId(),
                this.connection);
        userActivity.retweet(testTweet.getId());
        assertionTestHelper.assertContains(consoleOutput.toString(), "\nTweet Retweeted!\n");
        ioTestHelper.setStdOutToDefault();
    }

    @Test
    public void testRetweetWithInvalidTweetIdToPrintErrorOnStdOut(){
        ByteArrayOutputStream consoleOutput = ioTestHelper.mockStdOut();
        userActivity.retweet(2147483647);
        assertionTestHelper.assertContains(consoleOutput.toString(), "\nCannot " +
                "retweet this tweet!\n");
        ioTestHelper.setStdOutToDefault();
    }

    private Object getPrivateField(Object privateFieldContainer,
       String privateFieldName) throws NoSuchFieldException, IllegalAccessException {
        Field field = privateFieldContainer.getClass().getDeclaredField(privateFieldName);
        field.setAccessible(true);
        return field.get(privateFieldContainer);
    }

    private void validateTimeline(ByteArrayOutputStream consoleOutput,
                                  Tweet tweetOne, Tweet tweetTwo){
        assertionTestHelper.assertContains(consoleOutput.toString(),
                "Tweet ID: " + tweetOne.getId());
        assertionTestHelper.assertContains(consoleOutput.toString(),
                "Tweet ID: " + tweetTwo.getId());
        assertionTestHelper.assertContains(consoleOutput.toString(),
                tweetOne.getBody());
        assertionTestHelper.assertContains(consoleOutput.toString(),
                tweetTwo.getBody());
        assertTrue(assertionTestHelper.containsAnyOf(consoleOutput.toString(),
                "days ago", "hours ago", "minutes ago", "seconds ago"));

    }

}