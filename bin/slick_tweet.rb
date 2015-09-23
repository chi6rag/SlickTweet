#!/usr/bin/env ruby
require 'pg'
require_relative '../lib/slick_tweet.rb'

begin
	$con = PG.connect :dbname => 'slick_tweet'
	SlickTweet::CLI.invoke
rescue PG::Error => e
	puts e.message
	puts "Could not connect to the database"
	puts "Closing Unexpectedly"
ensure
	$con.close if $con
end