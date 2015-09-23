module SlickTweet
	class User
		attr_accessor :name, :email

		def self.create params
			statement = 'INSERT INTO users(username, email, password) '
			statement << "VALUES('#{params[:username]}', '#{params[:email]}', #{params[:password]})"
			$con.exec(statement)
		end

		def self.find params
			statement = 'SELECT * FROM users WHERE '
			statement << "username='#{params[:username]}' AND "
			statement << "email='#{params[:email]}' AND "
			statement << "password='#{params[:password]}'"
			$con.exec(statement)
		end

	end
end