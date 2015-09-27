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
    $con.exec('DELETE FROM tweets')
    $con.exec('DELETE FROM users')
    $con.close if $con
  }

  describe ".create" do 
    it 'creates a new user' do
      expect do
        SlickTweet::User.create(
          username: 'palak1812',
          email: 'palak1812@gmail.com',
          password: '1111111111'
        )
      end.to change{ SlickTweet::User.count }.by(1)
    end

    context 'with unique params' do
      it "creates a new unique user" do
        expect{
          @second_user = SlickTweet::User.create(
            username: 'chi6rag',
            email: 'me@chi6rag.net',
            password: '1234567890'
          )
        }.to_not raise_exception
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

  describe '#tweets' do
    before(:each) do
      # create a test user
      @user = SlickTweet::User.create(
        username: 'foo_example',
        email: 'foo@example.com',
        password: '1111111111'
      )
      # save 2 tweets
      SlickTweet::Tweet.new(body: 'Hello World!', user_id: @user.id).save
      SlickTweet::Tweet.new(body: 'Hello World!', user_id: @user.id).save
    end

    it "returns all the tweets of the user" do
      @user.tweets.each do |tweet|
        expect(tweet[:user_id]).to eq(@user.id.to_i)
      end
      expect(@user.tweets.count).to eq(2)
    end
  end

end