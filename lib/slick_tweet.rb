# require models
require_relative './slick_tweet/record.rb'
# autoload models in './lib/slick_tweet/models/' directory
Dir.foreach('./lib/slick_tweet/models/') do |filename|
	(require_relative './slick_tweet/models/' + filename) unless(filename == '.' || filename == '..')
end

# require views
require_relative './slick_tweet/view.rb'
# autoload views in './lib/slick_tweet/views/' directory
Dir.foreach('./lib/slick_tweet/views/') do |filename|
	(require_relative './slick_tweet/views/' + filename) unless(filename == '.' || filename == '..')
end

# require cli router
require_relative './slick_tweet/cli'

# require helpers
require_relative './slick_tweet/helpers'

module SlickTweet
	def self.current_screen
		@current_screen
	end

	def self.current_screen=(current_screen)
		@current_screen = current_screen
	end
end