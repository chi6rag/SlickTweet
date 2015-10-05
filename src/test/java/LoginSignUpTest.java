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
        createTestUser(connection, "foo_example", "123456789");
    }

    @After
    public void afterEach(){
        deleteAllUsers(connection);
    }

    @Test
    public void testLoginWithValidDetailsReturnsLoggedInUser(){
        User currentUser = auth.login(getValidUserDetails());
        assertEquals(currentUser.getClass(), User);
        assertEquals(currentUser.getUsername, "foo_example");
        assertEquals(currentUser.getPassword, "123456789");
    }

    @Test
    public void testLoginWithInvalidDetailsReturnsNull(){
        assertEquals(auth.login(getInvalidUserDetails()), null);
    }

    @Test
    public void testSignUpWithValidAndUniqueUserDetailsReturnSignedUpUser(){
        User currentUser = auth.signUp(getValidUniqueUserDetails());
        assertEquals(currentUser.getClass(), User);
        assertEquals(currentUser.getUsername, "foo_example");
        assertEquals(currentUser.getPassword, "123456789");
    }

    @Test
    public void testSignUpWithInvalidDetailsReturnsNull(){
        assertEquals(auth.signUp(getInvalidUserDetails()), null);
    }

    @Test
    public void testSignUpWithValidButNotUniqueUserDetailsReturnsNull(){
        assertEquals(auth.signUp(getValidUserDetails()), null);
    }

    private void createTestUser(Connection connection, String username, String password){
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

    private Hashtable getValidUserDetails(){
        Hashtable userDetails = new Hashtable();
        userDetails.put("username", "foo_example");
        userDetails.put("password", "123456789");
        return userDetails;
    }

    private Hashtable getValidUniqueUserDetails(){
        Hashtable userDetails = new Hashtable();
        userDetails.put("username", "bar_example");
        userDetails.put("password", "123456789");
        return userDetails;
    }

    private Hashtable getInvalidUserDetails(){
        Hashtable userDetails = new Hashtable();
        userDetails.put("username", "foo_example");
        userDetails.put("password", "123456789");
        return userDetails;
    }

    private void deleteAllUsers(Connection connection){
        try {
            preparedStatement = connection.prepareStatement("DELETE FROM users");
            preparedStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}