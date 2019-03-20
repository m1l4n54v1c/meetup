package io.axoniq.demo.meetup.query;

import io.axoniq.demo.meetup.api.MeetupCommentsQuery;
import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.queryhandling.QueryGateway;
import org.axonframework.queryhandling.SubscriptionQueryResult;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.util.List;
import java.util.Objects;

/**
 * @author Milan Savic
 */
@RestController
public class MeetupQueryController {

    private final QueryGateway queryGateway;

    public MeetupQueryController(QueryGateway queryGateway) {
        this.queryGateway = queryGateway;
    }

    @GetMapping("/meetups/{meetupId}/comments")
    public Flux<ServerSentEvent<MeetupComment>> comments(@PathVariable String meetupId) {
        return Flux.<MeetupComment>create(emitter -> {
            SubscriptionQueryResult<List<MeetupComment>, MeetupComment> queryResult = queryGateway
                    .subscriptionQuery(new MeetupCommentsQuery(meetupId),
                                       ResponseTypes.multipleInstancesOf(MeetupComment.class),
                                       ResponseTypes.instanceOf(MeetupComment.class));
            queryResult.initialResult()
                       .subscribe(comments -> comments.forEach(emitter::next));
            queryResult.updates()
                       .doOnComplete(emitter::complete)
                       .subscribe(emitter::next);
        }).map(this::buildSSE);
    }

    private ServerSentEvent<MeetupComment> buildSSE(MeetupComment message) {
        return ServerSentEvent.<MeetupComment>builder()
                .event("comment")
                .id(Objects.requireNonNull(message.getId()))
                .data(message)
                .build();
    }
}
