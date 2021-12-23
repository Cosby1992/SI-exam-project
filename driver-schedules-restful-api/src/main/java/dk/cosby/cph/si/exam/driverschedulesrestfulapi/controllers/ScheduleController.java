package dk.cosby.cph.si.exam.driverschedulesrestfulapi.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import dk.cosby.cph.si.exam.driverschedulesrestfulapi.domainobjects.Schedule;
import dk.cosby.cph.si.exam.driverschedulesrestfulapi.dto.CreateScheduleResponseEntity;
import dk.cosby.cph.si.exam.driverschedulesrestfulapi.dto.FindByLessonStartAndLessonEndEntity;
import dk.cosby.cph.si.exam.driverschedulesrestfulapi.utils.GrpcObjectConverter;
import dk.cosby.si.exam.driverscheduleapi.*;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.context.annotation.Bean;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.config.HypermediaMappingInformation;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/schedule")
public class ScheduleController {

    @GrpcClient("schedules")
    public CreateScheduleServiceGrpc.CreateScheduleServiceBlockingStub createScheduleStub;

    @GrpcClient("schedules")
    public FindByLessonStartAndLessonEndServiceGrpc.FindByLessonStartAndLessonEndServiceBlockingStub findByLessonStartAndLessonEndServiceStub;

    @GetMapping("/byLessonStartAndLessonEnd")
    public ResponseEntity<FindByLessonStartAndLessonEndEntity> findByLessonStartAndLessonEnd(@RequestParam("start") String start, @RequestParam("end") String end) {
        FindByLessonStartAndLessonEndRequest request = FindByLessonStartAndLessonEndRequest.newBuilder().setStart(start).setEnd(end).build();
        FindByLessonStartAndLessonEndResponse response = findByLessonStartAndLessonEndServiceStub.findByLessonStartAndLessonEnd(request);
        System.out.println(response);

        FindByLessonStartAndLessonEndEntity entity = new FindByLessonStartAndLessonEndEntity(response.getAcknowledged(), response.getSuccess(), response.getErrorMessage(), response.getSchedulesList());

        System.out.println(entity);



        entity.add(Link.of("/schedule?start=dd/MM/yyyy HH:mm:ss&end=dd/MM/yyyy HH:mm:ss"));
        if(entity.isSuccess()) {
            entity.add(Link.of("/schedule/").withRel("all").withName("All Schedules"));
            return ResponseEntity.ok(entity);
        } else if(entity.isAcknowledged()) {
            entity.add(Link.of("/schedule/").withRel("all").withName("All Schedules"));
            return ResponseEntity.internalServerError().body(entity);
        } else {
            entity.add(Link.of("/schedule?start=<url_encoded_date_in_format: dd/MM/yyyy HH:mm:ss>"));
            return ResponseEntity.badRequest().body(entity);
        }
    }

    @PostMapping("/create")
    public ResponseEntity<CreateScheduleResponseEntity> createSchedule(@RequestBody Schedule csr) {

        CreateScheduleRequest request = GrpcObjectConverter.parseScheduleToGrpcCreateScheduleRequest(csr);
        CreateScheduleResponse response = createScheduleStub.createSchedule(request);

        CreateScheduleResponseEntity entity;

        entity = new CreateScheduleResponseEntity(response.getAcknowledged(), response.getSuccess(), response.getInsertedId(), response.getErrorMessage());
        if(entity.isSuccess()) {
            entity.add(Link.of("/schedule/" + entity.getInsertedId()));
            entity.add(Link.of("/schedule/").withRel("all").withName("All Schedules"));
            entity.add(Link.of("/schedule/byTeacherId/" + request.getTeacherId()).withRel("byTeacher").withName("By teacher id"));
            return ResponseEntity.ok(entity);
        } else if(entity.isAcknowledged()) {
            return ResponseEntity.internalServerError().body(entity);
        } else {
            return ResponseEntity.badRequest().body(entity);
        }
    }


}
