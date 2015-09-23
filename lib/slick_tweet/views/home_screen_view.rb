module SlickTweet::View

	class HomeScreenView
		include SlickTweet::View
		
		def render
			puts options
			choice = gets.chomp
			handle_choice choice
		end

		def handle_choice choice
			case choice
			when "1"
				# render sign up view
				$current_screen = :sign_up
			when "2"
				# render login view
				$current_screen = :login
			when "3"
				$current_screen = :exit
			else
				# wrong choice
				# handle choice again
			end
		end

		private

		def options
			options = "Welcome to SlickTweet\n"
			options << "---------------------\n"
			options << "1. Sign Up\n"
			options << "2. Login\n"
			options << "3. Exit\n"
			options << "Choose: "
			options
		end

	end

end