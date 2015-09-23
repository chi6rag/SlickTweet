module SlickTweet	
	
	class CLI

		def invoke
			until $current_screen == :exit
				break if $current_screen == :welcome
				View::HomeScreenView.new.render if $current_screen == :home
				View::UsersView.new.login if $current_screen == :login
				View::UsersView.new.sign_up if $current_screen == :sign_up
			end
		end
	end

end