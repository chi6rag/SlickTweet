package test_helpers;

import net.chi6rag.twitchblade.domain.*;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Hashtable;

public class UserTestHelper {
    private DbConnection connection;
    PreparedStatement preparedStatement = null;

    public UserTestHelper(DbConnection connection) {
        this.connection = connection;
    }

    public Hashtable getUserDetails(String username, String password){
        Hashtable userDetails = new Hashtable();
        userDetails.put("username", username);
        userDetails.put("password", password);
        return userDetails;
    }

    public void deleteAllUsers(){
        try {
            preparedStatement = this.connection
                    .prepareStatement("DELETE FROM users");
            preparedStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void createTestUser(String username, String password){
        try {
            preparedStatement = connection.prepareStatement(
                    "INSERT INTO users(username, password) VALUES(?, ?)"
            );
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);
            preparedStatement.execute();
        } catch (SQLException e) {
            System.out.println("------ Unable to setup Test Data for User ------");
            e.printStackTrace();
        }
    }

    public User getSavedUserObject(String username, String password,
                              DbConnection connection){
        return (new User(username, password, connection)).save();
    }

    public void setupTestUsers(){
        try {
            preparedStatement = connection.prepareStatement("INSERT INTO users" +
                    "(username, password) VALUES(?, ?) RETURNING username, password");
            preparedStatement.setString(1, "foo_example");
            preparedStatement.setString(2, "123456789");
            preparedStatement.execute();
            preparedStatement.setString(1, "bar_example");
            preparedStatement.setString(2, "123456789");
            preparedStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public int getUserCount(){
        int count = 0;
        try {
            Statement countStatement = this.connection.createStatement();
            ResultSet res = countStatement.executeQuery("SELECT COUNT(*) AS total FROM users");
            if( res.next() ) count = res.getInt("total");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return count;
    }

}