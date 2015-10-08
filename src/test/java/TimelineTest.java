import org.junit.After;
import org.junit.Before;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class TimelineTest {
    DbConnection connection = new DbConnection();
    Timeline timeline;
    User currentUser;
    PreparedStatement preparedStatement;

    @Before
    public void BeforeEach(){
        currentUser = generateUser("foo_example", "123456789",
                this.connection);
        timeline = new Timeline(currentUser);
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

    private void deleteAllTweets(){
        try {
            preparedStatement = this.connection
                    .prepareStatement("DELETE FROM tweets");
            preparedStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
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
