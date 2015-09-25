require 'spec_helper'
require 'pg'
require 'slick_tweet'
require "rspec/expectations"

RSpec.describe SlickTweet::Tweet do
  before(:all){
    # establish pg connection
    $con = PG.connect :dbname => 'slick_tweet_testing'
    # create a test user
    @user = SlickTweet::User.create(
      username: 'test123',
      email: 'test123@example.com',
      password: '!Abcd1234'
    )
  }

  after(:each){
    $con.exec "DELETE FROM tweets"
  }

  after(:all){
    $con.exec "DELETE FROM tweets"
    $con.exec "DELETE FROM users"
    $con.close if $con
  }

  describe '.new' do
    it 'creates a new but unsaved tweet' do 
      count_before = SlickTweet::Tweet.count
      tweet = SlickTweet::Tweet.new(
                body: 'Testing Stuff',
                user_id: @user.id
              )
      count_after = SlickTweet::Tweet.count
      expect(tweet.id).to be_nil
      expect(count_before).to eq count_after
    end
  end

  describe '#save' do
    context 'with valid attributes' do
      it 'saves the tweet in the database' do
        count_before = SlickTweet::Tweet.count
        tweet = SlickTweet::Tweet.new(
                  body: 'Testing Stuff',
                  user_id: @user.id
                )
        tweet.save
        count_after = SlickTweet::Tweet.count
        expect(count_before).to_not eq(count_after)
        expect(count_after).to eq(1)
      end
    end
  end

  describe '.count' do
    it 'returns the number of tweets' do
      expect(SlickTweet::Tweet.count).to eq(0)
      tweet = SlickTweet::Tweet.new(
                body: 'Testing Stuff',
                user_id: @user.id
              )
      tweet.save
      expect(SlickTweet::Tweet.count).to eq(1)
    end
  end

end