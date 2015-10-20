package net.chi6rag.twitchblade.io;

import net.chi6rag.twitchblade.domain.*;
import java.util.Hashtable;
import java.util.Scanner;

public class Authentication {
    private Users allUsers;
    private DbConnection connection;

    public Authentication(DbConnection connection) {
        this.connection = connection;
        this.allUsers = new Users(connection);
    }

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

    public Hashtable<String,String> getAuthDetails(){
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
            printLoginErrorMessage();
            return null;
        }
        printLoginMessage();
        return user;
    }

    public User signUp(Hashtable<String, String> authDetails){
        String username = authDetails.get("username");
        String password = authDetails.get("password");
        User user = new User(username, password, connection);
        User savedUser = user.save();
        if(savedUser == null){
            printSignUpErrorMessage();
            return null;
        }
        printSignUpMessage();
        return savedUser;
    }

    private void printLoginMessage(){
        System.out.println("Logged In");
    }

    private void printSignUpMessage(){
        System.out.println("Signed Up");
    }

    private void printSignUpErrorMessage(){
        String signUpErrorMessage = "\nInvalid Username or Password\n"            +
                "- Username can only contain letters, numbers and underscores\n"  +
                "  and it can only be 6 to 20 characters long\n"                  +
                "- Username must be unique\n"                                     +
                "- Password must be at least 6 characters long\n";
        System.out.println(signUpErrorMessage);
    }

    private void printLoginErrorMessage(){
        System.out.println("\nInvalid Username or Password Combination\n");
    }

}
