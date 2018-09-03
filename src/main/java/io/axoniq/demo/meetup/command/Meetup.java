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

    public Meetup() {
        // used by Axon
    }

    @CommandHandler
    public Meetup(CreateMeetupCommand command) {
    }

    @EventSourcingHandler
    public void on(MeetupCreatedEvent event) {
    }

    @CommandHandler
    public void handle(CommentOnTopicCommand command) {
    }

    @CommandHandler
    public void handle(CloseMeetupCommand command) {
    }

    @EventSourcingHandler
    public void on(MeetupClosedEvent event) {
    }
}
