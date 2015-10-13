package net.chi6rag.twitchblade;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;

public class UserActivity {
    private DbConnection connection = new DbConnection();
    private Users allUsers;
    private User currentUser;

    public UserActivity(User currentUser){
        this.currentUser = currentUser;
    }

    public void printActivityOptions(){
        String activityOptions =
            "\nWelcome " + currentUser.getUsername() +
            "\n\n"                                   +
            "1. Tweet\n"                             +
            "2. Your Timeline\n"                     +
            "3. See someone's timeline\n"            +
            "4. Your Followers\n"                    +
            "5. Logout\n"                            +
            "Choose: ";
        System.out.println(activityOptions);
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

    public void printTimeline(String username){
        this.allUsers = new Users(connection);
        User queriedUser = allUsers.findByUsername(username);
        if(queriedUser == null){
            System.out.println("Username does not exist");
            return;
        }
        Timeline timeline = new Timeline(queriedUser, this.connection);
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

    public String getUsernameQuestion(){
        return "Enter username: ";
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
                "- tweet's length should be upto 140 characters\n"    +
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
        System.out.println("\nTimeline of " + currentUser.getUsername());
        System.out.println(getUnderlineFor(
                "Timeline of" + currentUser.getUsername())
        );
        for(int i=0; i<tweets.size(); i++){
            Tweet tweet = tweets.get(i);
            System.out.println("Tweet ID: " + tweet.getId());
            System.out.println(tweet.getBody());
            System.out.println("Posted: " + timeAgoInWords(tweet.getCreatedAt()));
            System.out.println();
        }
    }

    private String getUnderlineFor(String string){
        String s = "";
        for(int i=0; i<string.length(); i++){ s.concat("-"); }
        return s;
    }

    private String timeAgoInWords(Date date){
        long milliseconds = ((new Date()).getTime() - date.getTime());
        long seconds = milliseconds/1000;
        long minutes = seconds/60;
        long hours = minutes/60;
        long days = hours/24;
        if(days > 1){ return(days + " days ago"); }
        if(hours > 1) { return(hours + " hours ago"); }
        if(minutes > 1) { return(minutes+ " minutes ago"); }
        if(seconds > 1) { return(seconds + " seconds ago"); }
        return "1 seconds ago";
    }
}