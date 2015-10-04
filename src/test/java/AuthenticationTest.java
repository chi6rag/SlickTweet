import static org.junit.Assert.assertEquals;

import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.util.Hashtable;

public class AuthenticationTest {
    Authentication auth = new Authentication();

    @Test
    public void testGetsPreSignInOptions() {
        String preSignInOptions =
                "  TwitchBlade  \n" +
                "---------------\n" +
                "1. Sign Up\n"      +
                "2. Login  \n"      +
                "3. Exit\n"         +
                "Choose: ";
        assertEquals(auth.getPreSignInOptions(), preSignInOptions);
    }

//  to-do
//  assert stdout to contain what has been asked
//    - username
//    - password
    @Test
    public void testGetsSignUpDetails(){
//      ByteArrayOutputStream consoleOutput = new ByteArrayOutputStream();
//      System.setOut(new PrintStream(consoleOutput));
        ByteArrayInputStream consoleInput = new
                ByteArrayInputStream("foo_example\n123456789".getBytes());
        System.setIn(consoleInput);

        Hashtable signUpDetails = new Hashtable();
        signUpDetails.put("username", "foo_example");
        signUpDetails.put("password", "123456789");

        assertEquals(auth.getSignUpDetails(), signUpDetails);
        System.setIn(System.in);
    }

}
