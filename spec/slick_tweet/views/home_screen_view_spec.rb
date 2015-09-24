require 'slick_tweet'
require "rspec/expectations"

RSpec.describe SlickTweet::HomeScreenView do
	before(:all){
		@home_screen_view = SlickTweet::HomeScreenView.new
	}

	describe '#options' do
		it "returns the correct options" do 
			options = "Welcome to SlickTweet\n"
			options << "--------------------\n"
			options << "1. Sign Up\n"
			options << "2. Login\n"
			options << "3. Exit\n"
			options << "Choose: "
			options
			expect(@home_screen_view.options).to eq(options)
		end
	end

	describe '#handle_choice' do
		before(:each){ $current_screen = :home}

		it "handles choice for sign up " do
			choice = "1"
			expect(@home_screen_view.handle_choice(choice)).to eq($current_screen)
			expect($current_screen).to eq(:sign_up)
		end

		it 'handles choice for login' do 
			choice = "2"
			expect(@home_screen_view.handle_choice(choice)).to eq($current_screen)
			expect($current_screen).to eq(:login)
		
		end

		it 'handles choice for exit' do
			choice = "3"
			expect(@home_screen_view.handle_choice(choice)).to eq($current_screen)
			expect($current_screen).to eq(:exit)
		end
	end

	
end