package dk.cosby.cph.si.exam.driverschedulesrestfulapi.dto;

import com.fasterxml.jackson.databind.ObjectMapper;
import dk.cosby.si.exam.driverscheduleapi.FindByLessonStartAndLessonEndResponse;
import dk.cosby.si.exam.driverscheduleapi.Schedule;
import org.springframework.context.annotation.Bean;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.config.HypermediaMappingInformation;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class FindByLessonStartAndLessonEndEntity extends RepresentationModel<FindByLessonStartAndLessonEndEntity> {

    private boolean acknowledged;
    private boolean success;
    private String errorMessage;
    private ArrayList<Schedule> schedules;

    public FindByLessonStartAndLessonEndEntity() {
    }

    public FindByLessonStartAndLessonEndEntity(boolean acknowledged, boolean success, String errorMessage, List<dk.cosby.si.exam.driverscheduleapi.Schedule> schedules) {
        this.acknowledged = acknowledged;
        this.success = success;
        this.errorMessage = errorMessage;
        this.schedules = new ArrayList<>();

        for (int i = 0; i < schedules.size(); i++) {
            Schedule schedule =  new Schedule();
            schedule.setId(schedules.get(i).getId());
            schedule.setTeacherId(schedules.get(i).getTeacherId());

            for (int j = 0; j < schedules.get(i).getStudentsList().size(); j++) {
                Student student = new Student();
                student.setId(schedules.get(i).getStudentsList().get(j).getId());

                for (int k = 0; k < schedules.get(i).getStudentsList().get(j).getLessonsList().size(); k++) {
                    Lesson lesson = new Lesson();
                    lesson.setStart(schedules.get(i).getStudentsList().get(j).getLessonsList().get(k).getStart());
                    lesson.setEnd(schedules.get(i).getStudentsList().get(j).getLessonsList().get(k).getEnd());

                    student.getLessons().add(lesson);
                }

                student.add(Link.of("/student/" + student.getId()));
                student.add(Link.of("/student/").withRel("all").withName("All Students"));

                schedule.getStudents().add(student);
            }

            schedule.add(Link.of("/schedule/" + schedule.getId()));
            schedule.add(Link.of("/schedule/").withRel("all").withName("All Schedules"));

            this.schedules.add(schedule);
        }
    }

    public boolean isAcknowledged() {
        return acknowledged;
    }

    public void setAcknowledged(boolean acknowledged) {
        this.acknowledged = acknowledged;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public ArrayList<Schedule> getSchedules() {
        return schedules;
    }

    public void setSchedules(ArrayList<Schedule> schedules) {
        this.schedules = schedules;
    }

    class Lesson extends RepresentationModel<Lesson> {
        private String start;
        private String end;

        public Lesson() {
        }

        public String getStart() {
            return start;
        }

        public void setStart(String start) {
            this.start = start;
        }

        public String getEnd() {
            return end;
        }

        public void setEnd(String end) {
            this.end = end;
        }
    }

    class Student extends RepresentationModel<Student> {
        private String id;
        private ArrayList<Lesson> lessons = new ArrayList<>();

        public Student() {
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public ArrayList<Lesson> getLessons() {
            return lessons;
        }

        public void setLessons(ArrayList<Lesson> lessons) {
            this.lessons = lessons;
        }


    }

    class Schedule extends RepresentationModel<Schedule> {
        private String id;
        private String teacherId;
        private ArrayList<Student> students = new ArrayList<>();

        public Schedule() {
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getTeacherId() {
            return teacherId;
        }

        public void setTeacherId(String teacherId) {
            this.teacherId = teacherId;
        }

        public ArrayList<Student> getStudents() {
            return students;
        }

        public void setStudents(ArrayList<Student> students) {
            this.students = students;
        }
    }
}
