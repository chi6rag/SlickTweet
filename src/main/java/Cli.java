import org.apache.commons.configuration.ConfigurationException;

import java.util.Hashtable;
import java.util.Scanner;

public class Cli {
    public static void main(String[] args) throws ConfigurationException {
        Scanner scanner = new Scanner(System.in);
        Authentication authentication = new Authentication();

        System.out.print(authentication.getPreSignInOptions());
        int preSignInChoice = scanner.nextInt();
        switch(preSignInChoice){
            case 1:
                authentication.getAuthDetails();
                break;
            case 2:
                authentication.getAuthDetails();
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
