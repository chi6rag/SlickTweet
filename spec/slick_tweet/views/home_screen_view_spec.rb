require 'spec_helper'
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
      options << "3. View Timeline\n"
      options << "4. Exit\n"
      options << "Choose: "
      options
      expect(@home_screen_view.send(:options)).to eq(options)
    end
  end

  describe '#handle_choice' do
    before(:each){ SlickTweet::current_screen = 'home'}

    {'sign_up': "1", 'login': "2", 'timeline': '3', 'exit': "4"}.each do |screen_name, choice|
      it "handles choice for #{screen_name}" do
        expect( @home_screen_view.send(:handle_choice, choice) ).to eq(SlickTweet::current_screen)
        expect(SlickTweet::current_screen).to eq(screen_name.to_s)
      end
    end

  end

end