package io.axoniq.demo.meetup.command;

import io.axoniq.demo.meetup.api.CloseMeetupCommand;
import io.axoniq.demo.meetup.api.CommentOnTopicCommand;
import io.axoniq.demo.meetup.api.CreateMeetupCommand;
import io.axoniq.demo.meetup.api.MeetupClosedEvent;
import io.axoniq.demo.meetup.api.MeetupCreatedEvent;
import io.axoniq.demo.meetup.api.TopicCommentedEvent;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.commandhandling.model.AggregateIdentifier;
import org.axonframework.commandhandling.model.CommandHandlerInterceptor;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.spring.stereotype.Aggregate;

import static java.lang.String.format;
import static org.axonframework.commandhandling.model.AggregateLifecycle.apply;
import static org.axonframework.commandhandling.model.AggregateLifecycle.markDeleted;

/**
 * @author Milan Savic
 */
@SuppressWarnings("unused")
@Aggregate
public class Meetup {

    @AggregateIdentifier
    private String meetupId;
    private String host;

    public Meetup() {
        // used by Axon
    }

    @CommandHandler
    public Meetup(CreateMeetupCommand command) {
        apply(new MeetupCreatedEvent(command.getMeetupId(),
                                     command.getTopic(),
                                     command.getUser()));
    }

    @EventSourcingHandler
    public void on(MeetupCreatedEvent event) {
        this.meetupId = event.getMeetupId();
        this.host = event.getUser();
    }

    @CommandHandler
    public void handle(CommentOnTopicCommand command) {
        apply(new TopicCommentedEvent(command.getMeetupId(),
                                      command.getComment(),
                                      command.getUser()));
    }

    @CommandHandler
    public void handle(CloseMeetupCommand command) {
        if (!host.equals(command.getUser())) {
            throw new IllegalArgumentException("Only host can close the meetup.");
        }
        apply(new MeetupClosedEvent(command.getMeetupId(), command.getUser()));
    }

    @EventSourcingHandler
    public void on(MeetupClosedEvent event) {
        markDeleted();
    }
}
