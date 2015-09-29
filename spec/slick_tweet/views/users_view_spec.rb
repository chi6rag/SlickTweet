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
      expect { users_view.timeline }.to output(expected_text).to_stdout
    end

    context 'logged in user' do
      context 'searches valid username' do
        it 'prints the tweets of the user' do
        end
      end

      context 'searches invalid username' do
        it 'prints invalid username message' do
        end
        it 'takes the user back to the welcome page' do
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