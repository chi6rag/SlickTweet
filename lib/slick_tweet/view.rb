module SlickTweet
  class View
    
    def user_signed_in?
      $current_user ? true : false
    end

  end
end