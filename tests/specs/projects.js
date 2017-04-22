var request = require("request");
var chai = require('chai');
var chaiAsPromised = require('chai-as-promised');
var expect = chai.expect;
var should = chai.should();
chai.use(chaiAsPromised);

var testProjectObject = require('./../test-objects/project');
var makeRequest = require('./../helpers/request');

const SERVER_URL = "https://glacial-hollows-97055.herokuapp.com";
const PATH_TO_GET_PROJECTS_AS_HASH_MAP = "/api/project/hash_map";
const PROJECT_API_REST_ENDPOINT_PATH = "/api/project";

const basicRequestOptions = {
    headers: {
        "Secret-authorization-token": "123456"
    }
};

var newProjectId = null;

describe("Project tests", function() {

    it("should POST Project", function(done) {
        const postProjectPromise = makeRequest({
            method: 'POST',
            json: true,
            body: testProjectObject,
            url: SERVER_URL + PROJECT_API_REST_ENDPOINT_PATH
        });

        postProjectPromise.should.be.fulfilled.then(({ response, body }) => {
            const newProject = body;
            expect(newProject).to.be.an('object');
            expect(response.statusCode).to.equal(200);
            expect(newProject.id).to.be.a('string');
            newProjectId = newProject.id;
            testProjectObject.id = newProjectId;
        }).should.notify(done);
    });

    it("should GET projects", function(done) {
        const getProjectsPromise = makeRequest({
            method: 'GET',
            url: SERVER_URL + PATH_TO_GET_PROJECTS_AS_HASH_MAP,
            json: true
        });

        getProjectsPromise.should.be.fulfilled.then(({ response, body }) => {
            const projectsAsHashMap = body;
            const newProjectFromHashMap = projectsAsHashMap[testProjectObject.hashMapIdentifier];

            expect(newProjectFromHashMap).to.deep.equal(testProjectObject)
            expect(response.statusCode).to.equal(200);
            expect(body).to.be.an('object');
        }).should.notify(done);


    });

    it("should DELETE Project", function(done) {
        const deleteProjectPromise = makeRequest({
            method: 'DELETE',
            url: `${SERVER_URL}${PROJECT_API_REST_ENDPOINT_PATH}/${newProjectId}`
        });


        deleteProjectPromise.should.be.fulfilled.then(({ response}) => {
            expect(response.statusCode).to.equal(200);
        }).should.notify(done);
    });


});
