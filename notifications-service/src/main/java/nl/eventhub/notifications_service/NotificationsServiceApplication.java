package nl.eventhub.notifications_service;

import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableRabbit
public class NotificationsServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(NotificationsServiceApplication.class, args);
	}

}
