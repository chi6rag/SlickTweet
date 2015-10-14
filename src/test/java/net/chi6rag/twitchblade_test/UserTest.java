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
        User userToFollow = userTestHelper.getSavedUserObject("bar_example",
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

}
