package net.chi6rag.twitchblade.domain;

import java.sql.*;
import java.util.ArrayList;
import java.util.Hashtable;

public class Users {
    DbConnection connection;
    PreparedStatement userFindPreparedStatement = null;
    PreparedStatement userFindByUsernamePreparedStatement = null;

    public Users(DbConnection connection){
        this.connection = connection;
    }

    public static ArrayList<User> buildFromDbResult(ResultSet res, DbConnection connection){
        ArrayList<User> users = new ArrayList<User>();
        try {
            while(res.next()){
                Integer id = res.getInt("id");
                String username = res.getString("username");
                String password = res.getString("password");
                users.add(new User(id, username, password, connection));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }

    public User find(Hashtable authDetails){
        prepareUserFindStatement();
        String username = (String) authDetails.get("username");
        String password = (String) authDetails.get("password");
        ResultSet res = findUserFromDB(username, password);
        if(res != null) return getUserFromDBResult(res);
        return null;
    }

    public User findByUsername(String username){
        prepareUserFindByUsernameStatement();
        ResultSet res = findUserByUsernameFromDB(username);
        if(res != null) return getUserFromDBResult(res);
        return null;
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

    private void prepareUserFindStatement(){
        if(this.userFindPreparedStatement == null){
            try {
                this.userFindPreparedStatement = this.connection
                        .prepareStatement("SELECT * FROM users WHERE users.username = ?" +
                                " AND users.password = ?");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private void prepareUserFindByUsernameStatement(){
        if(this.userFindByUsernamePreparedStatement == null){
            try {
                this.userFindByUsernamePreparedStatement = this.connection
                        .prepareStatement("SELECT * FROM users WHERE users.username = ?");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private ResultSet findUserFromDB(String username, String password){
        if(username == null || password == null) return null;
        if(username.isEmpty() || password.isEmpty()) return null;
        ResultSet res = null;
        try {
            this.userFindPreparedStatement.setString(1, username);
            this.userFindPreparedStatement.setString(2, password);
            res = this.userFindPreparedStatement.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return res;
    }

    private ResultSet findUserByUsernameFromDB(String username){
        if(username == null || username.isEmpty()) return null;
        ResultSet res = null;
        try {
            this.userFindByUsernamePreparedStatement.setString(1, username);
            res = this.userFindByUsernamePreparedStatement.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return res;
    }
}
