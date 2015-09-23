module SlickTweet	
	
	class CLI
		def invoke
			print home_screen_options
			choice = gets.chomp
		end

    def home_screen_options
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