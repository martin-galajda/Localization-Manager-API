import authorizedRequest from '../../../common_scripts/authorizedRequest';
import parseConfigurationFile from '../../../common_scripts/parseConfigurationFile';

const configuration = parseConfigurationFile();

const SERVER_URL = configuration.backendUrl;
const PROJECT_API_REST_ENDPOINT_PATH = '/api/project';

const updateProjectEstimatedPrice = ({
	id,
	price,
	currency = "CZK"
}) => {
	return authorizedRequest({
		method: 'PUT',
		json: true,
		body: {
			price,
			currency
		},
		url: SERVER_URL + PROJECT_API_REST_ENDPOINT_PATH + "/" + id + "/price"
	});
};

module.exports = updateProjectEstimatedPrice;