require 'spec_helper'
require 'slick_tweet'
require "rspec/expectations"

RSpec.describe SlickTweet::View do
  before(:all) do
    $current_user = double(SlickTweet::User)
    @view = SlickTweet::View.new
  end

  describe '#user_signed_in?' do
    context 'when signed in' do
      it 'returns true' do
        expect(@view.user_signed_in?).to be_truthy
      end
    end

    context 'when not signed in' do
      it 'returns false' do
        $current_user = nil
        expect(@view.user_signed_in?).to be_falsey
      end
    end
  end

end