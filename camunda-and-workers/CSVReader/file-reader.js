const { Client, logger } = require("camunda-external-task-client-js");
const { Variables } = require("camunda-external-task-client-js");

const fs = require('fs');
const csvConv = require('csvtojson');
const documentTool = require('lodash');




// configuration for the Client:
//  - 'baseUrl': url to the Process Engine
//  - 'logger': utility to automatically log important events
const config = { baseUrl: "http://localhost:8080/engine-rest", use: logger };

// create a Client instance with custom configuration
const client = new Client(config);


// susbscribe to the topic: 'returnData'
client.subscribe("returnData", async function ({ task, taskService }) {

	const reply = new Variables();

	//check income and validate
	reply.set("validationVar", true);

	// complete the task
	await taskService.complete(task, reply);
});

// susbscribe to the topic: 'validate'
client.subscribe("validate", async function ({ task, taskService }) {

	const testRay = await readAllFiles();

	validationVar = validateData(testRay,true);	

	//variables to send back
	const reply = new Variables();

	//check income and validate
	reply.set("validationVar", true);
	reply.set("participants", testRay);

	// complete the task
	await taskService.complete(task, reply);
});

async function readAllFiles() {

	try {
		const returnObj = await readFiles(fs.readdirSync('..\\pickup-directory\\'));
		moveFiles(fs.readdirSync('..\\pickup-directory\\'));
		return returnObj
	} catch (err) {
		console.log(err);
	}


}


async function moveFiles(files = []) {
	for (const file of files) {
		fs.renameSync('..\\pickup-directory\\' + file, '..\\backup-directory\\' + file)
	}
};


async function readFiles(files = []) {

	var returnObj = [];

	for (const file of files) {
		const jsonObj = await csvConv({ delimiter: ";" }).fromFile('..\\pickup-directory\\' + file);
		jsonObj["fileName"] = '..\\pickup-directory\\' + file;
		returnObj.push(jsonObj);
	}


	return returnObj;

};

function validateData(studentArray = [], validationVar = true) {

	for (const object of studentArray) {

		const keys = Object.keys(object[0]);
		for (let i = 0; i < keys.length; i++) {
			const key = keys[i];			
			if(object[0][key].length<1){
				validationVar = false;
			}
		}
		return validationVar;
	}





}

async function testFunc() {
	const testRay = await readAllFiles();
	//console.log("prep ", testRay);

	validateData(testRay);

	console.log("raytesting",testRay);

}

//testFunc();
