package io.axoniq.demo.meetup.query;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author Milan Savic
 */
@SpringBootApplication
public class MeetupQueryApplication {

    public static void main(String[] args) {
        new SpringApplication(MeetupQueryApplication.class).run(args);
    }
}
