import java.sql.*;
import java.util.Hashtable;

public class Users {
    DbConnection connection;
    PreparedStatement userFindPreparedStatement = null;

    Users(DbConnection connection){
        this.connection = connection;
    }

    public User find(Hashtable authDetails){
        prepareUserFindStatement();
        String username = (String) authDetails.get("username");
        String password = (String) authDetails.get("password");
        ResultSet res = findUserFromDB(username, password);
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
}
