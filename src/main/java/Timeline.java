import java.util.ArrayList;
import java.util.Hashtable;

public class Timeline {

    private DbConnection connection;
    private User user;
    private Tweets tweets;

    Timeline(User user, DbConnection connection){
        this.user = user;
        this.connection = connection;
        this.tweets = new Tweets(connection);
    }

    public ArrayList<Tweet> getTweets(){
        if(this.user.getId() == null) { return null; }
        Hashtable queryHash = new Hashtable();
        queryHash.put("userId", this.user.getId());
        return tweets.where(queryHash);
    }

}
