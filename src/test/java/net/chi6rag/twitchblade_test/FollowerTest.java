package net.chi6rag.twitchblade_test;

import net.chi6rag.twitchblade.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import test_helpers.RelationshipTestHelper;
import test_helpers.UserTestHelper;
import static junit.framework.Assert.assertFalse;

public class FollowerTest {
    DbConnection connection = new DbConnection();
    Follower follower;
    User user;

    // Objects of helper classes
    UserTestHelper userTestHelper = new UserTestHelper(connection);
    RelationshipTestHelper relationshipTestHelper = new
            RelationshipTestHelper(connection);

    @Before
    public void beforeEach(){
        user = userTestHelper.getSavedUserObject("foo_example",
                "123456789", connection);
        follower = new Follower(user, connection);
    }

    @After
    public void afterEach(){
        relationshipTestHelper.deleteAllRelationships();
        userTestHelper.deleteAllUsers();
    }

    @Test
    public void testFollowForUnsavedUserWithValidArgumentsReturnsFalse(){
        User userToFollow = userTestHelper.getSavedUserObject("bar_example",
                "123456789", this.connection);
        boolean hasFollowed = follower.follow("bar_example");
        relationshipTestHelper.validateNonFollower(user, userToFollow);
        assertFalse("Unsaved user followed User bar_example", hasFollowed);
    }

}
