import javafx.animation.Timeline;

public class UserActivity {
    private DbConnection connection = new DbConnection();
    private User currentUser;

    UserActivity(User currentUser){
        this.currentUser = currentUser;
    }

    public String getActivityOptions(){
        String activityOptions =
            "\nWelcome foo_example" +
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
        System.out.println("testing one");
        System.out.println("testing two");
//        Timeline usersTimeline = new Timeline(this.currentUser.getId(),
//                this.connection);
//        String printableTimeline = usersTimeline.toString();
//        System.out.println(printableTimeline);
    }

}