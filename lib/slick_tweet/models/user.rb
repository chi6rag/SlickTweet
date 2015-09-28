module SlickTweet
  class User < Record
    attr_accessor :username, :email, :password
    attr_reader :id

    def initialize(id, username, email, password)
      @id = id
      @username = username
      @email = email
      @password = password
    end

    def self.create params
      statement = 'INSERT INTO users(username, email, password) '
      statement << "VALUES('#{params[:username]}', '#{params[:email]}', '#{params[:password]}')"
      begin
        $con.exec statement
      rescue PG::Error => e
        puts e.message
        puts ''
        return nil
      end
      find_for_login(username_or_email: params[:username], password: params[:password])
    end

    # SlickTweet::User.count
    # returns the number of users in the database
    def self.count
      ($con.exec 'SELECT COUNT(*) FROM users').values.flatten[0].to_i
    end

    def tweets
      SlickTweet::Tweet.where(user_id: self.id)
    end

    def self.find_for_login params
      statement = 'SELECT * FROM users WHERE '
      statement << "(username='#{params[:username_or_email]}' OR "
      statement << "email='#{params[:username_or_email]}') AND "
      statement << "password='#{params[:password]}'"
      values_found = $con.exec(statement).values
      return nil if values_found.flatten.empty?
      new(*values_found.first)
    end

    # todo
    # make find and where DRY
    # reference: http://ryanangilly.com/post/234897271/dynamically-adding-class-methods-in-ruby
    def self.find **params
      statement = 'SELECT * FROM users WHERE '
      condition =  params.map do |key, value|
        if(value && (value.is_a? String))
          " #{key}='#{value}' "
        elsif value
          " #{key}=#{value} "
        else
          nil
        end
      end.keep_if { |elem| !elem.nil? }.join('AND')
      statement << condition
      statement << ' LIMIT 1'
      begin
        res = $con.exec(statement).values[0]
        return nil if res.nil? || res.empty?
      rescue PG::Error => e
        puts e.message
        puts ''
        return nil
      end
      User.new(res[0].to_i, res[1], res[2], res[3])
    end

  end
end