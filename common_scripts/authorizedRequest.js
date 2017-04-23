var request = require("request");
var Promise = require('es6-promise').Promise;
const parseConfigFile = require('./parseConfigurationFile');
const configuration = parseConfigFile();

const basicRequestOptions = {
	headers: {
		"Secret-authorization-token": configuration['secret.token']
	}
};

function makeRequest({ url, body = undefined, json = true, method }) {
	var requestOptions = Object.assign({}, basicRequestOptions);
	requestOptions = Object.assign(requestOptions, {
		url,
		body,
		json,
		method,
	});


	return new Promise((resolve, reject) => {
		request(requestOptions, (error, response, body) => {
			if (error || response.statusCode != 200) {
				reject({error, response});
			} else {
				resolve({response, body});
			}
		});
	});
}


module.exports = makeRequest;