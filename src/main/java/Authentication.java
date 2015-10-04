import java.util.Hashtable;
import java.util.Scanner;

public class Authentication {

    public String getPreSignInOptions(){
        String preSignInOptions =
                "  TwitchBlade  \n" +
                "---------------\n" +
                "1. Sign Up\n"      +
                "2. Login  \n"      +
                "3. Exit\n"         +
                "Choose: ";
        return preSignInOptions;
    }

    public Hashtable getSignUpDetails(){
        Scanner consoleInput = new Scanner(System.in);
        System.out.println("Username: ");
        String username = consoleInput.nextLine();
        System.out.println("Password: ");
        String password = consoleInput.nextLine();
        Hashtable signUpDetails = new Hashtable();
        signUpDetails.put("username", username);
        signUpDetails.put("password", password);
        return signUpDetails;
    }

}
