package dk.cosby.cph.si.exam.driverschedulesrestfulapi.domainobjects;

import org.springframework.hateoas.RepresentationModel;

import java.util.ArrayList;
import java.util.List;

public class Schedule extends RepresentationModel<Schedule> {

    private String id;
    private String teacherId;
    private List<Student> students = new ArrayList<>();

    //no arg
    public Schedule() {

    }

    public Schedule(String id, String teacherId, List<Student> students) {
        this.id = id;
        this.teacherId = teacherId;
        this.students = students;
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

    public List<Student> getStudents() {
        return students;
    }

    public void setStudents(List<Student> students) {
        this.students = students;
    }

    @Override
    public String toString() {
        String listInit = students != null ?  "[array]" : "null";
        return "ScheduleObject: {\n id: " + id + ",\n" +
                "teacherId: " + teacherId + ",\n" +
                "students: " + listInit + "\n}";
    }
}
