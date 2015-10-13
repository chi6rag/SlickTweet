package net.chi6rag.twitchblade;

import java.util.Hashtable;
import java.util.Scanner;

public class Cli {
    public static void main(String[] args){
        Scanner scanner = new Scanner(System.in);
        Authentication authentication = new Authentication();
        User currentUser = null;
        Hashtable<String, String> authDetails = null;

        while(true){

            if(!isLoggedIn(currentUser)){
                System.out.print(authentication.getPreSignInOptions());
                int preSignInChoice = scanner.nextInt();
                switch(preSignInChoice){
                    case 1:
                        authDetails = authentication.getAuthDetails();
                        currentUser = authentication.signUp(authDetails);
                        break;
                    case 2:
                        authDetails = authentication.getAuthDetails();
                        currentUser = authentication.login(authDetails);
                        break;
                    case 3:
                        System.exit(0);
                        break;
                    default:
                        System.out.println("\nUnrecognized Option");
                }
            }

            if(isLoggedIn(currentUser)){
                UserActivity userActivity = new UserActivity(currentUser);
                userActivity.printActivityOptions();
                int userActivityChoice = scanner.nextInt();
                scanner.nextLine();
                switch(userActivityChoice){
                    case 1:
                        System.out.println(userActivity.askForTweet());
                        String tweetBody = scanner.nextLine();
                        userActivity.tweet(tweetBody);
                        break;
                    case 2:
                        userActivity.printTimeline();
                        break;
                    case 3:
                        userActivity.printUsernameQuestion();
                        String username = scanner.nextLine();
                        userActivity.printTimeline(username);
                        break;
                    case 4:
                        currentUser = null;
                        break;
                    default:
                        System.out.println("Wrong Choice");
                }
            }

        }

    }

    private static boolean isLoggedIn(User user){
        return user != null;
    }

}
