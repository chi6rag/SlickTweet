require 'spec_helper'
require 'pg'
require 'slick_tweet'
require "rspec/expectations"

RSpec.describe SlickTweet::Tweet do
  before(:all) do
    # establish pg connection
    $con = PG.connect :dbname => 'slick_tweet_testing'
    # create a test user
    @user = SlickTweet::User.create(
      username: 'test123',
      email: 'test123@example.com',
      password: '!Abcd1234'
    )
  end

  after(:each){ $con.exec "DELETE FROM tweets" }

  after(:all) do
    $con.exec "DELETE FROM tweets"
    $con.exec "DELETE FROM users"
    $con.close if $con
  end

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
    before(:each) { @count_before = SlickTweet::Tweet.count }
    after(:each) { $con.exec "DELETE FROM tweets" }

    context 'with valid attributes' do
      it 'saves the tweet in the database' do
        expect{
          tweet = SlickTweet::Tweet.new(
                    body: 'Testing Stuff',
                    user_id: @user.id
                  )
          tweet.save
          }.to change{SlickTweet::Tweet.count}.by 1
      end

      it "returns a tweet" do
        tweet = SlickTweet::Tweet.new(
            body: 'Testing Stuff',
            user_id: @user.id
        ).save
        expect(tweet.id.is_a? Integer).to be_truthy
        expect(tweet.user_id).to eq(@user.id.to_i)
        expect(tweet.body).to eq('Testing Stuff')
      end
    end
    
    context 'with invalid attributes' do
      it "does not save empty tweets in database" do
        tweet = SlickTweet::Tweet.new(
                  body: '',
                  user_id: @user.id
                )
        tweet.save
        expect(@count_before).to eq(SlickTweet::Tweet.count)
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

  # describe '#psql_to_tweet' do
  # end

end