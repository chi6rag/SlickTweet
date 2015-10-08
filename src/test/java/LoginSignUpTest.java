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
        assertEquals(currentUser.getClass().getName(), "User");
        assertEquals(currentUser.getUsername(), "foo_example");
        assertEquals(currentUser.getPassword(), "123456789");
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
    public void testSignUpWithValidAndUniqueUserDetailsReturnSignedUpUser(){
        Hashtable validUniqueUserDetails = userTestHelper
                .getUserDetails("baz_example", "123456789");
        User currentUser = auth.signUp(validUniqueUserDetails);
        assertEquals(currentUser.getClass().getName(), "User");
        assertEquals(currentUser.getUsername(), "baz_example");
        assertEquals(currentUser.getPassword(), "123456789");
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
    public void testSignUpWithInvalidUsernamePrintsAuthError(){
        ByteArrayOutputStream consoleOutput = ioTestHelper.mockStdOut();
        Hashtable invalidUserDetails = userTestHelper
                .getUserDetails("ab", "123456789");
        String authErrorMessage = getAuthErrorMessage();
        auth.signUp(invalidUserDetails);
        assertionTestHelper.assertContains(consoleOutput.toString(),
                authErrorMessage);
        ioTestHelper.setStdOutToDefault();
    }

    @Test
    public void testLoginWithInvalidUsernamePasswordPrintsAuthError(){
        ByteArrayOutputStream consoleOutput = ioTestHelper.mockStdOut();
        Hashtable invalidUserDetails = userTestHelper
                .getUserDetails("ab", "123456789");
        String authErrorMessage = getAuthErrorMessage();
        auth.login(invalidUserDetails);
        assertionTestHelper.assertContains(consoleOutput.toString(),
                authErrorMessage);
        ioTestHelper.setStdOutToDefault();
    }

    private String getAuthErrorMessage(){
        String authErrorMessage = "\nUsername or Password Not Proper\n" +
        "Username can only contain letters, numbers and underscores\n"  +
        "and it can only be 6 to 20 characters\n";
        return authErrorMessage;
    }

}