package dk.cosby.si.exam.driverscheduleapi.utils;

import dk.cosby.si.exam.driverscheduleapi.CreateScheduleRequest;
import dk.cosby.si.exam.driverscheduleapi.domainobjects.Lesson;
import dk.cosby.si.exam.driverscheduleapi.domainobjects.Schedule;
import dk.cosby.si.exam.driverscheduleapi.domainobjects.Student;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class GrpcObjectConverter {

    public static Schedule parseRequestToSchedule(CreateScheduleRequest request) throws ParseException {
        Schedule schedule = new Schedule();
        schedule.setTeacherId(request.getTeacherId());

        List<Student> studentList = new ArrayList<>();

        for (int i = 0; i < request.getStudentsCount(); i++) {

            Student student = new Student();
            student.setId(request.getStudents(i).getId());

            List<Lesson> lessonList = new ArrayList<>();

            for (int j = 0; j < request.getStudents(i).getLessonsCount(); j++) {

                Lesson lesson = new Lesson();
                String startDate = request.getStudents(i).getLessons(j).getStart();
                String endDate = request.getStudents(i).getLessons(j).getEnd();

                lesson.setStart(new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").parse(startDate));
                lesson.setEnd(new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").parse(endDate));
                lessonList.add(lesson);

            }

            student.setLessons(lessonList);
            studentList.add(student);
        }

        schedule.setStudents(studentList);

        return schedule;
    }

    public static List<dk.cosby.si.exam.driverscheduleapi.Schedule> parseScheduleToGrpcScheduleList(List<Schedule> schedules) {

        List<dk.cosby.si.exam.driverscheduleapi.Schedule> grpcSchedules = new LinkedList<>();

        // For each schedule, add a new schedule in the list
        for (Schedule schedule : schedules) {
            List<dk.cosby.si.exam.driverscheduleapi.Schedule.Student> students = new LinkedList<>();
            List<dk.cosby.si.exam.driverscheduleapi.Schedule.Student.Lesson> lessons;

            // For each student, add a new student to the list
            for (int j = 0; j < schedule.getStudents().size(); j++) {

                lessons = new LinkedList<>();

                // For each lesson add a new lesson to the list
                for (int k = 0; k < schedule.getStudents().get(j).getLessons().size(); k++) {

                    String start = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(schedule.getStudents().get(j).getLessons().get(k).getStart());
                    String end = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(schedule.getStudents().get(j).getLessons().get(k).getEnd());

                    lessons.add(dk.cosby.si.exam.driverscheduleapi.Schedule.Student.Lesson.newBuilder().setStart(start).setEnd(end).build());

                }

                students.add(dk.cosby.si.exam.driverscheduleapi.Schedule.Student.newBuilder().setId(schedule.getStudents().get(j).getId()).addAllLessons(lessons).build());

            }

            grpcSchedules.add(dk.cosby.si.exam.driverscheduleapi.Schedule.newBuilder().setId(schedule.getId()).setTeacherId(schedule.getTeacherId()).addAllStudents(students).build());

        }

        return grpcSchedules;

    }
}
