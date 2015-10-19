package net.chi6rag.twitchblade_test;

import net.chi6rag.twitchblade.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import test_helpers.RelationshipTestHelper;
import test_helpers.UserTestHelper;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class FollowerTest {
    DbConnection connection;
    Follower follower;
    User user;

    // Objects of helper classes
    UserTestHelper userTestHelper;
    RelationshipTestHelper relationshipTestHelper;

    @Before
    public void beforeEach(){
        connection = new DbConnection("testing");
        userTestHelper = new UserTestHelper(connection);
        relationshipTestHelper = new RelationshipTestHelper(connection);
        user = userTestHelper.getSavedUserObject("foo_example",
                "123456789", connection);
        follower = new Follower(user, connection);
    }

    @After
    public void afterEach(){
        relationshipTestHelper.deleteAllRelationships();
        userTestHelper.deleteAllUsers();
        connection.close();
    }

    @Test
    public void testFollowForUnsavedUserWithValidArgumentsReturnsFalse(){
        user = new User("baz_example", "123456789", this.connection);
        follower = new Follower(user, this.connection);
        User userToFollow = userTestHelper.getSavedUserObject("bar_example",
                "123456789", this.connection);
        boolean hasFollowed = follower.follow(userToFollow);
        relationshipTestHelper.validateNonFollower(user, userToFollow);
        assertFalse("Unsaved user followed User bar_example", hasFollowed);
    }

    @Test
    public void testFollowForSavedUserWithInvalidUserArgumentToReturnFalse(){
        User userToFollow = new User("bar_example", "123456789", this.connection);
        boolean hasFollowed = follower.follow(userToFollow);
        assertFalse("foo_example followed unsaved bar_example", hasFollowed);
        relationshipTestHelper.validateNonFollower(user, userToFollow);
    }

    @Test
    public void testFollowForSavedUserWithValidUserArgumentToReturnTrue(){
        User userToFollow = userTestHelper.getSavedUserObject("bar_example",
                "123456789", this.connection);
        boolean hasFollowed = follower.follow(userToFollow);
        assertTrue("foo_example did not follow bar_example", hasFollowed);
    }

    @Test
    public void testFollowForSavedUserWithValidArgumentsReturnsFalseOnRefollow(){
        User userToFollow = userTestHelper.getSavedUserObject("bar_example",
                "123456789", this.connection);
        follower.follow(userToFollow);
        boolean hasFollowedAgain = follower.follow(userToFollow);
        assertFalse("foo_example followed bar_example again", hasFollowedAgain);
    }

    @Test
    public void testFollowForSavedUserWithSelfReturnsFalse(){
        boolean hasFollowed = follower.follow(user);
        assertFalse("User must not be able to follow oneself", hasFollowed);
    }

}
