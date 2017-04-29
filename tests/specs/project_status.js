var request = require("request");
var deepCopy = require('deep-copy');

var chai = require('chai');
var chaiAsPromised = require('chai-as-promised');
var expect = chai.expect;
var should = chai.should();
chai.use(chaiAsPromised);

var testProjectObject = deepCopy(require('./../test-objects/project'));
var makeRequest = require('../../common_scripts/authorizedRequest');

const SERVER_URL = "https://glacial-hollows-97055.herokuapp.com";
const PROJECT_API_REST_ENDPOINT_PATH = "/api/project";


var newProjectId;

describe("Project status update tests", function() {
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

    it("should PUT Project status", function(done) {
        const postProjectPromise = makeRequest({
            method: 'PUT',
            json: true,
            body: {
                word_count: 10,
                value: "COMPLETED"
            },
            url: SERVER_URL + PROJECT_API_REST_ENDPOINT_PATH + "/" + newProjectId + "/status"
        });

        postProjectPromise.should.be.fulfilled.then(({ response, body }) => {
            console.log(body);
            expect(response.statusCode).to.equal(200);
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
