import org.junit.After;
import org.junit.Test;

import static org.junit.Assert.*;

public class UserTest {
    DbConnection connection = new DbConnection();

    // Objects of helper classes
    UserTestHelper userTestHelper = new UserTestHelper(connection);

    @After
    public void afterEach(){
        userTestHelper.deleteAllUsers();
    }

    @Test
    public void testNewWithValidDetailsSetsIdNull(){
        User user = new User("foo_example", "123456789", this.connection);
        assertEquals(user.getId(), null);
    }

    @Test
    public void testSaveWithValidUserObjectIncreasesUserCountBy1(){
        int beforeCount = userTestHelper.getUserCount();
        User user = new User("foo_example", "123456789", this.connection);
        user.save();
        int afterCount = userTestHelper.getUserCount();
        assertNotEquals(beforeCount, afterCount);
    }

    @Test
    public void testSaveWithValidUserObjectReturnsTheSavedUser(){
        User user = new User("foo_example", "123456789", this.connection);
        User savedUser = user.save();
        assertEquals(savedUser.getId().getClass()
                .getSimpleName(), "Integer");
        assertEquals(savedUser.getUsername(), "foo_example");
        assertEquals(savedUser.getPassword(), "123456789");
        assertEquals(savedUser.getClass().getName(), "User");
    }

    @Test
    public void testSaveWithInvalidUserObjectKeepsUserCountSame(){
        int beforeCount = userTestHelper.getUserCount();
        User user = new User("ab", "123456789", this.connection);
        user.save();
        int afterCount = userTestHelper.getUserCount();
        assertEquals(beforeCount, afterCount);
    }

    @Test
    public void testSaveWithInvalidUserObjectReturnsNull(){
        User user = new User("ab", "123456789", this.connection);
        assertEquals(user.save(), null);
    }

}
