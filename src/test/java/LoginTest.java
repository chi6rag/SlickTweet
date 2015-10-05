import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Hashtable;
import static org.junit.Assert.assertEquals;

/**
 * Created by chi6rag on 10/5/15.
 */
public class LoginTest {
    Authentication auth = new Authentication();
    Boolean isConnectionSetup = false;
    PreparedStatement preparedStatement = null;
    Connection connection = null;
    Hashtable authDetails;

    @Before
    public void beforeEach(){
        initializeDBConnection();

        // Setup authDetails
        authDetails = new Hashtable();
        authDetails.put("username", "foo_example");
        authDetails.put("password", "123456789");

        // Add user to twitchblade_testing
        try {
            preparedStatement = connection.prepareStatement(
                    "INSERT INTO users(username, password) " +
                    "VALUES(?, ?)"
            );
            preparedStatement.setString(1, "foo_example");
            preparedStatement.setString(2, "123456789");
            preparedStatement.execute();
        } catch (SQLException e) {
            System.out.println("------ Unable to setup Test Data for User ------");
            e.printStackTrace();
        }

    }

    @After
    public void afterEach(){
        // Delete All Users
        try {
            preparedStatement = connection.prepareStatement("DELETE FROM users");
            preparedStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
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

}
