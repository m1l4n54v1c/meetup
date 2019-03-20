package io.axoniq.demo.meetup.query;

import java.time.Instant;
import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * @author Milan Savic
 */
@Entity
public class MeetupComment {

    @Id
    private String id;
    private String meetupId;
    private String user;
    private String comment;
    private Instant timestamp;

    public MeetupComment() {
    }

    public MeetupComment(String id, String meetupId, String user, String comment, Instant timestamp) {
        this.id = id;
        this.meetupId = meetupId;
        this.user = user;
        this.comment = comment;
        this.timestamp = timestamp;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMeetupId() {
        return meetupId;
    }

    public void setMeetupId(String meetupId) {
        this.meetupId = meetupId;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Instant timestamp) {
        this.timestamp = timestamp;
    }
}
