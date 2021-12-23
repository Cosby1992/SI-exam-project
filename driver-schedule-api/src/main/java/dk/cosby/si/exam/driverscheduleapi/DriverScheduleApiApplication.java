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

    private final static String QUEUE_NAME = "schedule";

    public static void main(String[] args) throws IOException, TimeoutException {
        SpringApplication.run(DriverScheduleApiApplication.class, args);

        listenForMessagesRabbitMQ();
    }
    protected static void listenForMessagesRabbitMQ() throws IOException, TimeoutException {

        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        channel.queueDeclare(QUEUE_NAME, false, false, false, null);
        System.out.println(" [*] Waiting for messages. To exit press CTRL+C");

        DeliverCallback deliverCallback = (consumerTag, delivery) -> {

            String message = new String(delivery.getBody(), "UTF-8");
            System.out.println(" [x] Received '" + message + "'");
        };
        channel.basicConsume(QUEUE_NAME, true, deliverCallback, consumerTag -> { });
    }


}
