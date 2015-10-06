import java.sql.*;
import java.util.Hashtable;

public class Users {
    Connection connection = null;
    PreparedStatement userFindPreparedStatement = null;

    Users(){ initializeDBConnection(); }

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
