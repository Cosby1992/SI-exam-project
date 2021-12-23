package dk.cosby.si.exam.driverscheduleapi.domainobjects;

import com.google.protobuf.Timestamp;
import org.springframework.data.annotation.Id;

import java.util.Date;

public class Lesson {

    private Date start;
    private Date end;

    public Lesson(){

    }

    public Lesson(Date start, Date end) {
        this.start = start;
        this.end = end;
    }

    public Date getStart() {
        return start;
    }

    public void setStart(Date start) {
        this.start = start;
    }

    public Date getEnd() {
        return end;
    }

    public void setEnd(Date end) {
        this.end = end;
    }
}
