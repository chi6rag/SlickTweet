package net.chi6rag.twitchblade;

import java.sql.*;
import java.util.ArrayList;

public class User {
    String username;
    String password;
    Integer id;
    DbConnection connection;
    PreparedStatement userSavePreparedStatement = null;
    PreparedStatement usersFollowersPreparedStatement = null;
    PreparedStatement usersTweetCountByIdPreparedStatement = null;
    Users allUsers;

    public User(String username, String password, DbConnection connection){
        this.id = null;
        this.connection = connection;
        this.username = username;
        this.password = password;
    }

    public User(Integer id, String username, String password,
                 DbConnection connection){
        this.id         = id;
        this.username   = username;
        this.password   = password;
        this.connection = connection;
    }

    public User save() {
        prepareUserSaveStatement();
        ResultSet res = null;
        res = insertUserIntoDB(this.username, this.password);
        if(res != null) return getUserFromDBResult(res);
        return null;
    }

    public boolean hasTweetByID(Integer id){
        if(!this.isValid()) return false;
        prepareUsersTweetCountByIdStatement();
        int count = getTweetCountByID(id);
        return (count > 0 ? true : false);
    }

    public boolean retweet(int tweetId){
        Retweet retweet = new Retweet(tweetId, this, connection);
        return retweet.save();
    }

    public boolean isValid(){
        return (this.getId() == null) ? false : true;
    }

    public ArrayList<User> followers(){
        prepareUsersFollowersStatement();
        ResultSet res = findFollowersFromDB(this.getUsername());
        return Users.buildFromDbResult(res, this.connection);
    }

    public String getUsername(){
        return this.username;
    }

    public String getPassword(){
        return this.password;
    }

    public Integer getId() { return this.id; }

    public boolean follow(String username) {
        allUsers = new Users(this.connection);
        User userToFollow = allUsers.findByUsername(username);
        if(userToFollow == null) return false;
        Follower follower = new Follower(this, this.connection);
        return follower.follow(userToFollow);
    }

    private int getTweetCountByID(Integer id){
        int count = 0;
        try {
            this.usersTweetCountByIdPreparedStatement.setInt(1, this.getId());
            this.usersTweetCountByIdPreparedStatement.setInt(2, id);
            ResultSet res = this.usersTweetCountByIdPreparedStatement.executeQuery();
            if(res.next()){ count = res.getInt("tweets_count"); }
        } catch (SQLException e) {
            // e.printStackTrace();
        }
        return count;
    }

    private void prepareUsersTweetCountByIdStatement(){
        try {
            this.usersTweetCountByIdPreparedStatement = this.connection
                .prepareStatement(
                    "SELECT COUNT(*) AS tweets_count FROM tweets AS T " +
                    "INNER JOIN users AS U ON T.user_id=U.id "          +
                    "WHERE U.id=? AND T.id=?"
                );
        } catch (SQLException e) {
            // e.printStackTrace();
        }
    }

    private User getUserFromDBResult(ResultSet res){
        try {
            if(res.next()){
                Integer id = res.getInt("id");
                String username = res.getString("username");
                String password = res.getString("password");
                return new User(id, username, password, this.connection);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    private ResultSet insertUserIntoDB(String username, String password){
        ResultSet res = null;
        try {
            this.userSavePreparedStatement.setString(1, username);
            this.userSavePreparedStatement.setString(2, password);
            res = this.userSavePreparedStatement.executeQuery();
        } catch (SQLException e) {
            // e.printStackTrace();
        }
        return res;
    }

    private void prepareUserSaveStatement(){
        if(this.userSavePreparedStatement == null){
            try {
                this.userSavePreparedStatement = this.connection.prepareStatement
                        (
                            "INSERT INTO users(username, password) VALUES(?, ?) " +
                            "RETURNING id, username, password"
                        );
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private void prepareUsersFollowersStatement(){
        if(this.usersFollowersPreparedStatement == null){
            try {
                this.usersFollowersPreparedStatement = this.connection.prepareStatement
                    (
                        "SELECT * FROM users AS UA "    +
                        "INNER JOIN relationship AS R " +
                        "ON R.follower_id=UA.id "       +
                        "INNER JOIN users AS UB "       +
                        "ON R.followed_id=UB.id "      +
                        "WHERE UB.username=?"
                    );
            } catch (SQLException e) {
                // e.printStackTrace();
            }
        }
    }

    private ResultSet findFollowersFromDB(String username) {
        ResultSet res = null;
        try {
            this.usersFollowersPreparedStatement.setString(1, username);
            res = this.usersFollowersPreparedStatement.executeQuery();
        } catch (SQLException e) {
             e.printStackTrace();
        }
        return res;
    }

}
