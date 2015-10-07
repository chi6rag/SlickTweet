##TwitchBlade

TwitchBlade is a command line app which lets you share messages upto 140 characters.

###Setting Up: Development
1. Create a PostgreSQL database twitchblade_development with user chi6rag
2. Create a users table as follows:
    
    ```
    CREATE TABLE users(
      id SERIAL PRIMARY KEY,
      username varchar(20) NOT NULL,
      password varchar(30) NOT NULL,
      CONSTRAINT username_unique UNIQUE(username),
      CONSTRAINT username_proper CHECK ( username ~* '^[a-zA-Z0-9_]{6,20}$' )
    );
    ```
3. Create a tweets table as follows:

    ```
    CREATE TABLE tweets(
      id SERIAL PRIMARY KEY,
      body varchar(140) NOT NULL,
      user_id integer REFERENCES users(id),
      created_at timestamptz DEFAULT statement_timestamp(),
      CONSTRAINT body_proper CHECK ( body ~* '[\w|\s]+' )
    );
    ```

####Running in IDE
      - Fire up the IntelliJ Idea IDE and import Maven Dependencies
      - Right click on main > java > Cli and click Run.

####Running on the Command Line
      - Install the maven dependencies by running the following command:
        ```
        mvn install
        ```
      - Run the application by running the following command:
        ```
        mvn exec:java -Dexec.mainClass="Cli"
        ```

###Setting Up: Testing
1. Create a database twitchblade_testing with user chi6rag
2. Create a users table as follows:
    
    ```
    CREATE TABLE users(
      id SERIAL PRIMARY KEY,
      username varchar(20) NOT NULL,
      password varchar(30) NOT NULL,
      CONSTRAINT username_unique UNIQUE(username),
      CONSTRAINT username_proper CHECK ( username ~* '^[a-zA-Z0-9_]{6,20}$' )
    );
    ```
3. Create a tweets table as follows

    ```
    CREATE TABLE tweets(
      id SERIAL PRIMARY KEY,
      body varchar(140) NOT NULL,
      user_id integer REFERENCES users(id),
      created_at timestamptz DEFAULT statement_timestamp(),
      CONSTRAINT body_proper CHECK ( body ~* '[\w|\s]+' )
    );
    ```
4. Fire up the IntelliJ Idea IDE and import Maven Dependencies
5. Run the tests by clicking Fn + Shift + F10
   Alternatively, run the tests by executing the following command in the
   project root directory:
   
      ```
      ENV="testing" mvn test
      ```