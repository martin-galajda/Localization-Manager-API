var request = require("request");
var Promise = require('es6-promise').Promise;

/**
 *
 * @param url
 * @param json
 * @param method
 * @param basicAuthBase64Encoded
 * @returns {*}
 */
const makeRequest = ({ url, json = true, method, basicAuthBase64Encoded }) => {
	const requestOptions = {
		url,
		json,
		method,
		headers: {
			Authorization: `Basic ${basicAuthBase64Encoded}`
		}
	};


	return new Promise((resolve, reject) => {
		request(requestOptions, (error, response, body) => {
			console.log(`Response is ${response.statusCode}`);
			if (error || response.statusCode != 200) {
				console.log('Rejecting');
				reject({error, response});
			} else {
				console.log('resolving');
				resolve({response, body});
			}
		});
	});
};


module.exports = makeRequest;
