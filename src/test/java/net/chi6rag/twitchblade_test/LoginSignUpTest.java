package net.chi6rag.twitchblade_test;

import net.chi6rag.twitchblade.*;
import test_helpers.*;
import org.junit.*;
import java.io.ByteArrayOutputStream;
import java.util.Hashtable;
import static org.junit.Assert.assertEquals;

public class LoginSignUpTest {
    Authentication auth = new Authentication();
    DbConnection connection = new DbConnection();

    // Objects of helper classes
    UserTestHelper userTestHelper = new UserTestHelper(connection);
    AssertionTestHelper assertionTestHelper = new AssertionTestHelper();
    IOTestHelper ioTestHelper = new IOTestHelper();

    @Before
    public void beforeEach(){
        userTestHelper.createTestUser("foo_example", "123456789");
    }

    @After
    public void afterEach(){
        userTestHelper.deleteAllUsers();
    }

    @Test
    public void testLoginWithValidDetailsReturnsLoggedInUser(){
        Hashtable validUserDetails = userTestHelper
                .getUserDetails("foo_example", "123456789");
        User currentUser = auth.login(validUserDetails);
        Assert.assertEquals(currentUser.getClass().getSimpleName(), "User");
        Assert.assertEquals(currentUser.getUsername(), "foo_example");
        Assert.assertEquals(currentUser.getPassword(), "123456789");
    }

    @Test
    public void testLoginWithInvalidDetailsReturnsNull(){
        Hashtable invalidUserDetails = userTestHelper
                .getUserDetails("baz_example", "123456789");
        assertEquals(auth.login(invalidUserDetails), null);
    }

    @Test
    public void testLoginWithInvalidHashKeysReturnsNull(){
        Hashtable authDetails = new Hashtable();
        authDetails.put("height", 10);
        authDetails.put("width", 10);
        assertEquals(auth.login(authDetails), null);
    }

    @Test
    public void testLoginWithInvalidUsernamePasswordPrintsLoginError(){
        ByteArrayOutputStream consoleOutput = ioTestHelper.mockStdOut();
        Hashtable invalidUserDetails = userTestHelper
                .getUserDetails("ab", "123456789");
        String authErrorMessage = getLoginErrorMessage();
        auth.login(invalidUserDetails);
        assertionTestHelper.assertContains(consoleOutput.toString(),
                authErrorMessage);
        ioTestHelper.setStdOutToDefault();
    }

    @Test
    public void testSignUpWithValidAndUniqueUserDetailsReturnSignedUpUser(){
        Hashtable validUniqueUserDetails = userTestHelper
                .getUserDetails("baz_example", "123456789");
        User currentUser = auth.signUp(validUniqueUserDetails);
        Assert.assertEquals(currentUser.getClass().getSimpleName(), "User");
        Assert.assertEquals(currentUser.getUsername(), "baz_example");
        Assert.assertEquals(currentUser.getPassword(), "123456789");
    }

    @Test
    public void testSignUpWithInvalidDetailsReturnsNull(){
        Hashtable invalidUserDetails = userTestHelper
                .getUserDetails("aa", "123456789");
        assertEquals(auth.signUp(invalidUserDetails), null);
    }

    @Test
    public void testSignUpWithValidButNotUniqueUserDetailsReturnsNull(){
        Hashtable validNotUniqueUserDetails = userTestHelper
                .getUserDetails("foo_example", "123456789");
        assertEquals(auth.signUp(validNotUniqueUserDetails), null);
    }

    @Test
    public void testSignUpWithInvalidUsernamePrintsSignUpError(){
        ByteArrayOutputStream consoleOutput = ioTestHelper.mockStdOut();
        Hashtable invalidUserDetails = userTestHelper
                .getUserDetails("ab", "123456789");
        String signUpErrorMessage = getSignUpErrorMessage();
        auth.signUp(invalidUserDetails);
        assertionTestHelper.assertContains(consoleOutput.toString(),
                signUpErrorMessage);
        ioTestHelper.setStdOutToDefault();
    }

    @Test
    public void testSignUpWithValidButNotUniqueUserDetailsPrintsSignUpError(){
        ByteArrayOutputStream consoleOutput = ioTestHelper.mockStdOut();
        Hashtable validNotUniqueUserDetails = userTestHelper
                .getUserDetails("foo_example", "123456789");
        auth.signUp(validNotUniqueUserDetails);
        String authErrorMessage = getSignUpErrorMessage();
        assertionTestHelper.assertContains(consoleOutput.toString(),
                authErrorMessage);
        ioTestHelper.setStdOutToDefault();
    }

    @Test
    public void testSignUpWithBlankPasswordPrintsSignUpError(){
        ByteArrayOutputStream consoleOutput = ioTestHelper.mockStdOut();
        Hashtable invalidAuthDetails = userTestHelper
                .getUserDetails("baz_example", "");
        auth.signUp(invalidAuthDetails);
        String authErrorMessage = getSignUpErrorMessage();
        assertionTestHelper.assertContains(consoleOutput.toString(),
                authErrorMessage);
        ioTestHelper.setStdOutToDefault();
    }

    @Test
    public void testSignUpWithPasswordLessThanSixCharsPrintsSignUpError(){
        ByteArrayOutputStream consoleOutput = ioTestHelper.mockStdOut();
        Hashtable invalidAuthDetails = userTestHelper
                .getUserDetails("baz_example", "test");
        auth.signUp(invalidAuthDetails);
        String authErrorMessage = getSignUpErrorMessage();
        assertionTestHelper.assertContains(consoleOutput.toString(),
                authErrorMessage);
        ioTestHelper.setStdOutToDefault();
    }


    private String getSignUpErrorMessage(){
        String signUpErrorMessage = "\nInvalid Username or Password\n"            +
                "- Username can only contain letters, numbers and underscores\n"  +
                "  and it can only be 6 to 20 characters long\n"                  +
                "- Password must be at least 6 characters long\n";
        return signUpErrorMessage;
    }

    private String getLoginErrorMessage(){
        return "\nInvalid Username or Password Combination\n";
    }

}