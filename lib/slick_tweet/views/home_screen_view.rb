module SlickTweet

  class HomeScreenView < View
    
    # controller
    
    # render
    # test this method using an integration test suite
    def render
      print options
      choice = gets.chomp
      handle_choice choice
    end

    private
    
    def handle_choice choice
      case choice
      when "1"
        # render sign up view
        SlickTweet.current_screen = 'sign_up'
      when "2"
        # render login view
        SlickTweet.current_screen = 'login'
      when "3"
        SlickTweet.current_screen = 'timeline'
      when "4"
        SlickTweet.current_screen = 'exit'
      else
        print "\nWrong choice\n"
        print 'Choose: '
        choice = gets.chomp
        handle_choice choice
      end
    end
    
    # view
    def options
      options = "Welcome to SlickTweet\n"
      options << "--------------------\n"
      options << "1. Sign Up\n"
      options << "2. Login\n"
      options << "3. View Timeline\n"
      options << "4. Exit\n"
      options << "Choose: "
      options
    end

  end

end