const JIRA_HOST = 'https://techfides.atlassian.net';
const method = 'GET';
const makeRequest = require('./request');

const getJiraIssue = ({ issue, basicAuthBase64Encoded }) => {
	const JIRA_URL = `${JIRA_HOST}/rest/api/2/issue/${issue}`;
	return makeRequest({
		url: JIRA_URL,
		method,
		basicAuthBase64Encoded
	})
};

module.exports = getJiraIssue;