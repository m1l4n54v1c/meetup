# meetup
Demo application for Belgrade Java Community Meetup 06.09.2018.

Business rules for Meetup application:

Command model:
* Any user can create a meetup with given topic
* Any user can comment on a meetup
* Only the host (user who created a meetup) can close the meetup

Query model:
* Comments for given meetup should be displayed. If there are new comments, the endpoint should push them using Server Sent Events mechanism.