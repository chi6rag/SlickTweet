package test_helpers;

import static org.junit.Assert.assertTrue;

public class AssertionTestHelper {

    public void assertContains(String parentString, String subString){
        assertTrue(parentString.contains(subString));
    }

    // if none of the arguments is contained in the target string
    //     - use assertions
    public boolean containsAnyOf(String target, String... arguments){
        Boolean isArgumentPresent = false;
        for(int i=0; i<arguments.length; i++){
            isArgumentPresent = target.contains(arguments[i]);
            if(isArgumentPresent) return true;
        }
        return false;
    }

}
