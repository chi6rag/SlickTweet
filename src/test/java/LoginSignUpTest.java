import org.junit.*;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Hashtable;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class LoginSignUpTest {
    Authentication auth = new Authentication();
    PreparedStatement preparedStatement = null;
    DbConnection connection = new DbConnection();

    @Before
    public void beforeEach(){
        createTestUser("foo_example", "123456789");
    }

    @After
    public void afterEach(){
        deleteAllUsers();
    }

    @Test
    public void testLoginWithValidDetailsReturnsLoggedInUser(){
        Hashtable validUserDetails = getUserDetails("foo_example", "123456789");
        User currentUser = auth.login(validUserDetails);
        assertEquals(currentUser.getClass().getName(), "User");
        assertEquals(currentUser.getUsername(), "foo_example");
        assertEquals(currentUser.getPassword(), "123456789");
    }

    @Test
    public void testLoginWithInvalidDetailsReturnsNull(){
        Hashtable invalidUserDetails = getUserDetails("baz_example", "123456789");
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
        Hashtable validUniqueUserDetails = getUserDetails("baz_example",
                "123456789");
        User currentUser = auth.signUp(validUniqueUserDetails);
        assertEquals(currentUser.getClass().getName(), "User");
        assertEquals(currentUser.getUsername(), "baz_example");
        assertEquals(currentUser.getPassword(), "123456789");
    }

    @Test
    public void testSignUpWithInvalidDetailsReturnsNull(){
        Hashtable invalidUserDetails = getUserDetails("aa", "123456789");
        assertEquals(auth.signUp(invalidUserDetails), null);
    }

    @Test
    public void testSignUpWithValidButNotUniqueUserDetailsReturnsNull(){
        Hashtable validNotUniqueUserDetails = getUserDetails("foo_example",
                "123456789");
        assertEquals(auth.signUp(validNotUniqueUserDetails), null);
    }

    @Test
    public void testSignUpWithInvalidUsernamePrintsAuthError(){
        ByteArrayOutputStream consoleOutput = mockStdOut();
        Hashtable invalidUserDetails = getUserDetails("ab", "123456789");
        String authErrorMessage =
            "\nUsername or Password Not Proper\n" +
            "Username can only contain letters, numbers and underscores\n" +
            "and it can only be 6 to 20 characters\n";
        auth.signUp(invalidUserDetails);
        assertContains(consoleOutput.toString(), authErrorMessage);
        setStdOutToDefault();
    }

    @Test
    public void testLoginWithInvalidUsernamePasswordPrintsAuthError(){
        ByteArrayOutputStream consoleOutput = mockStdOut();
        Hashtable invalidUserDetails = getUserDetails("ab", "123456789");
        String authErrorMessage =
            "\nUsername or Password Not Proper\n" +
            "Username can only contain letters, numbers and underscores\n" +
            "and it can only be 6 to 20 characters\n";
        auth.login(invalidUserDetails);
        assertContains(consoleOutput.toString(), authErrorMessage);
        setStdOutToDefault();
    }

    private void assertContains(String parentString, String subString){
        assertTrue(parentString.contains(subString));
    }

    private ByteArrayOutputStream mockStdOut(){
        ByteArrayOutputStream consoleOutput = new ByteArrayOutputStream();
        System.setOut(new PrintStream(consoleOutput));
        return consoleOutput;
    }

    private void setStdOutToDefault(){
        System.setOut(System.out);
    }

    private void createTestUser(String username, String password){
        try {
            preparedStatement = connection.prepareStatement(
                    "INSERT INTO users(username, password) VALUES(?, ?)"
            );
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);
            preparedStatement.execute();
        } catch (SQLException e) {
            System.out.println("------ Unable to setup Test Data for User ------");
            e.printStackTrace();
        }
    }

    private Hashtable getUserDetails(String username, String password){
        Hashtable userDetails = new Hashtable();
        userDetails.put("username", username);
        userDetails.put("password", password);
        return userDetails;
    }

    private void deleteAllUsers(){
        try {
            preparedStatement = this.connection
                    .prepareStatement("DELETE FROM users");
            preparedStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}