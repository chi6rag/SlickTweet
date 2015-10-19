package net.chi6rag.twitchblade;

import java.util.Hashtable;
import java.util.Scanner;

public class Cli {
    private Scanner scanner = new Scanner(System.in);
    private DbConnection connection;

    Cli (DbConnection connection){
        this.connection = connection;
    }

    public void start(){
        Authentication authentication = new Authentication(connection);
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
                UserActivity userActivity = new UserActivity(currentUser, connection);
                userActivity.printActivityOptions();
                int userActivityChoice = scanner.nextInt();
                String username;
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
                        username = scanner.nextLine();
                        userActivity.printProfilePageOf(username);
                        break;
                    case 4:
                        userActivity.printFollowers();
                        break;
                    case 5:
                        userActivity.printUsernameQuestion();
                        username = scanner.nextLine();
                        userActivity.followUser(username);
                        break;
                    case 6:
                        System.out.print("Enter tweet id: ");
                        int tweetId = scanner.nextInt();
                        userActivity.retweet(tweetId);
                        break;
                    case 7:
                        userActivity.logout();
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
