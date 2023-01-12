package nl.tudelft.sem.notification.microservice;

import lombok.Generated;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * Contract microservice application.
 */
@EnableScheduling
@SpringBootApplication
@EnableEurekaClient
public class NotificationApplication {
    @Generated
    public static void main(String[] args) {
        SpringApplication.run(NotificationApplication.class, args);
    }
}
