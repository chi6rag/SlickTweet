package test_helpers;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

public class IOTestHelper {

    public ByteArrayOutputStream mockStdOut(){
        ByteArrayOutputStream consoleOutput = new ByteArrayOutputStream();
        System.setOut(new PrintStream(consoleOutput));
        return consoleOutput;
    }

    public void setStdOutToDefault(){
        System.setOut(System.out);
    }

}
