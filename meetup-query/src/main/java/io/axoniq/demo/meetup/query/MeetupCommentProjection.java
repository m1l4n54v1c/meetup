package io.axoniq.demo.meetup.query;

import io.axoniq.demo.meetup.api.MeetupClosedEvent;
import io.axoniq.demo.meetup.api.MeetupCommentsQuery;
import io.axoniq.demo.meetup.api.TopicCommentedEvent;
import io.axoniq.demo.meetup.query.MeetupComment;
import io.axoniq.demo.meetup.query.MeetupCommentRepository;
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
        meetupCommentRepository.saveAndFlush(meetupComment);
        queryUpdateEmitter.emit(MeetupCommentsQuery.class,
                                query -> query.matches(event.getMeetupId()),
                                meetupComment);
    }

    @EventHandler
    public void on(MeetupClosedEvent event) {
        queryUpdateEmitter.complete(MeetupCommentsQuery.class, query -> query.matches(event.getMeetupId()));
    }

    @QueryHandler
    public List<MeetupComment> comments(MeetupCommentsQuery meetupCommentsQuery) {
        return meetupCommentRepository.findCommentsByMeetupId(meetupCommentsQuery.getMeetupId());
    }
}
