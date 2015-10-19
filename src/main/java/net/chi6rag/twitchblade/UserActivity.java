package net.chi6rag.twitchblade;

import java.util.ArrayList;
import java.util.Date;

public class UserActivity {
    private DbConnection connection;
    private Users allUsers;
    private User currentUser;

    public UserActivity(User currentUser, DbConnection connection)
    {
        this.currentUser = currentUser;
        this.connection = connection;
    }

    public void printActivityOptions(){
        String activityOptions =
            "\nWelcome " + currentUser.getUsername() +
            "\n\n"                                   +
            "1. Tweet\n"                             +
            "2. Your Timeline\n"                     +
            "3. See someone's profile page\n"        +
            "4. Your Followers\n"                    +
            "5. Follow User\n"                       +
            "6. Retweet Tweet\n"                     +
            "7. Logout\n"                            +
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
        }
        else {
            printNoTweetsPresentMessage();
        }
    }

    public void printProfilePageOf(String username){
        this.allUsers = new Users(connection);
        User queriedUser = allUsers.findByUsername(username);
        if(queriedUser == null){
            System.out.println("Username does not exist");
            return;
        }
        Profile profile = new Profile(queriedUser, connection);
        ArrayList<Tweet> usersTweets = profile.getTweets();
        if(areTweetsPresent(usersTweets)){
            printTweets(usersTweets);
        }
        else {
            printNoTweetsPresentMessage();
        }
    }

    public void printFollowers() {
        ArrayList<User> followers = currentUser.followers();
        if(areFollowersPresent(followers)) {
            for(int i=0; i<followers.size(); i++){
                User follower = followers.get(i);
                System.out.println("User ID: " + follower.getId());
                System.out.println("Username: " + follower.getUsername());
                System.out.println();
            }
        }
        else {
            System.out.println("No Followers");
        }
    }

    public void logout(){
        this.connection.close();
        this.connection  = null;
        this.currentUser = null;
    }

    public void followUser(String username) {
        boolean hasFollowed = currentUser.follow(username);
        if(hasFollowed) {
            System.out.println("Followed " + username);
        }
        else {
            System.out.println("Cannot follow " + username);
        }
    }

    public void retweet(int tweetId){
        boolean isRetweetSuccessful = currentUser.retweet(tweetId);
        if(isRetweetSuccessful){
            System.out.println("\nTweet Retweeted!\n");
        }
        else {
            System.out.println("\nCannot retweet this tweet!\n");
        }
    }

    public void printUsernameQuestion(){
        System.out.println("Enter username: ");
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

    private boolean areFollowersPresent(ArrayList<User> followers) {
        return !(followers == null || followers.size() == 0);
    }

    private void printTweets(ArrayList<Tweet> tweets){
        for(int i=0; i<tweets.size(); i++){
            Tweet tweet = tweets.get(i);
            System.out.println("Tweet ID: " + tweet.getId());
            System.out.println(tweet.getBody());
            System.out.println("Posted: " + timeAgoInWords(tweet.getCreatedAt()));
            System.out.println();
        }
    }

    private String timeAgoInWords(Date date){
        long milliseconds = ((new Date()).getTime() - date.getTime());
        long seconds = milliseconds/1000;
        long minutes = seconds/60;
        long hours = (long) (minutes/60.0);
        long days = (long) (hours/24.0);
        if(days >= 1){ return(pluralize(days, "day") + " ago"); }
        if(hours > 1) { return(pluralize(hours, "hour") + " ago"); }
        if(minutes > 1) { return(pluralize(minutes, "minute") + " ago"); }
        if(seconds > 1) { return(pluralize(seconds, "seconds") + " ago"); }
        return "1 seconds ago";
    }

    private String pluralize(long number, String singularWord){
        if(number <= 1){
            return number + " " + singularWord;
        }
        else {
            return number + " " + singularWord + "s";
        }
    }

}