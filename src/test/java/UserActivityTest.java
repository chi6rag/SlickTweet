import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import static org.junit.Assert.assertEquals;

public class UserActivityTest {
    DbConnection connection = new DbConnection();
    PreparedStatement preparedStatement = null;
    User currentUser;
    UserActivity userActivity;

    @Before
    public void beforeEach(){
        currentUser = getUser("foo_example", "123456789", connection);
        userActivity = new UserActivity(currentUser);
    }

    @After
    public void afterEach(){
        deleteAllUsers();
    }

    @Test
    public void testGetsActivityOptions(){
        String activityOptions =
            "\nWelcome foo_example" +
            "\n\n"                  +
            "1. Tweet\n"            +
            "2. Your Timeline\n"    +
            "3. Logout\n"           +
            "Choose: ";
        assertEquals(userActivity.getActivityOptions(),
                activityOptions);
    }

    @Test
    public void askForTweet(){
        String question = "What's in your mind?\n";
        assertEquals(userActivity.askForTweet(), question);
    }

    private User getUser(String username, String password,
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

}