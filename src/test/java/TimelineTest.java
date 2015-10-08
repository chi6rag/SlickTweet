import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import static org.junit.Assert.assertEquals;

public class TimelineTest {
    DbConnection connection = new DbConnection();
    Timeline timeline;
    User currentUser;
    PreparedStatement preparedStatement;

    @Before
    public void BeforeEach(){
        currentUser = generateUser("foo_example", "123456789",
                this.connection);
        timeline = new Timeline(currentUser, this.connection);
    }

    @After
    public void afterEach(){
        deleteAllTweets();
        deleteAllUsers();
    }

    @Test
    public void testGetTweetsForValidUserIdReturnsUsersTweets(){
        generateTweet("hello", currentUser.getId(), this.connection);
        ArrayList<Tweet> tweets = timeline.getTweets();
        for(int i=0; i<tweets.size(); i++){
            assertEquals( (tweets.get(i)).getUserId(), currentUser.getId() );
        }
    }

    private User generateUser(String username, String password,
                              DbConnection connection){
        return (new User(username, password, connection)).save();
    }

    private Tweet generateTweet(String body, Integer userId,
                                DbConnection connection){
        return (new Tweet(body, userId, connection)).save();
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
