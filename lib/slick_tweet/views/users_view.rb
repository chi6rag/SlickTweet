module SlickTweet::View
	
	class UsersView

		def login
			unless $current_user
				user_details = details_from_login
				$current_user = SlickTweet::User.find(user_details)
			end
			$current_screen = :welcome
			puts "----- logged in ------"
			$current_user
		end

		def sign_up
			unless $current_user
				user_details = details_from_signup
				user = SlickTweet::User.create(user_details)
				if user
					puts "----- signed up ------"
					$current_user = user
					$current_screen = :welcome
				else
					$current_user = nil
				end
				$current_user
			end
		end

		private
		
		def details_from_login
			puts "\n\nSlickTweet Login\n"
			puts "----------------\n"
			print "Username / Email: "
			username_or_email = gets.chomp
			print "\nPassword: "
			password = gets.chomp
			{username_or_email: username_or_email, password: password}
		end

		def details_from_signup
			puts "\n\nSlickTweet Signup\n"
			puts "----------------\n"
			print "Username: "
			username = gets.chomp
			print "\nEmail: "
			email = gets.chomp
			print "\nPassword: "
			password = gets.chomp
			{username: username, email: email, password: password}
		end

	end

end