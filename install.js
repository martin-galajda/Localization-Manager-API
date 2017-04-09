#!/usr/bin/env node

require('babel-register');
const readline = require('readline');
const fs = require('fs');
const path = require('path');
const os = require('os');

const createQuestionAsker = () => {
    return readline.createInterface({
        input: process.stdin,
        output: process.stdout
    });
};

const questionAsker = createQuestionAsker();

const writeToConfFile = ( filename, field, data ) => {
    const line = `${field} = "${data}"${os.EOL}`;
    fs.appendFileSync(path.join('./conf_test', filename), line);
};

const clearConfigFile = (filename) => {
    fs.writeFileSync(path.join('./conf_test', filename), '');
};

const asPromise = (callback) => {
    return new Promise((resolve, reject) => {
        callback(resolve, reject);
    });
};

const handleBackendConfiguration = () => {
    clearConfigFile('authentication.conf');
    clearConfigFile('http.conf');

    const askQuestion = (done) => {
        questionAsker.question('What is the URL address of the server? ' + os.EOL, (serverUrl) => {
            serverUrl = serverUrl.trim();
            if (!serverUrl.length) {
                throw new Error("The URL address of the server cannot be empty!");
            }

            if (serverUrl[serverUrl.length - 1] === '/') {
                serverUrl = serverUrl.substring(0, serverUrl.length -1);
            }
            var authenticationRedirectUrl = serverUrl;
            authenticationRedirectUrl += '/api/auth/google/handler';

            writeToConfFile('authentication.conf', 'authentication.redirectUrl', authenticationRedirectUrl);
            writeToConfFile('http.conf', 'backendUrl', serverUrl);

            done();
        });
    };

    return asPromise(askQuestion);
};

const handleFrontEndConfiguration = () => {

    const askQuestion = (done) => {
        questionAsker.question('What is the URL address of your frontend server?' + os.EOL, (serverUrl) => {
            serverUrl = serverUrl.trim();
            if (!serverUrl.length) {
                throw new Error("The URL address of the server cannot be empty!");
            }

            if (serverUrl[serverUrl.length - 1] === '/') {
                serverUrl = serverUrl.substring(0, serverUrl.length -1);
            }

            writeToConfFile('http.conf', 'http.client.address', serverUrl);
            done();
        });
    };

    return asPromise(askQuestion);
};

const handleFireBase = () => {
    clearConfigFile('firebase.conf');

    const askQuestion = (done) => {
        questionAsker.question('What is the URL address of the Firebase database(e.g. https://localemanagementprototype.firebaseio.com)?' + os.EOL, (firebaseDatabaseUrl) => {
            firebaseDatabaseUrl = firebaseDatabaseUrl.trim();
            if (!firebaseDatabaseUrl.length) {
                throw new Error("The URL address of the firebase database cannot be empty!");
            }

            if (firebaseDatabaseUrl[firebaseDatabaseUrl.length - 1] === '/') {
                firebaseDatabaseUrl = firebaseDatabaseUrl.substring(0, firebaseDatabaseUrl.length -1);
            }
            const pathToServiceAccount = "./conf/ManagementPrototype.json";

            writeToConfFile('firebase.conf', 'firebase.databaseUrl', firebaseDatabaseUrl);
            writeToConfFile('firebase.conf', 'firebase.pathToServiceAccount', pathToServiceAccount);

            done();
        });
    };

    return asPromise(askQuestion);
};

const handleOAuthConfiguration = () => {
    clearConfigFile('google.oauth2.conf');
    const googleClientQuestion = (done) => {
        questionAsker.question('What is the client ID of your service account for OAuth2 credentials?' + os.EOL, (clientId) => {
            writeToConfFile('google.oauth2.conf', 'google.oauth2.clientId', clientId);
            questionAsker.question('What is the client secret of your service account for OAuth2 credentials?' + os.EOL, (clientSecret) => {
                writeToConfFile('google.oauth2.conf', 'google.oauth2.clientSecret', clientSecret);

                done();
            });
        });
    };

    return asPromise(googleClientQuestion);
};

const handleSecretToken = () => {
    const secretTokenQuestion = (done) => {
        questionAsker.question('Please choose secret token which will be used for authentication and authorization in 3rd party applications:' + os.EOL, (secretToken) => {
            writeToConfFile('authentication.conf', 'secret.token', secretToken);

            done();
        });
    };

    return asPromise(secretTokenQuestion);
};

const handleAdministrators = () => {
    questionAsker.question('Please enter emails(delimited by comma) for administrators. They will be responsible for confirming users:' + os.EOL, (administrators) => {
        writeToConfFile('google.oauth2.conf', 'administrators', administrators);

        questionAsker.close();
    });
};


handleBackendConfiguration()
    .then(handleFrontEndConfiguration)
    .then(handleFireBase)
    .then(handleOAuthConfiguration)
    .then(handleSecretToken)
    .then(handleAdministrators);



