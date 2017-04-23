const fs = require('fs');
const path = require('path');
const os = require('os');

const parseConfigurationFile = () => {
	const relativePath = path.join(__dirname, '../', 'conf', 'locale_manager.conf');
	const pathToConfigFile = path.resolve(relativePath);

	const configFileContent = fs.readFileSync(pathToConfigFile).toString();
	const configurationObject = {};

	const lines = configFileContent.split(os.EOL);
	lines.forEach(line => {
		const [ key,value ] = line.split('=').map(val => val.trim());
		if (key && value) {
			configurationObject[key] = value.replace(/"/g, '');
		}
	});

	return configurationObject;
};

module.exports = parseConfigurationFile;
