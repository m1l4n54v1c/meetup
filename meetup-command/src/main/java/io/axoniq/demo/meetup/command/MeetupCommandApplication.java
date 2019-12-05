package io.axoniq.demo.meetup.command;

import org.axonframework.axonserver.connector.ErrorCode;
import org.axonframework.commandhandling.CommandBus;
import org.axonframework.commandhandling.SimpleCommandBus;
import org.axonframework.config.EventProcessingConfigurer;
import org.axonframework.eventhandling.ErrorContext;
import org.axonframework.eventhandling.ErrorHandler;
import org.axonframework.eventhandling.EventMessage;
import org.axonframework.eventhandling.EventMessageHandler;
import org.axonframework.eventhandling.ListenerInvocationErrorHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

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
        InvalidSequenceErrorHandler iseh = new InvalidSequenceErrorHandler();
        eventProcessingConfigurer.registerListenerInvocationErrorHandler("some-group", configuration -> iseh);
        eventProcessingConfigurer.registerErrorHandler("some-group", configuration -> iseh);
    }

    @Bean
    public CommandBus commandBus() {
        return SimpleCommandBus.builder().build();
    }

    private static class InvalidSequenceErrorHandler implements ListenerInvocationErrorHandler, ErrorHandler {

        @Override
        public void handleError(ErrorContext errorContext) throws Exception {
            handle(errorContext.error());
        }

        @Override
        public void onError(Exception exception, EventMessage<?> event, EventMessageHandler eventHandler)
                throws Exception {
            handle(exception);
        }

        private void handle(Throwable e) {
            if (e.getMessage().contains(ErrorCode.INVALID_EVENT_SEQUENCE.errorCode())) {
                log.error("I got u!");
            }
        }
    }
}
