require 'slick_tweet'
require "rspec/expectations"

RSpec.describe SlickTweet::CLI do
	before(:all){
		CLI = SlickTweet::CLI.new
	}

	describe '#home_screen_options' do
		it "returns the correct options" do 
			options = "Welcome to SlickTweet\n"
			options << "---------------------\n"
			options << "1. Sign Up\n"
			options << "2. Login\n"
			options << "3. Exit\n"
			options << "Choose: "
			options
			expect(CLI.home_screen_options).to eq(options)
		end
	end
end