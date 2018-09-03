package io.axoniq.demo.meetup.web

import io.axoniq.demo.meetup.api.CreateMeetupCommand

data class CreateMeetupRequest(val meetupId: String, val topic: String) {
    fun asCommand(user: String): CreateMeetupCommand {
        return CreateMeetupCommand(meetupId, topic, user)
    }
}