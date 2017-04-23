require("babel-register");
const path = require('path');
const os = require('os');
const MochaPackage = require('mocha');
const mocha = new MochaPackage({
	timeout: 20000
});

mocha.addFile(path.resolve(path.join(__dirname, 'specs', 'projects.js')));
mocha.addFile(path.resolve(path.join(__dirname, 'specs', 'project_status.js')));
mocha.addFile(path.resolve(path.join(__dirname, 'specs', 'converters.js')));

mocha.run();

