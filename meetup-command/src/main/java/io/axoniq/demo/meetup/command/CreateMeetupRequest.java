package io.axoniq.demo.meetup.command;

import com.fasterxml.jackson.annotation.JsonCreator;
import io.axoniq.demo.meetup.api.CreateMeetupCommand;

/**
 * @author Milan Savic
 */
public class CreateMeetupRequest {

    private final String meetupId;
    private final String topic;

    @JsonCreator
    public CreateMeetupRequest(String meetupId, String topic) {
        this.meetupId = meetupId;
        this.topic = topic;
    }

   CreateMeetupCommand asCommand(String user) {
        return new CreateMeetupCommand(meetupId, topic, user);
    }
}
