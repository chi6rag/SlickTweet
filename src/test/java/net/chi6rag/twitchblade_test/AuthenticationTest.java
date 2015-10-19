package net.chi6rag.twitchblade_test;

import net.chi6rag.twitchblade.*;
import static org.junit.Assert.assertEquals;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import java.io.ByteArrayInputStream;
import java.util.Hashtable;

public class AuthenticationTest {
    private DbConnection connection;
    private Authentication auth;

    @Before
    public void beforeEach(){
        this.connection = new DbConnection("testing");
        this.auth = new Authentication(connection);
    }

    @After
    public void afterEach(){
        this.connection.close();
    }

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
    public void testGetsAuthDetails(){
//      ByteArrayOutputStream consoleOutput = new ByteArrayOutputStream();
//      System.setOut(new PrintStream(consoleOutput));
        ByteArrayInputStream consoleInput = new
                ByteArrayInputStream("foo_example\n123456789".getBytes());
        System.setIn(consoleInput);

        Hashtable authDetails = new Hashtable();
        authDetails.put("username", "foo_example");
        authDetails.put("password", "123456789");

        assertEquals(auth.getAuthDetails(), authDetails);
        System.setIn(System.in);
    }

}
