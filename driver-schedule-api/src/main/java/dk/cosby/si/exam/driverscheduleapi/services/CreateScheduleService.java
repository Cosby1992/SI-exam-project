package dk.cosby.si.exam.driverscheduleapi.services;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;
import dk.cosby.si.exam.driverscheduleapi.CreateScheduleRequest;
import dk.cosby.si.exam.driverscheduleapi.CreateScheduleResponse;
import dk.cosby.si.exam.driverscheduleapi.CreateScheduleServiceGrpc;
import dk.cosby.si.exam.driverscheduleapi.domainobjects.Schedule;
import dk.cosby.si.exam.driverscheduleapi.repositories.ScheduleRepository;
import dk.cosby.si.exam.driverscheduleapi.utils.GrpcObjectConverter;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;

import java.io.IOException;
import java.text.ParseException;
import java.util.concurrent.TimeoutException;

@GrpcService
public class CreateScheduleService extends CreateScheduleServiceGrpc.CreateScheduleServiceImplBase {

    private final ScheduleRepository repository;

    public CreateScheduleService(ScheduleRepository repository) {
        this.repository = repository;
    }

    @Override
    public void createSchedule(CreateScheduleRequest request, StreamObserver<CreateScheduleResponse> responseObserver) {

        boolean acknowledged = false;
        boolean success = false;
        StringBuilder errorMessageBuilder = new StringBuilder();
        StringBuilder id = new StringBuilder();

        try {
            Schedule schedule = GrpcObjectConverter.parseRequestToSchedule(request);
            acknowledged = true;
            Schedule inserted = repository.insert(schedule);
            id.append(inserted.getId());
            success = true;
        } catch (ParseException e) {
            errorMessageBuilder.append(e.getMessage());
            System.out.println(e.getMessage());
        }

        CreateScheduleResponse response = CreateScheduleResponse.newBuilder()
                                                                .setAcknowledged(acknowledged)
                                                                .setSuccess(success)
                                                                .setErrorMessage(errorMessageBuilder.toString())
                                                                .setInsertedId(id.toString())
                                                                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();

    }
}
