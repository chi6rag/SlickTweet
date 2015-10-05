import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Hashtable;
import static org.junit.Assert.assertEquals;

public class LoginTest {
    Authentication auth = new Authentication();
    Boolean isConnectionSetup = false;
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
    public void testLoginWithValidDetails(){
        assertEquals(auth.login(getValidAuthDetails()), true);
    }

    @Test
    public void testSignUpWithValidDetails(){
        assertEquals(auth.signUp(getValidAuthDetails()), true);
    }

    private void initializeDBConnection(){
        if(!isConnectionSetup){
            try {
                Class.forName("org.postgresql.Driver");
            } catch (ClassNotFoundException e) {
                System.out.println("PostgreSQL JDBC Driver not Found!");
                e.printStackTrace();
                return;
            }
            try {
                connection = DriverManager.getConnection(
                        "jdbc:postgresql://localhost:5432/twitchblade_testing",
                        "chi6rag", "");
                isConnectionSetup = true;
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private void createTestUser(Connection connection, String username, String password){
        try {
            preparedStatement = connection.prepareStatement(
                    "INSERT INTO users(username, password) " +
                            "VALUES(?, ?)"
            );
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);
            preparedStatement.execute();
        } catch (SQLException e) {
            System.out.println("------ Unable to setup Test Data for User ------");
            e.printStackTrace();
        }
    }

    private Hashtable getValidAuthDetails(){
        Hashtable authDetails = new Hashtable();
        authDetails.put("username", "foo_example");
        authDetails.put("password", "123456789");
        return authDetails;
    }

    private Hashtable getInvalidAuthDetails(){
        Hashtable authDetails = new Hashtable();
        authDetails.put("username", "foo_example");
        authDetails.put("password", "123456789");
        return authDetails;
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
