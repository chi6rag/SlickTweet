module SlickTweet

	class HomeScreenView < View
		
		# controller
		
		# render
		# test this method using an integration test suite
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
				puts "Wrong choice"
				choice = gets.chomp
				handle_choice choice
			end
		end

		# view
		def options
			options = "Welcome to SlickTweet\n"
			options << "--------------------\n"
			options << "1. Sign Up\n"
			options << "2. Login\n"
			options << "3. Exit\n"
			options << "Choose: "
			options
		end

	end

end