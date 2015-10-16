package net.chi6rag.twitchblade;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Retweet {
    private Integer userId;
    private Integer tweetId;
    private DbConnection connection;
    private PreparedStatement retweetSavePreparedStatement;

    public Retweet(Integer tweetId, Integer userId, DbConnection connection){
        this.tweetId = tweetId;
        this.userId = userId;
        this.connection = connection;
    }

    public boolean save(){
        prepareRetweetSaveStatement();
        return insertRetweetIntoDB(this.tweetId, this.userId);
    }

    private void prepareRetweetSaveStatement(){
        try {
            this.retweetSavePreparedStatement = this.connection.prepareStatement(
                " INSERT INTO retweets(tweet_id, retweeter_id) VALUES(?, ?)" +
                " RETURNING id"
            );
        } catch (SQLException e) {
            // e.printStackTrace();
        }
    }

    private boolean insertRetweetIntoDB(Integer tweetId, Integer userId){
        boolean isSaved = false;
        try {
            this.retweetSavePreparedStatement.setInt(1, tweetId);
            this.retweetSavePreparedStatement.setInt(2, userId);
            isSaved = this.retweetSavePreparedStatement.execute();
        } catch (SQLException e) {
            // e.printStackTrace();
        }
        return isSaved;
    }

}