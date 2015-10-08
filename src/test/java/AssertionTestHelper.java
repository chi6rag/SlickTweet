import static org.junit.Assert.assertTrue;

public class AssertionTestHelper {

    void assertContains(String parentString, String subString){
        assertTrue(parentString.contains(subString));
    }

}
