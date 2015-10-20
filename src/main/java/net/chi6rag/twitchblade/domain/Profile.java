package net.chi6rag.twitchblade.domain;

import java.util.ArrayList;

public class Profile {
    User user;
    Tweets allTweets;

    public Profile(User user, DbConnection connection){
        this.user = user;
        this.allTweets = new Tweets(connection);
    }

    public ArrayList<Tweet> getTweets(){
        if( !this.user.isValid() ) { return null; }
        return allTweets.forProfileOf(this.user.getId());
    }

}
