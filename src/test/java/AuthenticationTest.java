import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class AuthenticationTest {
    @Test
    public void getPreSignInOptions(){
        Authentication auth = new Authentication();
        String preSignInOptions =
                "  TwitchBlade  \n" +
                "---------------\n" +
                "1. Sign Up\n"      +
                "2. Login  \n"      +
                "3. Exit\n"         +
                "Choose: ";
        assertEquals(auth.getPreSignInOptions(), preSignInOptions);
    }
}
