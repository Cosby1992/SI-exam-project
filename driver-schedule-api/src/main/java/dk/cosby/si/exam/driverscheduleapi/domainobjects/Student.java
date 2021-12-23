package dk.cosby.si.exam.driverscheduleapi.domainobjects;

import java.util.List;

public class Student {

    private String id;
    private List<Lesson> lessons;

    public Student() {
    }

    public Student(String id, List<Lesson> lessons) {
        this.id = id;
        this.lessons = lessons;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<Lesson> getLessons() {
        return lessons;
    }

    public void setLessons(List<Lesson> lessons) {
        this.lessons = lessons;
    }
}
