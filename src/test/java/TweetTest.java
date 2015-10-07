import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import static org.junit.Assert.assertEquals;

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

//    new creates a tweet but does not save it in db
//    save on invalid tweet keeps tweet count same
    @Test
    public void newCreatesATweetButDoesNotSaveInDatabase(){
        int countBefore = getTweetsCount();
        Tweet tweet = new Tweet("hello", user.getId());
        int countAfter  = getTweetsCount();
        assertEquals(countBefore, countAfter);
    }

//    save on valid tweet increases tweet count by 1

//    save on valid tweet returns tweet
//    save on invalid tweet returns null
//    save on tweet with valid user_id returns tweet
//    save on tweet with invalid user_id returns null
//    save on tweet with body <= 140 && body >=0 characters
//    returns tweet
//    save on tweet with body > 140 characters returns null
//    getUserId(), getBody() on tweet

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