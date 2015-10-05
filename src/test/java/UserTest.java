import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import java.sql.*;
import static org.junit.Assert.assertNotEquals;

public class UserTest {
    PreparedStatement preparedStatement = null;
    Connection connection = null;

    @Before
    public void beforeEach(){
        initializeDBConnection();
    }

    @After
    public void afterEach(){
        deleteAllUsers(connection);
    }

    @Test
    public void testSaveWithValidUserObjectIncreasesUserCountBy1(){
        int beforeCount = getUserCount();
        User user = new User("foo_example", "123456789");
        user.save();
        int afterCount = getUserCount();
        assertNotEquals(beforeCount, afterCount);
    }

    @Test
    public void testSaveWithValidUserObjectReturnsTheSavedUser(){}

    @Test
    public void testSaveWithInvalidUserObjectKeepsUserCountSame(){}

    @Test
    public void testSaveWithInvalidUserObjectReturnsNull(){}

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

    private int getUserCount(){
        int count = 0;
        try {
            Statement countStatement = connection.createStatement();
            ResultSet res = countStatement
                    .executeQuery("SELECT COUNT(*) AS total FROM users");
            count = res.getInt("total");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return count;
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
