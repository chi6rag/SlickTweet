import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mockito;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Hashtable;

import static java.sql.DriverManager.getConnection;
import static org.junit.Assert.assertEquals;

/**
 * Created by chi6rag on 10/5/15.
 */
public class LoginTest {
    Authentication auth = new Authentication();
    Boolean isConnectionSetup = false;
    PreparedStatement deleteUsersStatement;
    Connection connection;
    Hashtable authDetails;

    @Before
    public void initializeDBConnectionBeforeAll(){
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
