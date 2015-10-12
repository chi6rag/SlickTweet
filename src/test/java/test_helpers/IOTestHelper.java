package test_helpers;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

public class IOTestHelper {

    public ByteArrayOutputStream mockStdOut(){
        ByteArrayOutputStream consoleOutput = new ByteArrayOutputStream();
        System.setOut(new PrintStream(consoleOutput));
        return consoleOutput;
    }

    public ByteArrayInputStream mockStdIn(String textToInput){
        ByteArrayInputStream consoleInput = new
                ByteArrayInputStream(textToInput.getBytes());
        System.setIn(consoleInput);
        return consoleInput;
    }

    public void setStdOutToDefault(){
        System.setOut(System.out);
    }
    public void setStdInToDefault(){ System.setIn(System.in); }

}
