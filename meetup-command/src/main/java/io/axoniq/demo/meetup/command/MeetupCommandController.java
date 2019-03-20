package io.axoniq.demo.meetup.command;

import io.axoniq.demo.meetup.api.CloseMeetupCommand;
import io.axoniq.demo.meetup.api.CommentOnTopicCommand;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.Future;
import javax.validation.Valid;

/**
 * @author Milan Savic
 */
@RestController
public class MeetupCommandController {

    private final CommandGateway commandGateway;

    public MeetupCommandController(CommandGateway commandGateway) {
        this.commandGateway = commandGateway;
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

    @DeleteMapping("/meetups/{meetupId}")
    public Future<Void> closeMeetup(@PathVariable String meetupId, @RequestHeader("Authorization") String user) {
        return commandGateway.send(new CloseMeetupCommand(meetupId, user));
    }
}
