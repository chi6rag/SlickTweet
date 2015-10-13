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
