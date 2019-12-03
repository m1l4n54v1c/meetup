package io.axoniq.demo.meetup.command;

import io.axoniq.demo.meetup.api.CreateMeetupCommand;
import io.axoniq.demo.meetup.api.SomethingHappenedEvent;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.config.ProcessingGroup;
import org.axonframework.eventhandling.EventHandler;
import org.springframework.stereotype.Component;

/**
 * @author Milan Savic
 */
@Component
@ProcessingGroup("some-group")
public class SomeEventHandler {

    private final CommandGateway commandGateway;

    public SomeEventHandler(CommandGateway commandGateway) {
        this.commandGateway = commandGateway;
    }

    @EventHandler
    public void somethingHappened(SomethingHappenedEvent evt) {
        commandGateway.sendAndWait(new CreateMeetupCommand(evt.getMeetupId(), evt.getTopic(), evt.getUser()));
    }
}
