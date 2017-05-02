/* more information about parsing command line arguments https://www.npmjs.com/package/command-line-arguments */
const cla = require('command-line-arguments');
var getAllProjects = require('./helpers/getAllProjects');
var updateProjectEstimatedPrice = require('./helpers/updateProjectEstimatedPrice');
let allProjects;

const getObjectKeys = (object) => {
	const keys = [];

	for (const objectKey in object) {
		if (object.hasOwnProperty(objectKey)) {
			keys.push(objectKey);
		}
	}

	return keys;
};

const updatePriceEstimation = (projectKeyToProject) => {
	const commandLineArguments = cla.getCommandLineArguments();

	const projectKeys = getObjectKeys(commandLineArguments);

	projectKeys.forEach((projectKey) => {
		const projectInformation = commandLineArguments[projectKey];
		const project = projectKeyToProject[projectKey];

		if (!projectInformation.price) {
			console.log('Wrong format of command line arguments!Price is mandatory for every project.');
			return;
		}

		const projectPrice = projectInformation.price;
		const projectCurrency = projectInformation.currency || "CZK";

		updateProjectEstimatedPrice({
			id: project.id,
			price: projectPrice,
			currency: projectCurrency
		});
	});
};

const buildHashMapFromProjectKeyToProject = (projects) => {
	const hashMap = {};
	projects.forEach(({projectKey}, projectIndex) => {
		hashMap[projectKey] = projects[projectIndex];
	});

	return hashMap;
};

getAllProjects().then(({ body }) => {
	allProjects = body;
	const projectKeyToProject = buildHashMapFromProjectKeyToProject(allProjects);
	updatePriceEstimation(projectKeyToProject);
});