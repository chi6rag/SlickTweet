package net.chi6rag.twitchblade_test;

import net.chi6rag.twitchblade.*;
import org.junit.After;
import org.junit.Before;
import test_helpers.RelationshipTestHelper;
import test_helpers.UserTestHelper;

public class FollowerTest {
    DbConnection connection = new DbConnection();
    Follower follower;

    // Objects of helper classes
    UserTestHelper userTestHelper = new UserTestHelper(connection);
    RelationshipTestHelper relationshipTestHelper = new
            RelationshipTestHelper(connection);

    @Before
    public void beforeEach(){
        User currentUser = userTestHelper.getSavedUserObject("foo_example",
                "123456789", connection);
        follower = new Follower(currentUser);
    }

    @After
    public void afterEach(){
        relationshipTestHelper.deleteAllRelationships();
        userTestHelper.deleteAllUsers();
    }

}
