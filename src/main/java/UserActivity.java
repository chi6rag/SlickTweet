public class UserActivity {
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

}