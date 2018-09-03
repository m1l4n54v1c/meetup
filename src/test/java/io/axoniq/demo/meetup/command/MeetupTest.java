package io.axoniq.demo.meetup.command;

import io.axoniq.demo.meetup.api.CloseMeetupCommand;
import io.axoniq.demo.meetup.api.CommentOnTopicCommand;
import io.axoniq.demo.meetup.api.CreateMeetupCommand;
import io.axoniq.demo.meetup.api.MeetupClosedEvent;
import io.axoniq.demo.meetup.api.MeetupCreatedEvent;
import io.axoniq.demo.meetup.api.TopicCommentedEvent;
import org.axonframework.eventsourcing.AggregateDeletedException;
import org.axonframework.test.aggregate.AggregateTestFixture;
import org.junit.*;

/**
 * @author Milan Savic
 */
public class MeetupTest {

    private AggregateTestFixture<Meetup> fixture;

    @Before
    public void setUp() {
        fixture = new AggregateTestFixture<>(Meetup.class);
    }

    @Test
    public void testMeetupCreation() {
        fixture.givenNoPriorActivity()
               .when(new CreateMeetupCommand("meetupId", "axonMeetup", "milan"))
               .expectSuccessfulHandlerExecution()
               .expectEvents(new MeetupCreatedEvent("meetupId", "axonMeetup", "milan"));
    }

    @Test
    public void testCommentOnTopic() {
        fixture.given(new MeetupCreatedEvent("meetupId", "axonMeetup", "milan"))
               .when(new CommentOnTopicCommand("meetupId", "awesomeMeetup", "petar"))
               .expectSuccessfulHandlerExecution()
               .expectEvents(new TopicCommentedEvent("meetupId", "awesomeMeetup", "petar"));
    }

    @Test
    public void testMeetupClosingByTheUserWhoDidntOpenIt() {
        fixture.given(new MeetupCreatedEvent("meetupId", "axonMeetup", "milan"))
               .when(new CloseMeetupCommand("meetupId", "petar"))
               .expectException(IllegalArgumentException.class);
    }

    @Test
    public void testMeetupClosingByTheUserWhoOpenedIt() {
        fixture.given(new MeetupCreatedEvent("meetupId", "axonMeetup", "milan"))
               .when(new CloseMeetupCommand("meetupId", "milan"))
               .expectSuccessfulHandlerExecution()
               .expectEvents(new MeetupClosedEvent("meetupId", "milan"));
    }

    @Test
    public void testMeetupClosed() {
        fixture.given(new MeetupCreatedEvent("meetupId", "axonMeetup", "milan"),
                      new MeetupClosedEvent("meetupId", "milan"))
               .when(new CommentOnTopicCommand("meetupId", "why is this closed", "milos"))
               .expectException(AggregateDeletedException.class);
    }
}
