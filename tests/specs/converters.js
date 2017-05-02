const chai = require('chai');
const chaiAsPromised = require('chai-as-promised');
const expect = chai.expect;
const should = chai.should();
chai.use(chaiAsPromised);

const testConverterObject = require('./../test-objects/converter');
const makeRequest = require('../../common_scripts/authorizedRequest');

const CONVERTER_API_REST_ENDPOINT_PATH = "/api/converter";
const parseConfigurationFile = require('../../common_scripts/parseConfigurationFile');
const configuration = parseConfigurationFile();
const SERVER_URL = configuration.backendUrl;

let newConverterId = null;

describe("Converter tests", function() {

    it("should POST Converter", function(done) {
        const postProjectPromise = makeRequest({
            method: 'POST',
            json: true,
            body: testConverterObject,
            url: SERVER_URL + CONVERTER_API_REST_ENDPOINT_PATH
        });

        postProjectPromise.should.be.fulfilled.then(({ response, body }) => {
            const newConverter = body;
            expect(newConverter).to.be.an('object');
            expect(response.statusCode).to.equal(200);
            expect(newConverter.id).to.be.a('string');
            newConverterId = newConverter.id;
            testConverterObject.id = newConverterId;
        }).should.notify(done);
    });

    it("should GET converters", function(done) {
        const getConverterPromise = makeRequest({
            method: 'GET',
            json: true,
            url: SERVER_URL + CONVERTER_API_REST_ENDPOINT_PATH
        });

        getConverterPromise.should.be.fulfilled.then(({ response, body }) => {
            const converters = body;
            const newConverter = converters.find(converter => converter.id === newConverterId);

            expect(response.statusCode).to.equal(200);
            expect(converters).to.be.an('array');
            expect(newConverter).to.deep.equal(testConverterObject);
        }).should.notify(done);

    });

    it("should DELETE converter", function(done) {
        const deleteConverterPromise = makeRequest({
            method: 'DELETE',
            json: true,
            url: SERVER_URL + CONVERTER_API_REST_ENDPOINT_PATH + "/" + newConverterId
        });

        deleteConverterPromise.should.be.fulfilled.then(({ response }) => {
            expect(response.statusCode).to.equal(200);
        }).should.notify(done);
    });
});
