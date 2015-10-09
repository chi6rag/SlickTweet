package net.chi6rag.twitchblade_test;

import net.chi6rag.twitchblade.*;
import test_helpers.*;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import java.util.Hashtable;

public class UsersTest {
    DbConnection connection = new DbConnection();
    Users allUsers = new Users(connection);

    // Objects of helper classes
    UserTestHelper userTestHelper = new UserTestHelper(connection);

    @Before
    public void beforeEach(){
        userTestHelper.setupTestUsers();
    }

    @After
    public void afterEach(){
        userTestHelper.deleteAllUsers();
    }

    @Test
    public void returnsFoundUserWhenCalledFindWithValidArguments(){
        Hashtable validAuthDetails = userTestHelper
                .getUserDetails("foo_example", "123456789");
        User foundUser = allUsers.find(validAuthDetails);
        Assert.assertEquals(foundUser.getId().getClass()
                .getSimpleName(), "Integer");
        Assert.assertEquals(foundUser.getClass().getSimpleName(), "User");
        Assert.assertEquals(foundUser.getUsername(), "foo_example");
        Assert.assertEquals(foundUser.getPassword(), "123456789");
    }

    @Test
    public void returnsNullWhenCalledFindWithInexistentUserDetails(){
        Hashtable inexistentAuthDetails = userTestHelper
                .getUserDetails("baz_example", "123456789");
        User foundUser = allUsers.find(inexistentAuthDetails);
        Assert.assertEquals(foundUser, null);
    }

    @Test
    public void returnsNullWhenCalledWithWrongUserDetailsHash(){
        Hashtable authDetails = getWrongUserDetailsHash();
        User foundUser = allUsers.find(authDetails);
        Assert.assertEquals(foundUser, null);
    }

    private Hashtable getWrongUserDetailsHash(){
        Hashtable authDetails = new Hashtable();
        authDetails.put("height", 10);
        authDetails.put("width", 10);
        return authDetails;
    }

}
