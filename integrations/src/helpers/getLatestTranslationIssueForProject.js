const method = 'GET';
const makeRequest = require('./makeRequest');
var Promise = require('es6-promise').Promise;
const parseConfigurationFile = require('../../../common_scripts/parseConfigurationFile');

const configuration = parseConfigurationFile();
const JIRA_HOST = configuration.jiraHostUrl;
const jiraTranslationIssueType = configuration.jiraTranslationIssueType;
console.log(`JIRA host URL is: ${JIRA_HOST}`);
console.log(`JIRA issuetype for translations is: ${jiraTranslationIssueType}`);

const getLatestTranslationIssuesForProject = ({ project, basicAuthBase64Encoded, limit = 1 }) => {
	const query = `project= ${project} AND issuetype = ${jiraTranslationIssueType} ORDER BY created DESC`;
	const queryString = encodeURIComponent(query);
	const JIRA_URL = `${JIRA_HOST}/rest/api/2/search?jql=${queryString}&maxResults=${limit}&fields=status`;

	return makeRequest({
		url: JIRA_URL,
		method,
		basicAuthBase64Encoded
	})
};

const getLatestTranslationIssueForProject = ({ project, basicAuthBase64Encoded }) => {

	return new Promise((resolve, reject) => {
		getLatestTranslationIssuesForProject({project, basicAuthBase64Encoded})
			.then(({ response, body }) => {
				if (!body || !body.issues || !body.issues.length) {
					throw new Error(`
						Error getting latest translation JIRA issues for project: ${project}
						Response body did not contain issues array with at least one issue.
					`);
				}

				if (body.issues.length !== 1) {
					console.log(`Warning: EXPECTED TO GET ISSUES array with length 1 and got ${body.issues.length}`);
				}

				resolve({response, body: body.issues[0]});
			})
			.catch(reason => {
				reject(reason);
			});
	})


};

module.exports = getLatestTranslationIssueForProject;