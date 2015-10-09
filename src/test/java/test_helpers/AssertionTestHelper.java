package test_helpers;

import static org.junit.Assert.assertTrue;

public class AssertionTestHelper {

    public void assertContains(String parentString, String subString){
        assertTrue(parentString.contains(subString));
    }

}
