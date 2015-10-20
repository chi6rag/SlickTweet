package test_helpers;

import net.chi6rag.twitchblade.domain.*;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class RetweetTestHelper {
    DbConnection connection;

    public RetweetTestHelper(DbConnection connection){
        this.connection = connection;
    }

    public void deleteAllRetweets(){
        try {
            PreparedStatement deleteRetweetsStatement = this.connection
                    .prepareStatement("DELETE FROM retweets");
            deleteRetweetsStatement.execute();
        } catch (SQLException e) {
            // e.printStackTrace();
        }
    }

}
