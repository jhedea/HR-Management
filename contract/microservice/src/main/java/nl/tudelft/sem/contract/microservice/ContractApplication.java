package nl.tudelft.sem.contract.microservice;

import lombok.Generated;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * Contract microservice application.
 */
@SpringBootApplication
@EnableScheduling
@EnableEurekaClient
public class ContractApplication {
    @Generated
    public static void main(String[] args) {
        SpringApplication.run(ContractApplication.class, args);
    }
}
