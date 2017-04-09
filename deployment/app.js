require('babel-register');
require("./assets/materialize.js");

const fs = require('fs');
const path = require('path');
const os = require('os');


var saveBtn = document.getElementById('save-btn');
const writeToConfFile = ( filename, field, data ) => {
    const line = `${field} = "${data}"${os.EOL}`;
    fs.appendFileSync(path.join('../', 'conf', filename), line);
};

const clearConfigFile = (filename) => {
    fs.writeFileSync(path.join('../', 'conf', filename), '');
};

const getInputValue = (id) => {
    return $(`#${id}`).val();
};

const setInputValue = (id, value) => {
    const $inputElement = $(`#${id}`)
    $inputElement.on('change keyup keypress blur', (e) => {
        if (!e.target.value) {
            $inputElement.next().removeClass('active');
        } else {
            $inputElement.next().addClass('active');
        }
    });

    $inputElement.val(value);

    if (value.length) {
        $inputElement.next().addClass('active');
    } else {
        $inputElement.next().removeClass('active');
    }
};

const animateFadingNotification = ($notification) => {
    setTimeout(() => {
        $notification.removeClass('fade-in');
    }, 50);
    setTimeout(() => {
        $notification.addClass('fade-out');
    }, 2000);
    setTimeout(() => {
        $notification.remove();
    }, 2500);
};

$(saveBtn).on('click', function() {
    clearConfigFile('locale_manager.conf');

    const backendUrl = getInputValue('backendUrl');
    const frontendUrl = getInputValue('frontendUrl');
    const authenticationRedirectUrl = backendUrl + "/api/auth/google/handler";
    const serverUrl = backendUrl;
    const firebaseDatabaseUrl = getInputValue('firebaseDatabaseUrl');
    const pathToServiceAccount = 'lol';
    const clientId = getInputValue('googleOAuthClientId');
    const clientSecret = getInputValue('googleOAuthClientSecret');
    const administrators = getInputValue('administrators');
    const secretToken = getInputValue('secretToken');
    const firebaseCredentialsPath = getInputValue('pathToFirebaseCredentials');

    writeToConfFile('locale_manager.conf', 'authentication.redirectUrl', authenticationRedirectUrl);
    writeToConfFile('locale_manager.conf', 'backendUrl', serverUrl);
    writeToConfFile('locale_manager.conf', 'http.client.address', frontendUrl);
    writeToConfFile('locale_manager.conf', 'firebase.databaseUrl', firebaseDatabaseUrl);
    writeToConfFile('locale_manager.conf', 'firebase.pathToServiceAccount', pathToServiceAccount);
    writeToConfFile('locale_manager.conf', 'google.oauth2.clientId', clientId);
    writeToConfFile('locale_manager.conf', 'google.oauth2.clientSecret', clientSecret);
    writeToConfFile('locale_manager.conf', 'administrators', administrators);
    writeToConfFile('locale_manager.conf', 'secret.token', secretToken);
    writeToConfFile('locale_manager.conf', 'firebase.pathToServiceAccount', firebaseCredentialsPath);

    const $notification = $('<div class="notification notification-success fade-in">Successfully saved</div>').appendTo('.notification-container');
    animateFadingNotification($notification);
});

const parseConfigurationFile = (successCallback) => {
    const configurationObject = {};
    fs.readFile(path.join('../', 'conf', 'locale_manager.conf'),  (err, data) => {
        if (err) throw err;
        data = data.toString();

        const lines = data.split(os.EOL);
        lines.forEach(line => {
            const [ key,value ] = line.split('=');
            if (key && value) {
                configurationObject[key.trim()] = value.trim().replace(/"/g, '');
            }
        });

        successCallback(configurationObject);
    });
};

parseConfigurationFile((config) => {
    const backendUrl = config['backendUrl'];
    const frontendUrl = config['http.client.address'];
    const firebaseDatabaseUrl = config['firebase.databaseUrl'];
    const clientId = config['google.oauth2.clientId'];
    const clientSecret = config['google.oauth2.clientSecret'];
    const administrators = config['administrators'];
    const secretToken = config['secret.token'];
    const pathToServiceAccount = config['firebase.pathToServiceAccount'];

    setInputValue('backendUrl', backendUrl);
    setInputValue('frontendUrl', frontendUrl);
    setInputValue('firebaseDatabaseUrl', firebaseDatabaseUrl);
    setInputValue('googleOAuthClientId', clientId);
    setInputValue('googleOAuthClientSecret', clientSecret);
    setInputValue('administrators', administrators);
    setInputValue('secretToken', secretToken);
    setInputValue('pathToFirebaseCredentials', pathToServiceAccount);
});