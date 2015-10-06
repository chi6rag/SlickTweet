import java.util.Hashtable;
import java.util.Scanner;

public class Authentication {
    Users allUsers = new Users();

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

    public Hashtable getAuthDetails(){
        Scanner consoleInput = new Scanner(System.in);
        System.out.println("Username: ");
        String username = consoleInput.nextLine();
        System.out.println("Password: ");
        String password = consoleInput.nextLine();
        Hashtable authDetails = new Hashtable();
        authDetails.put("username", username);
        authDetails.put("password", password);
        return authDetails;
    }

    public User login(Hashtable authDetails){
        return allUsers.find(authDetails);
    }

//    public User login(authDetails)
//    check if the user if present in db
//    yes, find user and return
//    no, return null

//    public User signUp(authDetails)
//    create user with required auth details
//    if success, return the user
//    else return null

}
