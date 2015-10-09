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
        printTweetSavedMessage(tweet);
        return tweet;
    }

    public void printTimeline(){
        Timeline timeline = new Timeline(this.currentUser, this.connection);
        ArrayList<Tweet> usersTweets = timeline.getTweets();
        if(areTweetsPresent(usersTweets)){
            printTweets(usersTweets);
        } else {
            printNoTweetsPresentMessage();
        }
    }

    public void logout(){
        try {
            this.connection.close();
            this.connection  = null;
            this.currentUser = null;
        } catch (SQLException e) {
            System.out.println("\nSome errors during logout\n" +
                    "Please contact the customer support");
        }
    }

    private void printTweetSavedMessage(Tweet tweet){
        if(tweet == null){
            System.out.println(getTweetErrorMessage());
        } else
        {
            System.out.println("\nTweet posted\n");
        }
    }

    private String getTweetErrorMessage(){
        String errorMessage = "\nTweet cannot be posted. Note: \n"  +
                "- tweet's length should be upto 140 characters"    +
                "- tweet should not be blank";
        return errorMessage;
    }

    private void printNoTweetsPresentMessage(){
        System.out.println("\nNo Tweets\n");
    }

    private boolean areTweetsPresent(ArrayList<Tweet> tweets){
        return !(tweets == null || tweets.size() == 0);
    }

    private void printTweets(ArrayList<Tweet> tweets){
        for(int i=0; i<tweets.size(); i++){
            System.out.println( (tweets.get(i)).getUserId() );
            System.out.println( (tweets.get(i)).getBody() );
        }
    }

}