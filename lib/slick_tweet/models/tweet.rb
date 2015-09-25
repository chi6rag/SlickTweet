module SlickTweet
	class Tweet < Record
		attr_accessor :body
		attr_reader :created_at, :id, :user_id

		def initialize(id: nil, body: nil, user_id: nil, created_at: nil)
			@id = id || nil
			@body = body
			@user_id = user_id
			@created_at = created_at || nil
		end

		# SlickTweet::Tweet.count
		# returns the number of tweets in the database
		def self.count
			($con.exec 'SELECT COUNT(*) FROM tweets').values.flatten[0].to_i
		end

	end
end