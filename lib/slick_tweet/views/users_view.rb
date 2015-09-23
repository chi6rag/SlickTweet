module SlickTweet::View
	
	class UsersView

		def login
			unless $current_user
				user_details = details_from_login
				$current_user = SlickTweet::User.find(user_details)
			end
			$current_screen = :welcome
			$current_user
		end

		def sign_up
			unless $current_user
				user_details = details_from_signup
				user = SlickTweet::User.new(user_details)
				if user.save
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
			puts "SlickTweet Login\n"
			puts "----------------\n"
			print "Username / Email: "
			username_or_email = gets.chomp
			print "\nPassword: "
			password = gets.chomp
			{username_or_email: username_or_email, password: password}
		end

		def details_from_signup
			puts "SlickTweet Signup\n"
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