require("babel-register");
const path = require('path');
const os = require('os');
var Promise = require('es6-promise').Promise;


let passed = 0;
let failed = 0;
let errors = [];

const runTests = () => {
	const MochaPackage = require('mocha');
	const mocha = new MochaPackage({
		timeout: 20000 // in ms
	});

	mocha.addFile(path.resolve(path.join(__dirname, 'specs', 'projects.js')));
	mocha.addFile(path.resolve(path.join(__dirname, 'specs', 'project_status.js')));
	mocha.addFile(path.resolve(path.join(__dirname, 'specs', 'converters.js')));

	passed = 0;
	failed = 0;
	errors = [];
	return new Promise((resolve) => {
		mocha.run()
			.on('pass', function (test) {
				passed = passed + 1;
			})
			.on('fail', function (test, err)  {
				failed = failed + 1;
				errors.push({
					test,
					err
				});
			})
			.on('end', function() {
				resolve({ passed, failed, errors});
			});
	});
};

module.exports = runTests;

