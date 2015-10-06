import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Hashtable;
import static org.junit.Assert.assertEquals;

public class LoginSignUpTest {
    Authentication auth = new Authentication();
    PreparedStatement preparedStatement = null;
    Connection connection = null;

    @Before
    public void beforeEach(){
        initializeDBConnection();
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
    public void testLoginWithInvalidHashKaysReturnsNull(){
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

    private void initializeDBConnection(){
        if(this.connection == null){
            try {
                Class.forName("org.postgresql.Driver");
            } catch (ClassNotFoundException e) {
                System.out.println("PostgreSQL JDBC Driver not Found!");
                e.printStackTrace();
                return;
            }
            try {
                this.connection = DriverManager.getConnection(
                        "jdbc:postgresql://localhost:5432/twitchblade_testing",
                        "chi6rag", "");
            } catch (SQLException e) {
                e.printStackTrace();
            }
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