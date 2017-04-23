require("babel-register");
const MochaPackage = require('mocha');
const mocha = new MochaPackage({});
const path = require('path');
const os = require('os');
var Promise = require('es6-promise').Promise;

mocha.addFile(path.resolve(path.join(__dirname, 'specs', 'projects.js')));
mocha.addFile(path.resolve(path.join(__dirname, 'specs', 'project_status.js')));
mocha.addFile(path.resolve(path.join(__dirname, 'specs', 'converters.js')));

let passed = 0;
let failed = 0;
let errors = [];

const runTests = () => {
	return new Promise((resolve) => {
		mocha.run()
			.on('pass', function (test) {
				passed = passed + 1;
			})
			.on('fail', function (test,err)  {
				failed = failed + 1;
				errors.push(err);
			})
			.on('end', function() {
				resolve({ passed, failed, errors});
			});
	});
};

runTests().then(({ passed, failed, errors}) => {
	console.log(`
		Passed: ${passed}${os.EOL}
		Failed: ${failed}${os.EOL}
		Errors: ${errors.join(os.EOL)}
	`)
});

module.exports = runTests;

