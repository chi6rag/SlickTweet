module SlickTweet
  class UsersView < View
    include SlickTweet::Helpers::TweetScreenHelper
    
    def timeline
      print "\nEnter username to view tweets of:"
      username = gets.chomp
      user = SlickTweet::User.find(username: username)
      if user_signed_in? && username_found?(user) 
        pretty_print user.tweets
        SlickTweet::current_screen = 'welcome'
      elsif user_signed_in? && username_not_found?(user)
        puts 'No such username exists'
        SlickTweet::current_screen = 'welcome'
      elsif !user_signed_in? && username_found?(user)
        pretty_print user.tweets
        SlickTweet::current_screen = 'home'
      else
        puts 'No such username exists'
        SlickTweet::current_screen = 'home'
      end
    end

    private

    def username_found? user
      user.nil? ? false : true
    end

    def username_not_found? user
      user.nil? ? true : false
    end

  end
end