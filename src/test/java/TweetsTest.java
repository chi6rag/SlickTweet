import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
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
        generateTweet("hello", user.getId(), this.connection);
        Hashtable queryHash = new Hashtable();
        queryHash.put("userId", user.getId());
        ArrayList<Tweet> tweets = allTweets.where(queryHash);
        for(int i=0; i<tweets.size(); i++){
            assertEquals( (tweets.get(i) ).getUserId(), user.getId());
            assertEquals( (tweets.get(i) ).getClass().getName(), "Tweet");
        }
    }

    @Test
    public void testWhereMethodWithNegativeUserIdReturnsNull(){
        Hashtable queryHash = new Hashtable();
        queryHash.put("userId", -999);
        ArrayList<Tweet> tweets = allTweets.where(queryHash);
        assertEquals(tweets, null);
    }

    @Test
    public void testWhereMethodWithInvalidUserIdReturnsEmptyArrayList(){
        Hashtable queryHash = new Hashtable();
        queryHash.put("userId", 987654);
        ArrayList<Tweet> tweets = allTweets.where(queryHash);
        assertEquals(tweets.size(), 0);
    }

    @Test
    public void testWhereMethodWithValidUserButNoTweetsReturnsEmptyArrayList(){
        Hashtable queryHash = new Hashtable();
        queryHash.put("userId", user.getId());
        ArrayList<Tweet> tweets = allTweets.where(queryHash);
        assertEquals(tweets.size(), 0);
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