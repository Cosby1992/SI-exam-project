package dk.cosby.si.exam.driverscheduleapi.services;

import dk.cosby.si.exam.driverscheduleapi.repositories.ScheduleRepository;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
public class CreateFromRabbitMQ {

/*    private final ScheduleRepository repository;

    public CreateFromRabbitMQ(ScheduleRepository repository) {
        this.repository = repository;
    }*/

        @RabbitListener(queues = {"schedule"})
        public void receive(@Payload String fileBody) {
            System.out.println("Message " + fileBody);
        }

}
