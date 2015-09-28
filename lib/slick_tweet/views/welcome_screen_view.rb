module SlickTweet
  
  class WelcomeScreenView < View
    
    def render
      # authorize only logged in users to use the app
      if $current_user.nil?
        puts "You need to login to continue"
        SlickTweet.current_screen = 'login'
        return
      end
      print options
      choice = gets.chomp
      handle_choice choice
    end

    # view
    def options
      puts ''
      options = "Welcome #{$current_user.username}\n"
      options_heading = '-' * options.length << "\n"
      options.prepend options_heading
      options << options_heading
      options << "What do you want to do today?\n"
      options << "1. Tweet\n"
      # options << "2. Search\n"
      options << "3. Your Tweets\n"
      options << "4. Timeline\n"
      # options << "5. Followers\n"
      # options << "6. Following\n"
      # options << "7. Edit Profile\n"
      options << "8. Logout\n"
      options << "Choose: "
      options
    end

    def handle_choice choice
      case choice
      when "1"
        # render sign up view
        SlickTweet.current_screen = 'tweet'
      when "2"
        # render login view
        SlickTweet.current_screen = 'search'
      when "3"
        SlickTweet.current_screen = 'your_tweets'
      when "4"
        SlickTweet.current_screen = 'timeline'
      when "5"
        SlickTweet.current_screen = 'followers'
      when "6"
        SlickTweet.current_screen = 'following'
      when "7"
        SlickTweet.current_screen = 'edit_profile'
      when "8"
        SlickTweet.current_screen = 'logout'
      else
        # to be tested
        puts "Wrong choice, try again!"
        print "Choose: "
        choice = gets.chomp
        handle_choice choice
      end
    end

  end

end