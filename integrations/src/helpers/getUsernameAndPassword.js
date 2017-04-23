const readline = require('readline');
const cla = require('command-line-arguments');

const questionCreator = readline.createInterface({
	input: process.stdin,
	output: process.stdout
});

const asPromise = (callback) => {
	return new Promise((resolve, reject) => {
		callback(resolve, reject);
	});
};


let username = '';
let password = '';


const getUserNameAndPassword = (resolve, reject) => {
	const commandLineArguments = cla.getCommandLineArguments();

	if (commandLineArguments.username && commandLineArguments.password) {
		resolve({
			username: commandLineArguments.username,
			password: commandLineArguments.password,
		})
	} else {

		questionCreator.question('JIRA username: ', (usernameAnswer) => {
			username = usernameAnswer;

			const stdin = process.openStdin();
			process.stdin.on('data', (char) => {
				char = String(char);
				switch (char) {
					case '\n':
					case '\r':
					case '\u0004':
						stdin.pause();
						break;
					default:
						process.stdout.write("\x1B[2K\x1B[200DJIRA password: " + Array(questionCreator.line.length + 1).join("*"));
						break;
				}
			});

			questionCreator.question('JIRA password: ', (passwordAnswer) => {
				questionCreator.history = questionCreator.history.slice(1);

				password = passwordAnswer;
				questionCreator.close();
				resolve({username, password});
			})
		});
	}
};

module.exports = asPromise(getUserNameAndPassword);