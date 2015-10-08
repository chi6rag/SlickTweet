import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import java.util.Hashtable;

public class UsersTest {
    DbConnection connection = new DbConnection();
    Users allUsers = new Users(connection);

    // Objects of helper classes
    UserTestHelper userTestHelper = new UserTestHelper(connection);

    @Before
    public void beforeEach(){
        userTestHelper.setupTestUsers();
    }

    @After
    public void afterEach(){
        userTestHelper.deleteAllUsers();
    }

    @Test
    public void returnsFoundUserWhenCalledFindWithValidArguments(){
        Hashtable validAuthDetails = userTestHelper
                .getUserDetails("foo_example", "123456789");
        User foundUser = allUsers.find(validAuthDetails);
        assertEquals(foundUser.getId().getClass()
                .getSimpleName(), "Integer");
        assertEquals(foundUser.getClass().getName(), "User");
        assertEquals(foundUser.getUsername(), "foo_example");
        assertEquals(foundUser.getPassword(), "123456789");
    }

    @Test
    public void returnsNullWhenCalledFindWithInexistentUserDetails(){
        Hashtable inexistentAuthDetails = userTestHelper
                .getUserDetails("baz_example", "123456789");
        User foundUser = allUsers.find(inexistentAuthDetails);
        assertEquals(foundUser, null);
    }

    @Test
    public void returnsNullWhenCalledWithWrongUserDetailsHash(){
        Hashtable authDetails = getWrongUserDetailsHash();
        User foundUser = allUsers.find(authDetails);
        assertEquals(foundUser, null);
    }

    private Hashtable getWrongUserDetailsHash(){
        Hashtable authDetails = new Hashtable();
        authDetails.put("height", 10);
        authDetails.put("width", 10);
        return authDetails;
    }

}
