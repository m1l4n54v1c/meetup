package io.axoniq.demo.meetup.command;

import org.axonframework.axonserver.connector.ErrorCode;
import org.axonframework.config.EventProcessingConfigurer;
import org.axonframework.eventhandling.EventMessage;
import org.axonframework.eventhandling.EventMessageHandler;
import org.axonframework.eventhandling.ListenerInvocationErrorHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author Milan Savic
 */
@SpringBootApplication
public class MeetupCommandApplication {

    private static final Logger log = LoggerFactory.getLogger(MeetupCommandApplication.class);

    public static void main(String[] args) {
        new SpringApplication(MeetupCommandApplication.class).run(args);
    }

    @Autowired
    public void configure(EventProcessingConfigurer eventProcessingConfigurer) {
        eventProcessingConfigurer.registerListenerInvocationErrorHandler("some-group", configuration -> new ListenerInvocationErrorHandler() {
            @Override
            public void onError(Exception e, EventMessage<?> eventMessage, EventMessageHandler eventMessageHandler)
                    throws Exception {
                if(e.getMessage().contains(ErrorCode.INVALID_EVENT_SEQUENCE.errorCode())) {
                    log.error("I got u!");
                }
            }
        });
    }
}
