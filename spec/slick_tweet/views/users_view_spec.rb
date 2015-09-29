require 'spec_helper'
require 'slick_tweet'
require 'pg'
require "rspec/expectations"

RSpec.describe SlickTweet::UsersView do
  before(:all) do
    $con = PG.connect :dbname => 'slick_tweet_testing'
  end
  let(:user) do
    SlickTweet::User.create(
      username: 'chi6rag',
      email: 'chi6rag@gmail.com',
      password: '11111111'
    )
  end
  let(:users_view){ SlickTweet::UsersView.new }

  after(:all) do
    $con.exec('DELETE FROM tweets')
    $con.exec('DELETE FROM users')
    $con.close if $con
  end
  
  describe '#timeline' do
    it "asks for user's username to view tweets of" do
      expected_text = /Enter username to view tweets of:/
      allow(users_view).to receive(:gets).and_return 'chi6rag'
      expect { users_view.timeline }.to output(expected_text).to_stdout
    end

    context 'logged in user' do
      context 'searches valid username' do
        it 'prints the tweets of the user' do
          tweet = SlickTweet::Tweet.new(body: 'hello!', user_id: user.id).save
          allow(users_view).to receive(:gets).and_return 'chi6rag'
          expect { users_view.timeline }.to output(tweet.body).to_stdout
        end
      end

      context 'searches invalid username' do
        it 'prints invalid username message' do
          allow(users_view).to receive(:gets).and_return 'foo_example'
          expect { users_view.timeline }.to output(/No such username exists/).to_stdout
        end
        it 'takes the user back to the welcome page' do
          allow(users_view).to receive(:gets).and_return 'foo_example'
          users_view.timeline
          expect(SlickTweet::current_page).to eq('welcome')
        end
      end
    end

    describe 'user not signed in' do
      context 'searches valid username' do
        it 'prints the tweets of the user' do
        end
      end

      context 'invalid username' do
        it 'prints invalid username message' do
        end
        it 'takes the user back to the home page' do
        end
      end
    end
  end

end