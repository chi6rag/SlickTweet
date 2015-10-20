package net.chi6rag.twitchblade;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

public class Tweet {
    private Integer id;
    private String body;
    private Integer userId;
    private Date createdAt;
    private DbConnection connection;
    private PreparedStatement tweetSavePreparedStatement;

    public Tweet(String body, Integer userId, DbConnection connection){
        this.id = null;
        this.body = body;
        this.userId = userId;
        this.createdAt = null;
        this.connection = connection;
    }

    public Tweet(Integer id, String body, Integer userId, Date createdAt,
          DbConnection connection){
        this.id = id;
        this.body = body;
        this.userId = userId;
        this.createdAt = createdAt;
        this.connection = connection;
    }

    public static Tweet buildFromDbResult(ResultSet res, DbConnection connection) throws SQLException {
        Integer id = res.getInt("id");
        String body = res.getString("body");
        Integer userId = res.getInt("user_id");
        Date createdAt = new Date(res.getTimestamp("created_at")
                .getTime());
        return new Tweet(id, body, userId, createdAt,
                connection);
    }

    public Tweet save(){
        if(!hasValidAttributes()){ return null; }
        prepareTweetSaveStatement();
        ResultSet res = insertUserIntoDB(this.body, this.userId);
        if(res != null) return getTweetFromDBResult(res);
        return null;
    }

    public Integer getId(){
        return this.id;
    }

    public String getBody(){
        return this.body;
    }

    public Integer getUserId(){
        return this.userId;
    }

    public Date getCreatedAt(){ return this.createdAt; }

    private void prepareTweetSaveStatement(){
        try {
            this.tweetSavePreparedStatement = this.connection.prepareStatement("INSERT" +
                    " INTO tweets(body, user_id) VALUES(?, ?) RETURNING id, body," +
                    " user_id, created_at");
        } catch (SQLException e) {
            // e.printStackTrace();
        }
    }

    private boolean hasValidAttributes(){
        if(this.body.length() > 140){ return false; }
        return true;
    }

    private ResultSet insertUserIntoDB(String body, Integer userId){
        if(body == null   || userId == null) return null;
        if(body.isEmpty()) return null;
        ResultSet res = null;
        try {
            this.tweetSavePreparedStatement.setString(1, body);
            this.tweetSavePreparedStatement.setInt(2, userId);
            res = this.tweetSavePreparedStatement.executeQuery();
        } catch (SQLException e) {
            // e.printStackTrace();
        }
        return res;
    }

    private Tweet getTweetFromDBResult(ResultSet res){
        try {
            if(res.next()){
                return buildFromDbResult(res, connection);
            }
        } catch (SQLException e) {
            // e.printStackTrace();
        }
        return null;
    }

}
