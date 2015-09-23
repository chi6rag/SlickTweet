# require models
require_relative './slick_tweet/record.rb'
require_relative './slick_tweet/models/user.rb'

# require views
require_relative './slick_tweet/view.rb'
require_relative './slick_tweet/views/home_screen_view'
require_relative './slick_tweet/views/users_view'

# require cli
require_relative './slick_tweet/cli'

# require helpers
require_relative './slick_tweet/helpers'

module SlickTweet
	$current_screen = :home
end