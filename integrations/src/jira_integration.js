require("babel-polyfill");
require("es6-shim");
const base64url = require('base64url');
const fs = require('fs');
const os = require('os');
var request = require("request");


var getJiraIssue = require('./helpers/getJiraIssue');
var getAllProjects = require('./helpers/getAllProjects');
var getLatestTranslationIssueForProject = require('./helpers/getLatestTranslationIssueForProject');
var updateProjectStatus = require('./helpers/updateProjectStatus');
var getUsernameAndPassword = require('./helpers/getUsernameAndPassword');



getUsernameAndPassword.then(({ username, password }) => {
	const basicAuthBase64Encoded = base64url(`${username}:${password}`);

	getAllProjects().then(({ body }) => {
		const projects = body;
		projects.forEach(({ projectKey, id}) => {
			handleUpdateProject({ projectKey, id, basicAuthBase64Encoded });
		});
	});
});


const handleUpdateProject = ({ projectKey, id, basicAuthBase64Encoded}) => {
	getLatestTranslationIssueForProject({
		basicAuthBase64Encoded,
		project: projectKey
	}).then(({ response, body}) => {
		if (!body || !body.fields || !body.fields.status || response.statusCode !== 200) {
			console.error(`
				Error getting JIRA issue with status for project with key: ${projectKey}
				Response status: ${response.statusCode}
			`);

			return ;
		}
		const status = body.fields.status.name;

		updateProjectStatus({
			id,
			status,
			wordCount: 0
		}).then(() => {
			console.log(`Status for project with id: ${id} and key ${projectKey} was successfully updated.`);
		}).catch((reason) => {
			console.log(`Updating status for project with id: ${id} and key ${projectKey} was NOT SUCCESSFUL.`);
			console.log(`Reason:.` + reason.error ? reason.error.toString() : '');
			console.log(`HTTP status:.` + reason.response.statusCode.toString());
		});

	}).catch((reason) => {
		console.error(`Error getting latest translation issue for project: ${projectKey}`);
		console.error(`Status code: ${reason.response.statusCode}`);
		const errorMessages = reason.response.body.errorMessages.map(message => message.toString()).join(os.EOL);
		console.error(`ErrorMessages: ${errorMessages}`);
	})
};


