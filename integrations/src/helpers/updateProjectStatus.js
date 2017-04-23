import authorizedRequest from '../../../common_scripts/authorizedRequest';
import parseConfigurationFile from '../../../common_scripts/parseConfigurationFile';

const configuration = parseConfigurationFile();

const SERVER_URL = configuration.backendUrl;
const PROJECT_API_REST_ENDPOINT_PATH = '/api/project';

const updateProjectStatus = ({
	id,
	wordCount,
	status
}) => {
	return authorizedRequest({
		method: 'POST',
		json: true,
		body: {
			word_count: wordCount,
			value: status
		},
		url: SERVER_URL + PROJECT_API_REST_ENDPOINT_PATH + "/" + id + "/status"
	});
};

module.exports = updateProjectStatus;