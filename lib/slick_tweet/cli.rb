module SlickTweet 
  
  class CLI

    def invoke
      SlickTweet.current_screen = 'home'
      until SlickTweet.current_screen == 'exit'
        case SlickTweet.current_screen
        when 'home'
          HomeScreenView.new.render
        when 'login'
          SessionsView.new.login
        when 'sign_up'
          SessionsView.new.sign_up
        when 'welcome'
          WelcomeScreenView.new.render
        when 'tweet'
          TweetsView.new.render
        when 'logout'
          SessionsView.new.logout
        else
          break 
        end
      end
    end
  end

end