require("babel-polyfill");
require("es6-shim");
var request = require("request");
var Promise = require('es6-promise').Promise;

const basicRequestOptions = {
    headers: {
        "Secret-authorization-token": "123456"
    }
};


/**
 *
 * @param []
 * @returns {*}
 */
function makeRequest({ url, body, json, method }) {
    var requestOptions = Object.assign({}, basicRequestOptions);
    requestOptions = Object.assign(requestOptions, {
        url,
        body,
        json,
        method,
    });


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
}


module.exports = makeRequest;