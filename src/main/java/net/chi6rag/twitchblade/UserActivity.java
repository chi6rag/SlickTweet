package net.chi6rag.twitchblade;

import java.sql.SQLException;
import java.util.ArrayList;

public class UserActivity {
    private DbConnection connection = new DbConnection();
    private User currentUser;

    public UserActivity(User currentUser){
        this.currentUser = currentUser;
    }

    public String getActivityOptions(){
        String activityOptions =
            "\nWelcome " + this.currentUser
                    .getUsername()  +
            "\n\n"                  +
            "1. Tweet\n"            +
            "2. Your Timeline\n"    +
            "3. Logout\n"           +
            "Choose: ";
        return activityOptions;
    }

    public String askForTweet(){
        String question = "What's in your mind?";
        return question;
    }

    public Tweet tweet(String tweetBody){
        Tweet tweet = (new Tweet(tweetBody, this.currentUser.getId(),
                this.connection)).save();
        if(tweet == null) System.out.println("Tweet cannot be saved");
        return tweet;
    }

    public void printTimeline(){
        Timeline timeline = new Timeline(this.currentUser, this.connection);
        ArrayList<Tweet> usersTweets = timeline.getTweets();
        for(int i=0; i<usersTweets.size(); i++){
            System.out.println( (usersTweets.get(i)).getUserId() );
            System.out.println( (usersTweets.get(i)).getBody() );
        }
    }

    public void logout(){
        try {
            this.connection.close();
            this.connection = null;
        } catch (SQLException e) {
            System.out.println("\nSome errors during logout\n" +
                    "Please contact the customer support");
        }
    }

}