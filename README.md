SlickTweet
==========

SlickTweet is a command line social networking service which lets its users
post messages up to 140 characters.

Setup Development Environment
-----------------------------
1. Clone the app
2. Run bundle install
3. In PostgreSQL
   Execute the following commands

   CREATE TABLE users(
		id SERIAL PRIMARY KEY,
		username varchar(20) NOT NULL UNIQUE,
		email varchar(20) NOT NULL UNIQUE,
		password varchar(30) NOT NULL
   );