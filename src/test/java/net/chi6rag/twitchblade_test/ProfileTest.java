package net.chi6rag.twitchblade_test;

import net.chi6rag.twitchblade.DbConnection;
import net.chi6rag.twitchblade.Timeline;
import net.chi6rag.twitchblade.User;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import test_helpers.AssertionTestHelper;
import test_helpers.RelationshipTestHelper;
import test_helpers.TweetTestHelper;
import test_helpers.UserTestHelper;

public class ProfileTest {
    DbConnection connection;
    Profile profile;
    User currentUser;

    // Objects of helper classes
    UserTestHelper userTestHelper;
    TweetTestHelper tweetTestHelper;
    AssertionTestHelper assertionTestHelper;
    RelationshipTestHelper relationshipTestHelper;

    @Before
    public void BeforeEach(){
        connection = new DbConnection();
        userTestHelper = new UserTestHelper(connection);
        tweetTestHelper = new TweetTestHelper(connection);
        relationshipTestHelper = new RelationshipTestHelper(connection);
        assertionTestHelper = new AssertionTestHelper();
        user = userTestHelper.getSavedUserObject("foo_example", "123456789",
                this.connection);
        profile = new Profile(user, this.connection);
    }

    @After
    public void afterEach(){
        tweetTestHelper.deleteAllTweets();
        relationshipTestHelper.deleteAllRelationships();
        userTestHelper.deleteAllUsers();
        connection.close();
    }

    @Test
    public void testGetTweetsFor

}
