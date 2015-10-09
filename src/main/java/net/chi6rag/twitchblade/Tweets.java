package net.chi6rag.twitchblade;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;

public class Tweets {
    private final DbConnection connection;
    private PreparedStatement tweetsWherePreparedStatement;

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

    private ArrayList<Tweet> getTweetsFromDbResult(ResultSet res){
        ArrayList<Tweet> tweets = new ArrayList<Tweet>();
        try {
            while(res.next()){
                Integer id = res.getInt("id");
                String body = res.getString("body");
                Integer userId = res.getInt("user_id");
                Date createdAt = new Date(res.getTimestamp("created_at")
                        .getTime());
                tweets.add(new Tweet(id, body, userId, createdAt,
                        this.connection));
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
