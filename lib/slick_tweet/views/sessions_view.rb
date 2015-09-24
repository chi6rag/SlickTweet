module SlickTweet
	
	class SessionsView < View

		# controller level logic
		# to be tested
		def login
			unless $current_user
				user_details = details_from_login
				system "clear"
				user = SlickTweet::User.find(user_details)
				unless user
					puts "Wrong login details! Try again"
					return
				else
					$current_user = user
				end
			end
			$current_screen = :welcome
			$current_user
		end

		# to be tested
		def sign_up
			unless $current_user
				user_details = details_from_signup
				system "clear"
				user = SlickTweet::User.create(user_details)
				if user
					$current_user = user
					$current_screen = :welcome
				else
					$current_user = nil
				end
				$current_user
			end
		end
		
		# view
		def details_from_login(clear: false)
			puts "\n\nSlickTweet Login\n"
			puts "----------------\n"
			print "Username / Email: "
			username_or_email = gets.chomp
			print "Password: "
			password = gets.chomp
			{username_or_email: username_or_email, password: password}
		end

		def details_from_signup
			puts "\n\nSlickTweet Signup\n"
			puts "----------------\n"
			print "Username: "
			username = gets.chomp
			print "Email: "
			email = gets.chomp
			print "Password: "
			password = gets.chomp
			{username: username, email: email, password: password}
		end

	end

end