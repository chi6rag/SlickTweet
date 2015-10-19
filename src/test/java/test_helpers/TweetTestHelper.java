package test_helpers;

import net.chi6rag.twitchblade.*;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import static org.junit.Assert.assertTrue;

public class TweetTestHelper {
    private final DbConnection connection;
    private final AssertionTestHelper assertionTestHelper;
    PreparedStatement preparedStatement;

    public TweetTestHelper(DbConnection connection) {
        this.assertionTestHelper = new AssertionTestHelper();
        this.connection = connection;
    }


    public void deleteAllTweets(){
        try {
            preparedStatement = this.connection
                    .prepareStatement("DELETE FROM tweets");
            preparedStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public String[] getTweetBodies(ArrayList<Tweet> tweets){
        String[] tweetBodies = new String[tweets.size()];
        for(int i=0; i<tweets.size(); i++){ tweetBodies[i] = tweets.get(i).getBody(); }
        return tweetBodies;
    }

    public Tweet getSavedTweetObject(String body, Integer userId,
                                DbConnection connection){
        return (new Tweet(body, userId, connection)).save();
    }

    public int getTweetsCount(){
        int count = 0;
        try {
            Statement countStatement = this.connection.createStatement();
            ResultSet res = countStatement.executeQuery("SELECT COUNT(*) AS total FROM tweets");
            if( res.next() ) count = res.getInt("total");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return count;
    }

    public void validatePresenceOfTweetBodies(String[] expectedTweetsBodies,
                                               String[] queriedTweetBodies) {
        String tweetBody;
        for(int i=0; i<expectedTweetsBodies.length; i++){
            tweetBody = expectedTweetsBodies[i];
            assertTrue("expectedTweetsBodies array does not contain " + tweetBody,
                    assertionTestHelper.containsElement(queriedTweetBodies, tweetBody));
        }
    }

    public String getInvalidTweetBody(){
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

    public String getTweetErrorMessage(){
        String errorMessage = "\nTweet cannot be posted. Note: \n"     +
                      "- tweet's length should be upto 140 characters\n" +
                      "- tweet should not be blank";
        return errorMessage;
    }

    public void createSampleTweetsFor(User user, String... tweetBodies){
        for(int i=0; i<tweetBodies.length; i++){
            String body = tweetBodies[i];
            (new Tweet(body, user.getId(), connection)).save();
        }
    }

}
