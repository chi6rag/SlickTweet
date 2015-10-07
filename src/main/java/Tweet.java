import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Tweet {
    private Integer id;
    private String body;
    private Integer userId;
    private DbConnection connection;
    private PreparedStatement tweetSavePreparedStatement;

    Tweet(String body, Integer userId, DbConnection connection){
        this.id = null;
        this.body = body;
        this.userId = userId;
        this.connection = connection;
    }

    public Tweet save(){
        prepareTweetSaveStatement();
        ResultSet res = null;
        res = insertUserIntoDB(this.body, this.userId);
        if(res != null) return getUserFromDBResult(res);
        return null;
    }

    public Integer getId(){
        return this.id;
    }

    private void prepareTweetSaveStatement(){
        try {
            this.tweetSavePreparedStatement = this.connection.prepareStatement("INSERT" +
                    " INTO tweets(body, user_id) VALUES(?, ?) RETURNING id, body," +
                    " user_id");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private ResultSet insertUserIntoDB(String body, Integer userId){
        if(body == null   || userId == null) return null;
        if(body.isEmpty()) return null;
        ResultSet res = null;
        try {
            this.tweetSavePreparedStatement.setString(1, body);
            this.tweetSavePreparedStatement.setInt(2, userId);
            res = this.tweetSavePreparedStatement.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return res;
    }

    private Tweet getUserFromDBResult(ResultSet res){
        try {
            if(res.next()){
                String body = res.getString("body");
                Integer userId = res.getInt("user_id");
                return new Tweet(body, userId, this.connection);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

}
