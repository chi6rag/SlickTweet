SlickTweet
==========

SlickTweet is a command line social networking service which lets its users
post messages up to 140 characters.

Setup Development Environment
-----------------------------
1. Clone the app
2. Install the required dependencies
   PostgreSQL
      Ubuntu: sudo apt get install postgres
      Mac:    brew install postgres
   gem install pg
2. Run bundle install
3. Open PostgreSQL using the following command
   $ psql
4. Create a new slick_tweet database
   =# CREATE DATABASE slick_tweet;
5. Add a users table
   CREATE TABLE users(
		id SERIAL PRIMARY KEY,
		username varchar(20) NOT NULL UNIQUE,
		email varchar(20) NOT NULL UNIQUE,
		password varchar(30) NOT NULL
   );

Setup the Testing Environment
-----------------------------
1. Open PostgreSQL using the following command
   $ psql
2. Create a new slick_tweet_testing database
   =# CREATE DATABASE slick_tweet_testing;
3. Add a users table
   slick_tweet_testing=# CREATE TABLE users(
                            id SERIAL PRIMARY KEY,
                            username varchar(20) NOT NULL UNIQUE,
                            email varchar(20) NOT NULL UNIQUE,
                            password varchar(30) NOT NULL
                         );
