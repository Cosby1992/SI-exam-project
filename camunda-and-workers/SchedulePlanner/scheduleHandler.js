const { Client, logger } = require("camunda-external-task-client-js");
const { Variables } = require("camunda-external-task-client-js");




// configuration for the Client:
//  - 'baseUrl': url to the Process Engine
//  - 'logger': utility to automatically log important events
const config = { baseUrl: "http://localhost:8080/engine-rest", use: logger };

// create a Client instance with custom configuration
const client = new Client(config);


// susbscribe to the topic: 'schedule'
client.subscribe("schedule", async function ({ task, taskService }) {

	const participants = task.variables.get("participants");
	//console.log(participants, "original array");

	//tester her::::
	var dataToSend = dataTransformation(task);


	const reply = new Variables();

	console.log("data to send", dataToSend);
	//check income and validate
	reply.set("schedule", dataToSend);

	// complete the task
	await taskService.complete(task, reply);
});


function dataTransformation(task) {
	const participants = task.variables.get("participants");
	const participantsArray = objectifyParticipants(participants)

	betterdata = deconstructData(participantsArray);

	return createScheduleObject(betterdata);

};

function createScheduleObject(arrayOfStudentsInPeriod = []) {
	const periods = []
	var sameStudentsInPeriod = []
	for (const student of arrayOfStudentsInPeriod) {
		if (sameStudentsInPeriod.length === 0 || sameStudentsInPeriod[sameStudentsInPeriod.length - 1]?.drivingPeriod == student?.drivingPeriod) {
			sameStudentsInPeriod.push(student)
		} else {
			periods.push(sameStudentsInPeriod);
			sameStudentsInPeriod = [];
			sameStudentsInPeriod.push(student)
		}
	}
	if (sameStudentsInPeriod.length > 0) {
		periods.push(sameStudentsInPeriod);
	}

	var scheduleForAllPeriods = []
	for (const studentsInPeriod of periods) {
		var dates = studentsInPeriod[0]?.drivingPeriod.split(".");
		var start = dates[0];
		var end = dates[1];
		var date = new Date(start)
		const numOfLessons = 4;
		var allStudentsInThisPeriod = []
		

		for (const [key, value] of Object.entries(studentsInPeriod)) {
		
			var studentLessons = [];
			var lessonCounter = 0;
			while (lessonCounter < numOfLessons) {
				if (numberToDay(date.getDay()) in value?.preferedDays) {

					var stringDate = date.toLocaleDateString("en-US")
	
					studentLessons.push(assignLesson(stringDate + " 08:00:00", stringDate + " 09:00:00"))
					
					date.setDate( date.getDate(date)+1);

					lessonCounter ++;
				}else{
					date.setDate( date.getDate(date)+1);
				}
				
			}
			var studentObj = assignStudent(value.studentId,studentLessons);
			
			allStudentsInThisPeriod.push(studentObj);

		}

		var scheduleForPeriod = createSchedule("ru498ru2ru8292y498ry2",allStudentsInThisPeriod);
		scheduleForAllPeriods.push(scheduleForPeriod);
	}

	return scheduleForAllPeriods;

}

function numberToDay(number = 7) {
	var day = "";

	switch (number) {
		case 1:
			day = "monday"
			break;
		case 2:
			day = "tuesday"
			break;
		case 3:
			day = "wednesday"
			break;
		case 4:
			day = "thursday"
			break;
		case 5:
			day = "friday"
			break;
		case 6:
			day = "saturday"
			break;
		case 0:
			day = "sunday"
			break;
		default:
			day = "this is not a day"
			break;
	}

	return day;
}

function deconstructData(participantsArray) {
	const betterArray = []
	for (const element of participantsArray) {
		var betterJSON = {}
		betterJSON["drivingPeriod"] = element.start + "." + element.end;
		betterJSON["studentId"] = element.id;
		betterJSON["studentFirstname"] = element.firstName;
		betterJSON["studentLastname"] = element.lastName;

		var days = {}
		var counter = 0;
		for (const [key, value] of Object.entries(element)) {
			if (value === "1") {
				counter++;
				days[key] = key;
			}
		}
		betterJSON["preferedDays"] = days;
		betterJSON["email"] = element.email;
		betterArray.push(betterJSON);
	}
	return betterArray;
}


function objectifyParticipants(participants = []) {
	const returnObj = []
	for (const element of participants) {
		for (const obj of element) {
			returnObj.push(obj);

		}
	}
	return returnObj
}

function createSchedule(teacherId = String, students = []) {
	const schedule = {}
	schedule["teacherId"] = teacherId
	schedule["students"] = students

	return schedule
}


function assignStudent(id = String, lessons = []) {
	const student = {}
	student["id"] = id
	student["lessons"] = lessons

	return student
}



function assignLesson(start = String, end = String) {
	const lesson = {}
	lesson["start"] = start
	lesson["end"] = end

	return lesson
}
