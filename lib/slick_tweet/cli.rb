module SlickTweet	
	
	class CLI

		def invoke
			until $current_screen == :exit
				break if $current_screen == :welcome
				HomeScreenView.new.render if $current_screen == :home
				SessionsView.new.login if $current_screen == :login
				SessionsView.new.sign_up if $current_screen == :sign_up
			end
		end
	end

end