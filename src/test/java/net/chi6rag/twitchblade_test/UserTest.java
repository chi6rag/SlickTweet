package net.chi6rag.twitchblade_test;

import net.chi6rag.twitchblade.*;
import test_helpers.*;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import java.util.ArrayList;
import static org.junit.Assert.*;

public class UserTest {
    DbConnection connection = new DbConnection();

    // Objects of helper classes
    UserTestHelper userTestHelper = new UserTestHelper(connection);
    RelationshipTestHelper relationshipTestHelper = new
            RelationshipTestHelper(connection);

    @After
    public void afterEach(){
        relationshipTestHelper.deleteAllRelationships();
        userTestHelper.deleteAllUsers();
    }

    @Test
    public void testNewWithValidDetailsSetsIdNull(){
        User user = new User("foo_example", "123456789", this.connection);
        Assert.assertEquals(user.getId(), null);
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

}
