import java.sql.*;
import java.util.Hashtable;

public class Users {
    Connection connection = null;
    PreparedStatement userFindPreparedStatement = null;

    Users(){ initializeDBConnection(); }

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
                String username = res.getString("username");
                String password = res.getString("password");
                return new User(username, password);
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

    private void initializeDBConnection(){
        if(this.connection == null){
            try {
                Class.forName("org.postgresql.Driver");
            } catch (ClassNotFoundException e) {
                System.out.println("PostgreSQL JDBC Driver not Found!");
                e.printStackTrace();
                return;
            }
            String environment = ( System.getenv("ENV") == null ?
                    "development" : System.getenv("ENV"));
            try {
                this.connection = DriverManager.getConnection(
                        "jdbc:postgresql://localhost:5432/twitchblade_"
                                + environment, "chi6rag", "");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

}
