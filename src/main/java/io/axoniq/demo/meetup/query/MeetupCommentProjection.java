package io.axoniq.demo.meetup.query;

import io.axoniq.demo.meetup.api.MeetupClosedEvent;
import io.axoniq.demo.meetup.api.MeetupCommentsQuery;
import io.axoniq.demo.meetup.api.TopicCommentedEvent;
import org.axonframework.eventhandling.EventHandler;
import org.axonframework.eventhandling.Timestamp;
import org.axonframework.queryhandling.QueryHandler;
import org.axonframework.queryhandling.QueryUpdateEmitter;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

/**
 * @author Milan Savic
 */
@SuppressWarnings("unused")
@Component
public class MeetupCommentProjection {

    private final MeetupCommentRepository meetupCommentRepository;
    private final QueryUpdateEmitter queryUpdateEmitter;

    public MeetupCommentProjection(MeetupCommentRepository meetupCommentRepository,
                                   QueryUpdateEmitter queryUpdateEmitter) {
        this.meetupCommentRepository = meetupCommentRepository;
        this.queryUpdateEmitter = queryUpdateEmitter;
    }

    @EventHandler
    public void on(TopicCommentedEvent event, @Timestamp Instant timestamp) {
        MeetupComment meetupComment = new MeetupComment(UUID.randomUUID().toString(),
                                                        event.getMeetupId(),
                                                        event.getUser(),
                                                        event.getComment(),
                                                        timestamp);
    }

    @EventHandler
    public void on(MeetupClosedEvent event) {
    }

    @QueryHandler
    public List<MeetupComment> comments(MeetupCommentsQuery meetupCommentsQuery) {
        return null;
    }
}
