const parseConfigurationFile = require('../../../common_scripts/parseConfigurationFile');
const makeRequest = require('./makeRequest');

const configuration = parseConfigurationFile();
const JIRA_HOST = configuration.jiraHostUrl;
const method = 'GET';

const getJiraIssue = ({ issue, basicAuthBase64Encoded }) => {
	const JIRA_URL = `${JIRA_HOST}/rest/api/2/issue/${issue}`;
	return makeRequest({
		url: JIRA_URL,
		method,
		basicAuthBase64Encoded
	})
};

module.exports = getJiraIssue;