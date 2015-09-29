require 'spec_helper'
require 'pg'
require 'slick_tweet'
require "rspec/expectations"

RSpec.describe SlickTweet::User do
  before(:all){
    # establish pg connection
    $con = PG.connect :dbname => 'slick_tweet_testing'
  }

  after(:all) do
    $con.exec('DELETE FROM tweets')
    $con.exec('DELETE FROM users')
    $con.close if $con
  end

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
    before(:all) do
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

    it 'returns all the tweets of the user' do
      @user.tweets.each do |tweet|
        expect(tweet[:user_id]).to eq(@user.id.to_i)
      end
      expect(@user.tweets.count).to eq(2)
    end
  end

  describe '.find_for_login' do
    context 'with valid params' do
      it 'returns the user' do
        expect( SlickTweet::User.find_for_login( username_or_email: 'palak1812',
                password: '1111111111' )
        ).to be_kind_of(SlickTweet::User)
      end
    end

    context 'with invalid params' do
      it 'returns nil' do
        expect( SlickTweet::User.find_for_login( username_or_email: 'testfoo',
                password: '1111111111' )
        ).to be_nil
      end
    end
  end

  describe '.find' do
    before(:all) do
      # create a test user
      @user = SlickTweet::User.create(
        username: 'bar_example',
        email: 'bar@example.com',
        password: '1111111111'
      )
    end

    context 'with valid params' do
      it 'returns a user' do
        user = SlickTweet::User.find(username: 'bar_example')
        expect(user).to be_kind_of(SlickTweet::User)
        expect(user.id).to be_kind_of(Integer)
        expect(user.username).to eq('bar_example')
        expect(user.email).to eq('bar@example.com')
        expect(user.password).to eq('1111111111')
      end
    end

    context 'with invalid params' do
      it 'returns blank' do
        expect(SlickTweet::User.find(username: 'hello')).to be_nil
      end
    end
  end

end