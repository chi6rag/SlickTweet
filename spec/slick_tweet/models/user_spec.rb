require 'slick_tweet'
require "rspec/expectations"

RSpec.describe SlickTweet::User do
	before(:all){
		@user
	}

	describe '#options' do
		it "returns the correct options" do 
			options = "Welcome to SlickTweet\n"
			options << "---------------------\n"
			options << "1. Sign Up\n"
			options << "2. Login\n"
			options << "3. Exit\n"
			options << "Choose: "
			options
			expect(@home_screen_view.options).to eq(options)
		end
	end

	
end