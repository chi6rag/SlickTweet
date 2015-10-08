import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class UserActivityTest {
    DbConnection connection = new DbConnection();
    PreparedStatement preparedStatement = null;
    User currentUser;
    UserActivity userActivity;

    @Before
    public void beforeEach(){
        currentUser = generateUser("foo_example", "123456789", connection);
        userActivity = new UserActivity(currentUser);
    }

    @After
    public void afterEach(){
        deleteAllTweets();
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
    public void testAsksForTweet(){
        String question = "What's in your mind?";
        assertEquals(userActivity.askForTweet(), question);
    }

    @Test
    public void tweetWithValidBodyReturnsTweet(){
        String validTweetBody = "hello";
        Tweet tweet = userActivity.tweet(validTweetBody);
        assertEquals(tweet.getClass().getName(), "Tweet");
        assertEquals(tweet.getId().getClass()
                .getSimpleName(), "Integer");
        assertEquals(tweet.getBody(), "hello");
        assertEquals(tweet.getUserId(), currentUser.getId());
    }

    @Test
    public void tweetWithInvalidBodyReturnsNull(){
        Tweet tweet = userActivity.tweet(getInvalidTweetBody());
        assertEquals(tweet, null);
    }

    @Test
    public void tweetWithInvalidBodyPrintsErrorOnStdOut(){
        ByteArrayOutputStream consoleOutput = new ByteArrayOutputStream();
        System.setOut(new PrintStream(consoleOutput));
        Tweet tweet = userActivity.tweet(getInvalidTweetBody());
        CharSequence errorMessage = "Tweet cannot be saved";
        assertTrue(consoleOutput.toString().contains(errorMessage));
        System.setOut(System.out);
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

    private String getInvalidTweetBody(){
        String tweetBody = "Lorem ipsum dolor sit amet, consectetur adipisicing elit." +
                "Rem, incidunt eos delectus veniam cupiditate possimus in velit, quia"     +
                " sed perspiciatis similique suscipit tempora laborum reprehenderit "      +
                "maxime nulla. Maiores, id, error.\n"                                      +
                "Lorem ipsum dolor sit amet, consectetur adipisicing elit. Recusandae "    +
                "beatae nostrum maiores voluptatum atque repellat necessitatibus ullam "   +
                "molestias, mollitia neque quidem molestiae totam commodi ut sed dolorum." +
                " Adipisci amet, molestias.\n"                                             +
                "Lorem ipsum dolor sit amet, consectetur adipisicing elit. Dolorum "       +
                "incidunt tenetur error in veniam, vitae aut aliquid repellat dolores "    +
                "alias necessitatibus nobis quidem unde ducimus. Repudiandae mollitia "    +
                "nostrum, possimus velit.";
        return tweetBody;
    }

}