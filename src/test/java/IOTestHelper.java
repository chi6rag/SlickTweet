import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

public class IOTestHelper {

    ByteArrayOutputStream mockStdOut(){
        ByteArrayOutputStream consoleOutput = new ByteArrayOutputStream();
        System.setOut(new PrintStream(consoleOutput));
        return consoleOutput;
    }

    void setStdOutToDefault(){
        System.setOut(System.out);
    }

}
