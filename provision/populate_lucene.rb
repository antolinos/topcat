
require 'rest-client'
require 'openssl'
require 'json'



sessionId = JSON.parse(RestClient::Request.execute({
	:method => :post,
	:url => 'https://localhost:8181/icat/session/',
	:payload => {
		:json => JSON.generate({
			:plugin => "simple",
			:credentials => [
				{:username => "root"},
				{:password => "root"}
			]
		})
	},
	:verify_ssl => OpenSSL::SSL::VERIFY_NONE
}))['sessionId']

['Investigation', 'Dataset', 'Datafile'].each do |entityName|
	RestClient::Request.execute({
		:method => :post,
		:url => "https://localhost:8181/icat/lucene/db/#{entityName}/",
		:payload => {:sessionId => sessionId},
		:verify_ssl => OpenSSL::SSL::VERIFY_NONE
	})
end
