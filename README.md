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
      CONSTRAINT password_length_proper CHECK ( password ~* '^(.+){6,}$' ),
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
4. Create a relationship table as follows:

    ```
    CREATE TABLE relationship(
      id SERIAL PRIMARY KEY,
      follower_id integer NOT NULL REFERENCES users(id),
      followed_id integer NOT NULL REFERENCES users(id),
      created_at timestamptz DEFAULT statement_timestamp(),
      CONSTRAINT relationship_unique UNIQUE(follower_id, followed_id)
    );
    ```
5. Create a retweets table as follows:

    ```
    CREATE TABLE retweets(
      id SERIAL PRIMARY KEY,
      tweet_id integer NOT NULL REFERENCES tweets(id),
      retweeter_id integer NOT NULL REFERENCES users(id),
      created_at timestamptz DEFAULT statement_timestamp(),
      CONSTRAINT retweet_unique UNIQUE(tweet_id, retweeter_id)
    );
    ```

####Running in IDE
      - Fire up the IntelliJ Idea IDE and import Maven Dependencies
      - Right click on main > java > net.chi6rag.twitchblade > Cli and click Run.

####Running on the Command Line
      - Install the maven dependencies by running the following command:
        ```
        ENV="development" mvn install
        ```
        Note: Running the command will clean the database

      - Run the application by running the following command:
        ```
        mvn exec:java -Dexec.mainClass="net.chi6rag.twitchblade.Main"
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
      CONSTRAINT password_length_proper CHECK ( password ~* '^(.+){6,}$' ),
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
4. Create a relationship table as follows:

    ```
    CREATE TABLE relationship(
      id SERIAL PRIMARY KEY,
      follower_id integer NOT NULL REFERENCES users(id),
      followed_id integer NOT NULL REFERENCES users(id),
      created_at timestamptz DEFAULT statement_timestamp(),
      CONSTRAINT relationship_unique UNIQUE(follower_id, followed_id)
    );
    ```
5. Create a retweets table as follows:

    ```
    CREATE TABLE retweets(
      id SERIAL PRIMARY KEY,
      tweet_id integer NOT NULL REFERENCES tweets(id),
      retweeter_id integer NOT NULL REFERENCES users(id),
      created_at timestamptz DEFAULT statement_timestamp(),
      CONSTRAINT retweet_unique UNIQUE(tweet_id, retweeter_id)
    );
    ```
6. Fire up the IntelliJ Idea IDE and import Maven Dependencies
7. Run the tests by clicking Fn + Shift + F10
   Alternatively, run the tests by executing the following command in the
   project root directory:
   
      ```
      ENV="testing" mvn test
      ```