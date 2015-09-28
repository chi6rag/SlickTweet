require 'spec_helper'
require 'slick_tweet'
require "rspec/expectations"

RSpec.describe SlickTweet::CLI do
  before(:all){ @cli = SlickTweet::CLI.new }

  describe '#match' do
    before(:all){ SlickTweet::current_screen = 'timeline_chi6rag' }

    context 'current_screen matches regex' do
      it 'returns the current_screen' do
        expect(@cli.send(:match, 'timeline')).to eq('timeline_chi6rag')
      end
    end

    context 'current_screen does not match regex' do
      it 'returns nil' do
        expect(@cli.send(:match, 'home')).to be_nil
      end
    end
  end

  describe '#extract_params' do
    before(:each){ @screen_prefix = 'timeline' }

    context 'screen_prefix equals current_screen_with_params' do
      it 'returns current_screen_with_params' do
        current_screen_with_params = @screen_prefix
        expect(@cli.send(:extract_params, @screen_prefix,
               current_screen_with_params)
        ).to be_nil
      end
    end

    context 'screen_prefix does not equal current_screen_with_params' do
      it 'returns params' do
        current_screen_with_params = @screen_prefix + '_chi6rag'
        expect(@cli.send(:extract_params, @screen_prefix,
               current_screen_with_params)
        ).to eq(['chi6rag'])
      end
    end
  end

end