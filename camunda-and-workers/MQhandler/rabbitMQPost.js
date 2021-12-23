const { Client, logger } = require("camunda-external-task-client-js");
const { Variables } = require("camunda-external-task-client-js");

const amqp = require('amqplib/callback_api');




// configuration for the Client:
//  - 'baseUrl': url to the Process Engine
//  - 'logger': utility to automatically log important events
const config = { baseUrl: "http://localhost:8080/engine-rest", use: logger };

// create a Client instance with custom configuration
const client = new Client(config);

//connect to rabbitMQ on standart configuration


// susbscribe to the topic: 'message'
client.subscribe("message", async function ({ task, taskService }) {

	const schedule = task.variables.get("schedule");

	if (schedule.length > 1) {

		for (const obj of schedule) {


			amqp.connect('amqp://localhost', function (error0, connection) {

				if (error0) {
					throw error0;
				}
				connection.createChannel(function (error1, channel) {

					if (error1) {
						throw error1;
					}

					var queue = "schedule";
					var msg = Buffer.from(JSON.stringify(obj));

					channel.assertQueue(queue, {
						durable: false
					});

					channel.sendToQueue(queue, Buffer.from(msg));
					console.log(" [x] Sent %s", msg);

					setTimeout(function () {
						connection.close();
					}, 500);

				});

			});
		}

	}

	const reply = new Variables();

	//check income and validate
	reply.set("validationVar", true);

	// complete the task
	await taskService.complete(task, reply);
});