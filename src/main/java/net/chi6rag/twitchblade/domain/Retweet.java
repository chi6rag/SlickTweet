package net.chi6rag.twitchblade.domain;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Retweet {
    private User user;
    private Integer tweetId;
    private DbConnection connection;
    private PreparedStatement retweetSavePreparedStatement;

    public Retweet(Integer tweetId, User user, DbConnection connection){
        this.tweetId = tweetId;
        this.user = user;
        this.connection = connection;
    }

    public boolean save(){
        prepareRetweetSaveStatement();
        if(this.user.hasTweetByID(this.tweetId) || !this.user.isValid()) return false;
        return insertRetweetIntoDB(this.tweetId, this.user.getId());
    }

    private void prepareRetweetSaveStatement(){
        try {
            this.retweetSavePreparedStatement = this.connection.prepareStatement(
                " INSERT INTO retweets(tweet_id, retweeter_id) VALUES(?, ?)" +
                " RETURNING id"
            );
        } catch (SQLException e) {
            e.printStackTrace();
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