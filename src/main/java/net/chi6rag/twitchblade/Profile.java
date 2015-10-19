package net.chi6rag.twitchblade;

import java.util.ArrayList;
import java.util.Hashtable;

public class Profile {
    User user;
    Tweets allTweets;

    public Profile(User user, DbConnection connection){
        this.user = user;
        this.allTweets = new Tweets(connection);
    }

    public ArrayList<Tweet> getTweets(){
        if( !this.user.isValid() ) { return null; }
        Hashtable query = new Hashtable();
        query.put("userId", this.user.getId());
        return allTweets.where(query);
    }

}
