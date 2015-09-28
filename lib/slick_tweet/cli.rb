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
        when match(/^timeline/)
          params = extract_params('timeline', SlickTweet.current_screen)
          UsersView.new.timeline(*params)
        when 'welcome'
          WelcomeScreenView.new.render
        when 'tweet'
          TweetsView.new.render
        when 'your_tweets'
          TweetsView.new.users_tweets
        when 'logout'
          SessionsView.new.logout
        else
          break 
        end
      end
    end

    private

    def match(regex)
      SlickTweet::current_screen.match(regex) ? SlickTweet::current_screen : nil
    end

    # extract_params
    # extracts params from current screen if any
    # returns them in an array
    def extract_params(screen_prefix, current_screen_with_params)
      return nil if current_screen_with_params == screen_prefix
      current_screen_with_params.gsub(/^#{screen_prefix}/, '').split('_')
      .keep_if { |elem| !elem.empty? }
    end

  end

end