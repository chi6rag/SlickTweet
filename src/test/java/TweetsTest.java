import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Hashtable;
import static org.junit.Assert.assertEquals;

public class TweetsTest {
    PreparedStatement preparedStatement = null;
    Tweets allTweets;
    DbConnection connection = new DbConnection();
    User user;

    @Before
    public void beforeEach(){
        user = generateUser("foo_example", "123456789", connection);
        allTweets = new Tweets(this.connection);
    }

    @After
    public void afterEach(){
        deleteAllTweets();
        deleteAllUsers();
    }

    @Test
    public void testWhereMethodWithValidUserIdReturnsTweetsForUser(){
        Hashtable queryHash = new Hashtable();
        queryHash.put("userId", user.getId());
        Tweets[] tweets = allTweets.where(queryHash);
        for(int i=0; i<tweets.length; i++){
            assertEquals((tweets[i]).getId(), user.getId());
        }
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