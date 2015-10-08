import java.util.Hashtable;
import java.util.Scanner;

public class Authentication {
    DbConnection connection = new DbConnection();
    Users allUsers = new Users(connection);

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
        User user = allUsers.find(authDetails);
        if(user == null){
            printAuthErrorMessage();
        }
        return user;
    }

    public User signUp(Hashtable authDetails){
        String username = (String) authDetails.get("username");
        String password = (String) authDetails.get("password");
        User user = new User(username, password, connection);
        User savedUser = user.save();
        if(savedUser == null){
            printAuthErrorMessage();
            return null;
        }
        return savedUser;
    }

    private void printAuthErrorMessage(){
        String authErrorMessage =
            "\nUsername or Password Not Proper\n" +
            "Username can only contain letters, numbers and underscores\n" +
            "and it can only be 6 to 20 characters\n";
        System.out.println(authErrorMessage);
    }

}
