require 'slick_tweet'
require 'pg'
require "rspec/expectations"

RSpec.describe SlickTweet::WelcomeScreenView do
  before(:all){
    @welcome_screen_view = SlickTweet::WelcomeScreenView.new
    # establish pg connection
    $con = PG.connect :dbname => 'slick_tweet_testing'
    # create one user
    statement = "INSERT INTO users(username, email, password) "
    statement << "VALUES('chi6rag', 'me@chi6rag.net', '1234567890')"
    $con.exec(statement)
    # set $current_user to created user
    user = SlickTweet::User.find(username_or_email: 'chi6rag', password: '1234567890')
    $current_user = user
    # set current screen as welcome
    $current_screen = :welcome
  }

  after(:all){
    statement = "DELETE FROM users"
    $con.exec(statement)
    $con.close if $con
  }

  it "renders the correct view" do
    expect($current_screen).to eq(:welcome)
  end

  describe "#options" do
    it "displays the correct options" do 
      options = "Welcome #{$current_user.username}\n"
      options_heading = '-' * options.length << "\n"
      options.prepend options_heading
      options << options_heading
      options << "What do you want to do today?\n"
      options << "1. Tweet\n"
      options << "2. Search\n"
      options << "3. Your Tweets\n"
      options << "4. Timeline\n"
      options << "5. Followers\n"
      options << "6. Following\n"
      options << "7. Edit Profile\n"
      options << "8. Logout\n"
      options << "Choose: "
      expect(@welcome_screen_view.options).to eq(options)
    end
  end

  describe '#handle_choice' do
    before(:each){ $current_screen = :home}

    {
      tweet: "1", search: "2", your_tweets: "3", timeline: "4",
      followers: "5", following: "6", edit_profile: "7", logout: "8"
    }.each do |screen_name, choice|
      it "handles choice for #{screen_name}" do 
        expect(@welcome_screen_view.handle_choice(choice)).to eq(screen_name)
        expect($current_screen).to eq(screen_name)
      end
    end

  end

end