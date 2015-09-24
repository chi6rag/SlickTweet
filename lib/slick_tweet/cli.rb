module SlickTweet	
	
	class CLI

		def invoke
			until $current_screen == :exit
				case $current_screen
				when :home
					HomeScreenView.new.render
				when :login
					SessionsView.new.login
				when :sign_up
					SessionsView.new.sign_up
				when :welcome
					WelcomeScreenView.new.render
				else
					break 
				end
			end
		end
	end

end