import org.junit.After;
import org.junit.Before;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class TweetTest {
    PreparedStatement preparedStatement = null;
    DbConnection connection = new DbConnection();
    User user;

    @Before
    public void beforeEach(){
        user = generateUser("foo_example", "123456789", connection);
    }

    @After
    public void afterEach(){
        deleteAllTweets();
        deleteAllUsers();
    }

    private User generateUser(String username, String password,
                              DbConnection connection){
        return (new User(username, password, connection)).save();
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

    private void deleteAllTweets(){
        try {
            preparedStatement = this.connection
                    .prepareStatement("DELETE FROM tweets");
            preparedStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}