require 'spec_helper'
require 'slick_tweet'
require "rspec/expectations"

RSpec.describe SlickTweet::SessionsView do
  before(:all){
    @sessions_view = SlickTweet::SessionsView.new
  }

  describe '#logout' do
    before(:each) do
      $current_user = double(:user)
      SlickTweet::current_screen = 'welcome'
    end

    it 'changes current screen to home' do
      @sessions_view.logout
      expect(SlickTweet::current_screen).to eq('home')
    end

    it 'sets current_user to nil' do 
      @sessions_view.logout
      expect($current_user).to be_nil
    end
  end

  describe '#details_from_login' do
    it "gives a hash of username_or_email and password" do
      allow(@sessions_view).to receive(:gets).and_return('chi6rag', '1234567890')

      expect(@sessions_view.details_from_login)
      .to eq({username_or_email: 'chi6rag', password: '1234567890'})
    end
  end

  describe '#details_from_signup' do
    it 'gives a hash of username, email and password' do
      allow(@sessions_view).to receive(:gets)
      .and_return('chi6rag', 'me@chi6rag.net', '1234567890')

      expect(@sessions_view.details_from_signup)
      .to eq({username: 'chi6rag', email: 'me@chi6rag.net', password: '1234567890'})
    end
  end

end
