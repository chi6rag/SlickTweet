# require views
require_relative './slick_tweet/view.rb'
require_relative './slick_tweet/views/home_screen_view'

# require cli
require_relative './slick_tweet/cli'

# require helpers
require_relative './slick_tweet/helpers'

module SlickTweet
	$current_page = :home
end