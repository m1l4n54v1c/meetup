package io.axoniq.demo.meetup.command;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author Milan Savic
 */
@SpringBootApplication
public class MeetupCommandApplication {

    public static void main(String[] args) {
        new SpringApplication(MeetupCommandApplication.class).run(args);
    }
}
