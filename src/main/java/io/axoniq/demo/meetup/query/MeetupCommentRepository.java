package io.axoniq.demo.meetup.query;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author Milan Savic
 */
public interface MeetupCommentRepository extends JpaRepository<MeetupComment, String> {

    List<MeetupComment> findMessagesByMeetupId(String meetupId);
}
