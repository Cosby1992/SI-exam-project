package dk.cosby.si.exam.driverscheduleapi.services;

import dk.cosby.si.exam.driverscheduleapi.FindByLessonStartAndLessonEndRequest;
import dk.cosby.si.exam.driverscheduleapi.FindByLessonStartAndLessonEndResponse;
import dk.cosby.si.exam.driverscheduleapi.FindByLessonStartAndLessonEndServiceGrpc;
import dk.cosby.si.exam.driverscheduleapi.domainobjects.Schedule;
import dk.cosby.si.exam.driverscheduleapi.repositories.ScheduleRepository;
import dk.cosby.si.exam.driverscheduleapi.utils.GrpcObjectConverter;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

@GrpcService
public class FindScheduleByLessonStartAndLessonEndService extends FindByLessonStartAndLessonEndServiceGrpc.FindByLessonStartAndLessonEndServiceImplBase {

    private final ScheduleRepository repository;

    public FindScheduleByLessonStartAndLessonEndService(ScheduleRepository repository) {
        this.repository = repository;
    }

    @Override
    public void findByLessonStartAndLessonEnd(FindByLessonStartAndLessonEndRequest request, StreamObserver<FindByLessonStartAndLessonEndResponse> responseObserver) {

        boolean acknowledged = false;
        boolean success = false;
        StringBuilder errorMessageBuilder = new StringBuilder();
        List<Schedule> schedules = new ArrayList<>();
        List<dk.cosby.si.exam.driverscheduleapi.Schedule> parsed = new LinkedList<>();

        try {
            Date start = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").parse(request.getStart());
            Date end = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").parse(request.getEnd());
            acknowledged = true;
            schedules = repository.findSchedulesByStudentLessonStartAndLessonEnd(start, end);
            parsed = GrpcObjectConverter.parseScheduleToGrpcScheduleList(schedules);
            success = true;
        } catch (ParseException e) {
            errorMessageBuilder.append("Parse exception: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            errorMessageBuilder.append("Other exception: " + e.getMessage());
            e.printStackTrace();
        }

        FindByLessonStartAndLessonEndResponse response = FindByLessonStartAndLessonEndResponse.newBuilder()
                                                                                                .setAcknowledged(acknowledged)
                                                                                                .setSuccess(success)
                                                                                                .setErrorMessage(errorMessageBuilder.toString())
                                                                                                .addAllSchedules(parsed)
                                                                                                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();

    }
}
