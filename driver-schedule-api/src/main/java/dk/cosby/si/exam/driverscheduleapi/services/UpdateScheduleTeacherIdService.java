package dk.cosby.si.exam.driverscheduleapi.services;

import dk.cosby.si.exam.driverscheduleapi.UpdateScheduleTeacherIdRequest;
import dk.cosby.si.exam.driverscheduleapi.UpdateScheduleTeacherIdResponse;
import dk.cosby.si.exam.driverscheduleapi.UpdateScheduleTeacherIdServiceGrpc;
import dk.cosby.si.exam.driverscheduleapi.domainobjects.Schedule;
import dk.cosby.si.exam.driverscheduleapi.repositories.ScheduleRepository;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;

import java.util.Optional;

@GrpcService
public class UpdateScheduleTeacherIdService extends UpdateScheduleTeacherIdServiceGrpc.UpdateScheduleTeacherIdServiceImplBase {

    private final ScheduleRepository repository;

    public UpdateScheduleTeacherIdService(ScheduleRepository repository) {
        this.repository = repository;
    }

    @Override
    public void updateScheduleTeacherId(UpdateScheduleTeacherIdRequest request, StreamObserver<UpdateScheduleTeacherIdResponse> responseObserver) {

        boolean acknowledged = false;
        boolean success = false;
        StringBuilder errorMessage = new StringBuilder();

        try {
            Optional<Schedule> optionalSchedule = repository.findById(request.getScheduleId());

            acknowledged = true;

            if(optionalSchedule.isPresent()) {
                Schedule schedule = optionalSchedule.get();

                schedule.setTeacherId(request.getTeacherId());

                repository.save(schedule);
                success = true;
            } else {
                errorMessage.append("The schedule with id: ");
                errorMessage.append(request.getScheduleId());
                errorMessage.append(" was not found in the database");
            }


        } catch (Exception e) {
            errorMessage.append("Not able to process the request due to a database connection error.");
            System.out.println(e.getMessage());
        }

        UpdateScheduleTeacherIdResponse response = UpdateScheduleTeacherIdResponse.newBuilder()
                                                                                    .setAcknowledged(acknowledged)
                                                                                    .setSuccess(success)
                                                                                    .setErrorMessage(errorMessage.toString())
                                                                                    .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();

    }
}
