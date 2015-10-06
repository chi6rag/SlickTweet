import java.sql.*;

public class User {
    String username;
    String password;
    PreparedStatement userSavePreparedStatement = null;
    Connection connection = null;

    User(String username, String password){
        initializeDBConnection();
        this.username = username;
        this.password = password;
    }

    public User save() {
        prepareUserSaveStatement();
        ResultSet res = null;
        res = insertUserIntoDB(this.username, this.password);
        if(res != null) return getUserFromDBResult(res);
        return null;
    }

    public String getUsername(){
        return this.username;
    }

    public String getPassword(){
        return this.password;
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

    private ResultSet insertUserIntoDB(String username, String password){
        ResultSet res = null;
        try {
            this.userSavePreparedStatement.setString(1, this.username);
            this.userSavePreparedStatement.setString(2, this.password);
            res = this.userSavePreparedStatement.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return res;
    }

    private void prepareUserSaveStatement(){
        if(this.userSavePreparedStatement == null){
            try {
                this.userSavePreparedStatement = this.connection.prepareStatement
                        ("INSERT INTO users(username, password) VALUES(?, ?) " +
                                "RETURNING username, password");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
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
