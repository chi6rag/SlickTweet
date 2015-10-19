package net.chi6rag.twitchblade_test;

import net.chi6rag.twitchblade.*;
import test_helpers.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import java.util.Hashtable;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class UsersTest {
    DbConnection connection;
    Users allUsers;

    // Objects of helper classes
    UserTestHelper userTestHelper;

    @Before
    public void beforeEach(){
        connection = new DbConnection("testing");
        userTestHelper = new UserTestHelper(connection);
        userTestHelper.setupTestUsers();
        allUsers = new Users(connection);
    }

    @After
    public void afterEach(){
        userTestHelper.deleteAllUsers();
        connection.close();
    }

    @Test
    public void returnsFoundUserWhenCalledFindWithValidArguments(){
        Hashtable validAuthDetails = userTestHelper
                .getUserDetails("foo_example", "123456789");
        User foundUser = allUsers.find(validAuthDetails);
        assertEquals(foundUser.getId().getClass()
                .getSimpleName(), "Integer");
        assertEquals(foundUser.getClass().getSimpleName(), "User");
        assertEquals(foundUser.getUsername(), "foo_example");
        assertEquals(foundUser.getPassword(), "123456789");
    }

    @Test
    public void returnsNullWhenCalledWithWrongUserDetailsHash(){
        Hashtable authDetails = getWrongUserDetailsHash();
        User foundUser = allUsers.find(authDetails);
        assertEquals(foundUser, null);
    }

    @Test
    public void testFindByUsernameForValidUsernameReturnsUser(){
        User foundUser = allUsers.findByUsername("foo_example");
        assertEquals(foundUser.getUsername(), "foo_example");
        assertEquals(foundUser.getId().getClass()
                .getSimpleName(), "Integer");
        assertEquals(foundUser.getPassword(), "123456789");
    }

    @Test
    public void testFindByUsernameForInvalidUsernameReturnsNull(){
        User foundUser = allUsers.findByUsername("baz_example");
        assertNull(foundUser);
    }

    @Test
    public void returnsNullWhenCalledFindWithInexistentUserDetails(){
        Hashtable inexistentAuthDetails = userTestHelper
                .getUserDetails("baz_example", "123456789");
        User foundUser = allUsers.find(inexistentAuthDetails);
        assertEquals(foundUser, null);
    }

    private Hashtable getWrongUserDetailsHash(){
        Hashtable authDetails = new Hashtable();
        authDetails.put("height", 10);
        authDetails.put("width", 10);
        return authDetails;
    }

}
