package dk.cosby.si.exam.driverscheduleapi.repositories;


import dk.cosby.si.exam.driverscheduleapi.domainobjects.Schedule;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface ScheduleRepository extends MongoRepository<Schedule, String>  {

    List<Schedule> findSchedulesByTeacherId(String teacherId);

    //, fields = "{ 'questions.questionID' : 1 }"
    @Query(value = "{ 'students.lessons.start' :{$gte : ?0}, 'students.lessons.end' :{ $lte : ?1}}")
    List<Schedule> findSchedulesByStudentLessonStartAndLessonEnd(Date start, Date end);
}
