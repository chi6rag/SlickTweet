import java.util.Scanner;

public class Cli {
    public static void main(String[] args){
        Scanner scanner = new Scanner(System.in);
        Authentication authentication = new Authentication();

        System.out.print(authentication.getPreSignInOptions());
        int preSignInChoice = scanner.nextInt();

    }
}
