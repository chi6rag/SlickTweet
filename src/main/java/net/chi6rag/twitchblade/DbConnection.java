package net.chi6rag.twitchblade;

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

    public void close(){
        try {
            this.connection.close();
        } catch (SQLException e) {
            // e.printStackTrace();
        }
    }

    public Statement createStatement() throws SQLException {
        return this.connection.createStatement();
    }

    public void setAutoCommit(boolean val) throws SQLException {
        this.connection.setAutoCommit(val);
    }

    public void rollback() throws SQLException {
        this.connection.rollback();
    }
}