require 'spec_helper'
require 'pg'
require 'slick_tweet'
require "rspec/expectations"

RSpec.describe SlickTweet::User do
  before(:all){
    # establish pg connection
    $con = PG.connect :dbname => 'slick_tweet_testing'
  }

  after(:all){
    statement = "DELETE FROM users"
    $con.exec(statement)
    $con.close if $con
  }

  describe ".create" do 
    before(:each){
      @first_user = SlickTweet::User.create(
        username: 'palak1812',
        email: 'palak1812@gmail.com',
        password: '1111111111'
      )
    }

    context 'with unique params' do
      it "creates a new unique user" do
        expect{
          @second_user = SlickTweet::User.create(
            username: 'chi6rag',
            email: 'me@chi6rag.net',
            password: '1234567890'
          )
        }.to_not raise_exception
        expect(@second_user.username).to_not eq(@first_user.username)
        expect(@second_user.email).to_not eq(@first_user.email)
      end
    end

    context 'with duplicate params' do
      it 'does not create a new user' do
        @second_user = SlickTweet::User.create(
          username: 'palak1812',
          email: 'palak1812@gmail.com',
          password: '1111111111'
        )
        expect(@second_user).to be_nil
      end
    end
  
  end

end