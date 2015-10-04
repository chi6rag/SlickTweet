import org.apache.commons.configuration.ConfigurationException;
import java.util.Scanner;

public class Cli {
    public static void main(String[] args) throws ConfigurationException {
        Scanner scanner = new Scanner(System.in);
        Authentication authentication = new Authentication();

        System.out.print(authentication.getPreSignInOptions());
        int preSignInChoice = scanner.nextInt();
        switch(preSignInChoice){
            case 1:
                System.out.println("Implement Sign Up");
                break;
            case 2:
                System.out.println("Implement Login");
                break;
            case 3:
                System.out.println("Implement Exit");
                break;
            default:
                System.out.println("Unrecognized Option");
                break;
        }

    }
}
