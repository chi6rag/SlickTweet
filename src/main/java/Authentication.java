import java.util.Hashtable;

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

}
