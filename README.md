TwitchBlade
===========

TwitchBlade is a command line app which lets you share messages upto 140 characters.

Setting Up: Development
-----------------------
1. Create a database twitchblade_development with user chi6rag
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
3. Fire up the IntelliJ Idea IDE and import Maven Dependencies
4. Run the tests by clicking Fn + Shift + F10
   Alternatively, run the tests by executing the following command in the
   project root directory:
      ```
      ENV="testing" mvn test
      ```

Setting Up: Testing
-----------------------
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
3. Run the tests as:
   ```
   ENV="testing" mvn test
   ```