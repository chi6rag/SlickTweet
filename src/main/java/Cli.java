import java.util.Hashtable;
import java.util.Scanner;

public class Cli {
    public static void main(String[] args){
        Scanner scanner = new Scanner(System.in);
        Authentication authentication = new Authentication();
        User currentUser = null;
        Hashtable authDetails = null;

        while(true){

            if(!isLoggedIn(currentUser)){
                System.out.print(authentication.getPreSignInOptions());
                int preSignInChoice = scanner.nextInt();
                switch(preSignInChoice){
                    case 1:
                        authDetails = authentication.getAuthDetails();
                        currentUser = authentication.signUp(authDetails);
                        System.out.println(currentUser);
                        if(currentUser != null){
                            System.out.println("Signed Up");
                        }
                        break;
                    case 2:
                        authDetails = authentication.getAuthDetails();
                        currentUser = authentication.login(authDetails);
                        System.out.println(currentUser);
                        if(currentUser != null){
                            System.out.println("Logged In");
                        }
                        break;
                    case 3:
                        System.exit(0);
                        break;
                    default:
                        System.out.println("Unrecognized Option");
                        System.exit(0);
                        break;
                }
            }

            if(isLoggedIn(currentUser)){
                UserActivity currentUserActivity = new UserActivity(currentUser);
                System.out.print(currentUserActivity.getActivityOptions());
                int userActivityChoice = scanner.nextInt();
                switch(userActivityChoice){
                    case 1:
                        System.out.println(currentUserActivity.askForTweet());
                        String tweetBody = scanner.next();
                        currentUserActivity.tweet(tweetBody);
                        break;
                    case 3:
                        currentUser = null;
                        break;
                    default:
                        System.out.println("Wrong Choice");
                        break;
                }
            }

        }

    }

    private static boolean isLoggedIn(User user){
        return user != null;
    }

}
