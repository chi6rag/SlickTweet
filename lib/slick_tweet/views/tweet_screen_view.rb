module SlickTweet
  
  class TweetsView < View
    include SlickTweet::Helpers::TweetScreenHelper

    # controller
    def render
      print info
      tweet_body = gets.chomp
      tweet = SlickTweet::Tweet.new(body: tweet_body, user_id: $current_user.id)
      if tweet.save
        system("clear")
        puts success
        SlickTweet::current_screen = 'welcome'
      else
        puts "Unable to save tweet"
      end
    end

    # users_tweets
    def users_tweets
      pretty_print $current_user.tweets
      SlickTweet::current_screen = 'welcome'
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