require 'spec_helper'
require 'slick_tweet'
require "rspec/expectations"

RSpec.configure do |c|
  c.include SlickTweet
  c.include SlickTweet::Helpers
  c.include SlickTweet::Helpers::TweetScreenHelper
end

describe '#pretty_print' do
  context 'with no tweets' do
    it "prints an error message on stdout" do
      tweets = []
      expect{ pretty_print tweets }.to output("\nOops! Looks like you don't have any tweets\n").to_stdout
    end
  end

  context 'with two tweets' do
    it "prints body of two tweets on stdout" do
      tweets = [ {id: 1, body: 'lorem', user_id: '1', created_at: '2015-09-28 10:20:30'},
               {id: 2, body: 'ipsum', user_id: '1', created_at: '2015-09-28 10:20:30'} ]
      expect{pretty_print tweets }.to output(/lorem/).to_stdout
      expect{pretty_print tweets }.to output(/ipsum/).to_stdout
    end
  end
end
