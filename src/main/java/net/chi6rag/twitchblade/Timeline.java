package net.chi6rag.twitchblade;

import java.util.ArrayList;
import java.util.Hashtable;

public class Timeline {

    private User user;
    private Tweets tweets;

    public Timeline(User user, DbConnection connection){
        this.user = user;
        this.tweets = new Tweets(connection);
    }

    public ArrayList<Tweet> getTweets(){
        if(this.user.getId() == null) { return null; }
        return tweets.forTimelineOf(this.user.getId());
    }

}
