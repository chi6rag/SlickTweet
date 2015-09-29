module SlickTweet
  class UsersView < View
    include SlickTweet::Helpers::TweetScreenHelper
    
    # controller
    def timeline
      print ask_for_username
      username = gets.chomp
      user = SlickTweet::User.find(username: username)
      if username_found? user
        pretty_print user.tweets
      else
        puts 'No such username exists'
      end
      SlickTweet::current_screen = (user_signed_in? ? 'welcome' : 'home')
      return
    end

    private

    # views
    def ask_for_username
      "\nEnter username to view tweets of:"
    end

    def username_found? user
      user.nil? ? false : true
    end

    def username_not_found? user
      user.nil? ? true : false
    end

  end
end