package io.axoniq.demo.meetup.api

import org.axonframework.commandhandling.TargetAggregateIdentifier

data class CreateMeetupCommand(val meetupId: String, val topic: String, val user: String)
data class CommentOnTopicCommand(@TargetAggregateIdentifier val meetupId: String, val comment: String, val user: String)
data class CloseMeetupCommand(@TargetAggregateIdentifier val meetupId: String, val user: String)

data class MeetupCreatedEvent(val meetupId: String, val topic: String, val user: String)
data class TopicCommentedEvent(val meetupId: String, val comment: String, val user: String)
data class MeetupClosedEvent(val meetupId: String, val user: String)

data class MeetupCommentsQuery(val meetupId: String) {
    fun matches(meetupId: String): Boolean {
        return this.meetupId == meetupId
    }
}