const fs = require('fs');
const path = require('path');
const os = require('os');
const parseConfigurationFile = require('../../../common_scripts/parseConfigurationFile');
const authorizedRequest = require('../../../common_scripts/authorizedRequest');

const configuration = parseConfigurationFile();
const API_ROUTE_TO_PROJECTS = `${configuration.backendUrl}/api/project/list`;

const getAllProjects = () => {
	return authorizedRequest({
		url: API_ROUTE_TO_PROJECTS,
		method: 'GET'
	});
};

module.exports = getAllProjects;