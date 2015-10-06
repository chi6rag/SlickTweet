import java.util.Hashtable;
import java.util.Scanner;

public class Cli {
    public static void main(String[] args){
        Scanner scanner = new Scanner(System.in);
        Authentication authentication = new Authentication();
        User currentUser = null;
        Hashtable authDetails = null;

        while(true){
            System.out.print(authentication.getPreSignInOptions());
            int preSignInChoice = scanner.nextInt();
            switch(preSignInChoice){
                case 1:
                    authDetails = authentication.getAuthDetails();
                    currentUser = authentication.signUp(authDetails);
                    if(currentUser != null){
                        System.out.println("Signed Up");
                    }
                    break;
                case 2:
                    authDetails = authentication.getAuthDetails();
                    currentUser = authentication.login(authDetails);
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

    }
}
