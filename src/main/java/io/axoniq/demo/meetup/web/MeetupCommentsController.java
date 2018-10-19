package io.axoniq.demo.meetup.web;

import io.axoniq.demo.meetup.api.CloseMeetupCommand;
import io.axoniq.demo.meetup.api.CommentOnTopicCommand;
import io.axoniq.demo.meetup.api.MeetupCommentsQuery;
import io.axoniq.demo.meetup.query.MeetupComment;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.queryhandling.QueryGateway;
import org.axonframework.queryhandling.SubscriptionQueryResult;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.Future;
import javax.validation.Valid;

/**
 * @author Milan Savic
 */
@RestController
public class MeetupCommentsController {

    private final CommandGateway commandGateway;
    private final QueryGateway queryGateway;

    public MeetupCommentsController(CommandGateway commandGateway, QueryGateway queryGateway) {
        this.commandGateway = commandGateway;
        this.queryGateway = queryGateway;
    }

    @PostMapping("/meetups")
    public Future<String> createMeetup(@RequestBody @Valid CreateMeetupRequest request,
                                       @RequestHeader("Authorization") String user) {
        return commandGateway.send(request.asCommand(user));
    }

    @PostMapping("/meetups/{meetupId}/comments")
    public Future<Void> postComment(@PathVariable String meetupId,
                                    @RequestBody String comment,
                                    @RequestHeader("Authorization") String user) {
        return commandGateway.send(new CommentOnTopicCommand(meetupId, comment, user));
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

    @DeleteMapping("/meetups/{meetupId}")
    public Future<Void> closeMeetup(@PathVariable String meetupId, @RequestHeader("Authorization") String user) {
        return commandGateway.send(new CloseMeetupCommand(meetupId, user));
    }

    private ServerSentEvent<MeetupComment> buildSSE(MeetupComment message) {
        return ServerSentEvent.<MeetupComment>builder()
                .event("comment")
                .id(Objects.requireNonNull(message.getId()))
                .data(message)
                .build();
    }
}
