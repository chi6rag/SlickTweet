module SlickTweet::View

	class HomeScreenView
		include SlickTweet::View
		
		def render
			puts options
			choice = gets.chomp
			handle_choice choice
		end

		def options
			options = "Welcome to SlickTweet\n"
			options << "---------------------\n"
			options << "1. Sign Up\n"
			options << "2. Login\n"
			options << "3. Exit\n"
			options << "Choose: "
			options
		end

		def handle_choice choice
			case choice
			when 1 
				# render sign up view
				
			when 2
				# render login view
			when 3
				# exit
			else
				# wrong choice
				# handle choice again
			end
		end

	end

end