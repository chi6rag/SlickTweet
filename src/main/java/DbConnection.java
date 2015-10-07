import java.sql.*;

public class DbConnection {

    private Connection connection;

    public DbConnection() {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            System.out.println("PostgreSQL JDBC Driver not Found!");
            e.printStackTrace();
        }
        String environment = ( System.getenv("ENV") == null ?
                "development" : System.getenv("ENV"));
        try {
            connection = DriverManager.getConnection(
                    "jdbc:postgresql://localhost:5432/twitchblade_"
                            + environment, "chi6rag", "");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public PreparedStatement prepareStatement(String query) throws SQLException{
        return this.connection.prepareStatement(query);
    }

    public void close() throws SQLException {
        this.connection.close();
    }

    public Statement createStatement() throws SQLException {
        return this.connection.createStatement();
    }

}