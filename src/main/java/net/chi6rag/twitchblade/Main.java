package net.chi6rag.twitchblade;

public class Main {

    public static void main(String[] args) {

        String environment = ( System.getenv("ENV") == null ?
                "development" : System.getenv("ENV") );
        DbConnection connection = new DbConnection(environment);

        Cli cli = new Cli(connection);
        cli.start();
    }

}
