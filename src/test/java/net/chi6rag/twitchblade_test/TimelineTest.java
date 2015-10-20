package net.chi6rag.twitchblade_test;

import net.chi6rag.twitchblade.*;
import test_helpers.*;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import java.util.ArrayList;

import static junit.framework.TestCase.assertNotNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

public class TimelineTest {
    DbConnection connection;
    Timeline timeline;
    User currentUser;

    // Objects of helper classes
    UserTestHelper userTestHelper;
    TweetTestHelper tweetTestHelper;
    AssertionTestHelper assertionTestHelper;
    RelationshipTestHelper relationshipTestHelper;

    @Before
    public void BeforeEach(){
        connection = new DbConnection("testing");
        connection.setAutoCommit(false);
        userTestHelper = new UserTestHelper(connection);
        tweetTestHelper = new TweetTestHelper(connection);
        relationshipTestHelper = new RelationshipTestHelper(connection);
        assertionTestHelper = new AssertionTestHelper();
        currentUser = userTestHelper.getSavedUserObject("foo_example", "123456789",
                this.connection);
        timeline = new Timeline(currentUser, this.connection);
    }

    @After
    public void afterEach(){
        connection.rollback();
        connection.close();
    }

    @Test
    public void testGetTweetsForUserReturnsTweetsForUserAndUsersUserFollows(){
        // ----- setup data -----
        User userToFollow = userTestHelper.getSavedUserObject("bar_example",
                "123456789", this.connection);
        tweetTestHelper.createSampleTweetsFor(userToFollow, "testing_one",
                "testing_two");
        tweetTestHelper.createSampleTweetsFor(currentUser, "testing_three");
        currentUser.follow("bar_example");
        // -------- test --------
        ArrayList<Tweet> tweets = timeline.getTweets();
        String[] expectedTweetsBodies = {"testing_one", "testing_two", "testing_three"};
        validateTimeline(tweets, expectedTweetsBodies);
    }

    @Test
    public void testGetTweetsReturnsBlankArrayListForValidUserWithNoFollowersAndNoTweets(){
        ArrayList<Tweet> tweets = timeline.getTweets();
        assertEquals(tweets.size(), 0);
    }

    @Test
    public void testGetTweetsReturnsNullForUnsavedUser(){
        User unsavedUser = new User("baz_example", "123456789",
                this.connection);
        timeline = new Timeline(unsavedUser, this.connection);
        ArrayList<Tweet> tweets = timeline.getTweets();
        assertEquals(tweets, null);
    }

    @Test
    public void testGetTweetsReturnsBlankArraylistIfNeitherUserNeitherUsersFollowersHaveTweets(){
        userTestHelper.getSavedUserObject("bar_example", "123456789", this.connection);
        currentUser.follow("bar_example");
        ArrayList<Tweet> tweets = timeline.getTweets();
        assertEquals(tweets.size(), 0);
    }

    private void validateTimeline(ArrayList<Tweet> tweetsQueried,
                                  String[] expectedTweetBodies){
        String[] queriedTweetBodies = tweetTestHelper.getTweetBodies(tweetsQueried);
        String tweetBody;
        for(int i=0; i<expectedTweetBodies.length; i++){
            tweetBody = expectedTweetBodies[i];
            assertTrue(tweetBody + " not found in queried tweets",
                assertionTestHelper.containsElement(queriedTweetBodies, tweetBody)
            );
        }
    }

}
