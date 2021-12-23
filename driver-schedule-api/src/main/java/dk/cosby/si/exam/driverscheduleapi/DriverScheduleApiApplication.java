package dk.cosby.si.exam.driverscheduleapi;

import dk.cosby.si.exam.driverscheduleapi.repositories.ScheduleRepository;
import dk.cosby.si.exam.driverscheduleapi.services.CreateScheduleService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

@SpringBootApplication
@EnableMongoRepositories(basePackageClasses = ScheduleRepository.class)
public class DriverScheduleApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(DriverScheduleApiApplication.class, args);
    }

}
