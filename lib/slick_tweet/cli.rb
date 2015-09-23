module SlickTweet	
	
	class CLI

		def invoke
			while true
				SlickTweet::View::HomeScreenView.new.show if $current_page = :home
			end
		end
	end

end