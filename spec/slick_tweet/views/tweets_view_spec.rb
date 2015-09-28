require 'spec_helper'
require 'slick_tweet'
require 'pg'
require "rspec/expectations"

RSpec.describe SlickTweet::TweetsView do
	before(:all) do
	 SlickTweet::current_screen = 'tweet'
	 @tweets_view = SlickTweet::TweetsView.new
	end

	describe '#info' do
		it 'returns the right tweets message' do
			expect(@tweets_view.send(:info)).to eq("\nWhat\'s going in your mind?\n")
		end
	end

	describe '#success' do
		it 'returns the correct tweets save message' do
			expect(@tweets_view.send(:success)).to eq("\nYayy! Tweet Saved\n")
		end
	end

end