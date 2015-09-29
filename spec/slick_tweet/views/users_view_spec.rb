require 'spec_helper'
require 'slick_tweet'
require 'pg'
require "rspec/expectations"

RSpec.describe SlickTweet::UsersView do
  before(:all) do
    $con = PG.connect :dbname => 'slick_tweet_testing'
    @users_view = SlickTweet::UsersView.new
    @user = SlickTweet::User.create(
              username: 'chi6rag',
              email: 'chi6rag@gmail.com',
              password: '11111111'
            )
  end

  after(:all) do
    $con.exec('DELETE FROM tweets')
    $con.exec('DELETE FROM users')
    $con.close if $con
  end
  
  describe '#timeline' do
    it "asks for user's username to view tweets of" do
      expected_text = /Enter username to view tweets of:/
      allow( @users_view ).to receive(:gets).and_return 'chi6rag'
      expect { @users_view.timeline }.to output(expected_text).to_stdout
    end

    context 'logged in user' do
      before(:all){ $current_user = @user }

      context 'searches valid username' do
        # EXTRACT INTO SHARED EXAMPLES ------
        it 'prints the tweets of the user' do
          tweet = SlickTweet::Tweet.new(body: 'hello!', user_id: @user.id).save
          allow(@users_view).to receive(:gets).and_return 'chi6rag'
          expect { @users_view.timeline }.to output(/#{tweet.body}/).to_stdout
        end
        # ------ EXTRACT INTO SHARED EXAMPLES
      end

      context 'searches invalid username' do
        # EXTRACT INTO SHARED EXAMPLES ------
        it 'prints invalid username message' do
          allow(@users_view).to receive(:gets).and_return('foo_example')
          expect { @users_view.timeline }.to output(/No such username exists/).to_stdout
        end
        # ------ EXTRACT INTO SHARED EXAMPLES
      end

      it 'takes the user back to the welcome page' do
        allow(@users_view).to receive(:gets).and_return 'foo_example'
        @users_view.timeline
        expect(SlickTweet::current_screen).to eq('welcome')
      end
    end

    describe 'user not signed in' do
      before(:all){ $current_user = nil }

      context 'searches valid username' do
        # EXTRACT INTO SHARED EXAMPLES ------
        it 'prints the tweets of the user' do
          tweet = SlickTweet::Tweet.new(body: 'hello!', user_id: @user.id).save
          allow(@users_view).to receive(:gets).and_return 'chi6rag'
          expect { @users_view.timeline }.to output(/#{tweet.body}/).to_stdout
        end
        # ------ EXTRACT INTO SHARED EXAMPLES
      end

      context 'invalid username' do
        # EXTRACT INTO SHARED EXAMPLES ------
        it 'prints invalid username message' do
          allow(@users_view).to receive(:gets).and_return('foo_example')
          expect { @users_view.timeline }.to output(/No such username exists/).to_stdout
        end
        # ------ EXTRACT INTO SHARED EXAMPLES
      end

      it 'takes the user back to the home page' do
        allow(@users_view).to receive(:gets).and_return 'foo_example'
        @users_view.timeline
        expect(SlickTweet::current_screen).to eq('home')
      end
    end
  end

  describe '#username_found?' do
    context 'argument user is not nil' do
      it 'returns true' do
        user = double(SlickTweet::User)
        expect( @users_view.send(:username_found?, user) ).to be_truthy
      end
    end

    context 'argument user is nil' do
      it 'returns true' do
        user = nil
        expect( @users_view.send(:username_found?, user) ).to be false
      end
    end
  end

  describe '#username_not_found?' do
    context 'argument user is not nil' do
      it 'returns false' do
        user = double(SlickTweet::User)
        expect( @users_view.send(:username_not_found?, user) ).to be false
      end
    end

    context 'argument user is nil' do
      it 'returns true' do
        user = nil
        expect( @users_view.send(:username_not_found?, user) ).to be true
      end
    end
  end

end