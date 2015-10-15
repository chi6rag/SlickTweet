package test_helpers;

import static org.junit.Assert.assertTrue;

public class AssertionTestHelper {

    public void assertContains(String parentString, String subString){
        System.out.println(parentString);
        System.out.println(subString);
        String errorMessage = parentString + " does not contain " + subString;
        assertTrue(errorMessage, parentString.contains(subString));
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

    public <T> boolean containsElement(T[] array, T element){
        for(int i=0; i<array.length; i++) {
            if(array[i].equals(element) || array[i] == element) return true;
        }
        return false;
    }

}
