package net.chi6rag.twitchblade;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Hashtable;

public class Tweets {
    private final DbConnection connection;
    private PreparedStatement tweetsWherePreparedStatement;
    private PreparedStatement tweetsForTimelinePreparedStatement;

    public Tweets(DbConnection connection){
        this.connection = connection;
    }

    public ArrayList<Tweet> where(Hashtable query){
        ArrayList<Tweet> tweets = null;
        prepareTweetsWhereStatement();
        Integer userId = (Integer) query.get("userId");
        ResultSet res = queryTweetsFromDB(userId);
        if(res != null) tweets = getTweetsFromDbResult(res);
        return tweets;
    }

    public ArrayList<Tweet> forTimelineOf(Integer userId){
        ArrayList<Tweet> tweets = new ArrayList<Tweet>();
        prepareTweetsForTimelineStatement();
        ResultSet res = queryTweetsForTimelineFromDb(userId);
        if(res != null) tweets = getTweetsFromDbResult(res);
        return tweets;
    }

    private ResultSet queryTweetsForTimelineFromDb(Integer userId) {
        if(userId < 0) return null;
        ResultSet res = null;
        try {
            tweetsForTimelinePreparedStatement.setInt(1, userId);
            tweetsForTimelinePreparedStatement.setInt(2, userId);
            res = this.tweetsForTimelinePreparedStatement.executeQuery();
        } catch (SQLException e) {
            // e.printStackTrace();
        }
        return res;
    }

    private void prepareTweetsWhereStatement(){
        if(this.tweetsWherePreparedStatement == null){
            try {
                this.tweetsWherePreparedStatement = this.connection
                        .prepareStatement("SELECT * FROM tweets WHERE user_id=?");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private void prepareTweetsForTimelineStatement(){
        if(this.tweetsForTimelinePreparedStatement == null){
            try {
                this.tweetsForTimelinePreparedStatement = this.connection
                    .prepareStatement(
                            "(SELECT T.id, T.body, T.user_id, T.created_at" +
                                    " FROM relationship AS R INNER JOIN tweets AS T" +
                                    " ON R.followed_id=T.user_id WHERE R.follower_id=?)" +
                                    " UNION (SELECT * FROM tweets AS T WHERE T.user_id=?)" +
                                    " ORDER BY created_at DESC RETURNING T.id, T.body," +
                                    " T.user_id, T.created_at");
            } catch (SQLException e) {
                // e.printStackTrace();
            }
        }
    }

    private ArrayList<Tweet> getTweetsFromDbResult(ResultSet res){
        ArrayList<Tweet> tweets = new ArrayList<Tweet>();
        try {
            while(res.next()){
                tweets.add(Tweet.buildFromDbResult(res, this.connection));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tweets;
    }

    private ResultSet queryTweetsFromDB(Integer userId){
        if(userId < 0) return null;
        ResultSet res = null;
        try {
            this.tweetsWherePreparedStatement.setInt(1, userId);
            res = this.tweetsWherePreparedStatement.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return res;
    }

}
