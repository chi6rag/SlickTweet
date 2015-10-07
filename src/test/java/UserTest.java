import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import java.sql.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertSame;

public class UserTest {
    PreparedStatement preparedStatement = null;
    DbConnection connection = new DbConnection();

    @Before
    public void beforeEach(){
    }

    @After
    public void afterEach(){
        deleteAllUsers();
    }

    @Test
    public void testSaveWithValidUserObjectIncreasesUserCountBy1(){
        int beforeCount = getUserCount();
        User user = new User("foo_example", "123456789", this.connection);
        user.save();
        int afterCount = getUserCount();
        assertNotEquals(beforeCount, afterCount);
    }

    @Test
    public void testSaveWithValidUserObjectReturnsTheSavedUser(){
        User user = new User("foo_example", "123456789", this.connection);
        User savedUser = user.save();
        assertEquals(savedUser.getUsername(), "foo_example");
        assertEquals(savedUser.getPassword(), "123456789");
        assertEquals(savedUser.getClass().getName(), "User");
    }

    @Test
    public void testSaveWithInvalidUserObjectKeepsUserCountSame(){
        int beforeCount = getUserCount();
        User user = new User("ab", "123456789", this.connection);
        user.save();
        int afterCount = getUserCount();
        assertEquals(beforeCount, afterCount);
    }

    @Test
    public void testSaveWithInvalidUserObjectReturnsNull(){
        User user = new User("ab", "123456789", this.connection);
        assertEquals(user.save(), null);
    }

    private int getUserCount(){
        int count = 0;
        try {
            Statement countStatement = this.connection.createStatement();
            ResultSet res = countStatement.executeQuery("SELECT COUNT(*) AS total FROM users");
            if( res.next() ) count = res.getInt("total");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return count;
    }

    private void deleteAllUsers(){
        try {
            preparedStatement = this.connection.prepareStatement("DELETE FROM users");
            preparedStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
