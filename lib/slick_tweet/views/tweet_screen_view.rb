module SlickTweet
  
  class TweetsView < View
    
    # controller
    def render
      print info
      tweet_body = gets.chomp
      tweet = SlickTweet::Tweet.new(body: tweet_body, user_id: $current_user.id)
      if tweet.save
        puts success
        SlickTweet::current_screen = 'welcome'
      else
        puts "Unable to save tweet"
      end
    end

    # view
    def info
      "\nWhat\'s going in your mind?\n"
    end

    def success
      "\nYayy! Tweet Saved\n"
    end
  end

end