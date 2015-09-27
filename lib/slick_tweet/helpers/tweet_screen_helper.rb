module SlickTweet
  module Helpers

    module TweetScreenHelper

      def pretty_print tweets
        puts "\nYour Tweets"
        puts '-----------'
        formatted_tweets = tweets.map do |tweet|
          [ tweet[:body], time_ago_in_words(tweet[:created_at]), '------' ]
        end
        puts formatted_tweets
      end

      private

      def time_ago_in_words(date)
        time_difference_in_seconds = ((DateTime.now - DateTime.parse(date))
                                      .to_f * 24 * 3600).to_i
        case time_difference_in_seconds
        when 0...60
          seconds = time_difference_in_seconds
          "#{seconds} #{pluralize(seconds, 'second')} ago"
        when 60...3600
          minutes = time_difference_in_seconds/60
          "#{minutes} #{pluralize(minutes, 'minute')} ago"
        when 3600...86400
          hours = time_difference_in_seconds/3600
          "#{hours} #{pluralize(hours, 'hour')} ago"
        else
          days = time_difference_in_seconds/86400
          "#{days} #{pluralize(days, 'day')} ago"
        end
      end

      def pluralize(number, singular_string)
        number > 1 ? singular_string + 's' : singular_string
      end

    end

  end
end