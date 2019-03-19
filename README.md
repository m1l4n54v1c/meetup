# meetup
Demo application for Java Skop conference 30.03.2019.

Business rules for Meetup application:

Command model:
* Any user can create a meetup with given topic
* Any user can comment on a meetup
* Only the host (user who created a meetup) can close the meetup

Query model:
* Comments for given meetup should be displayed. If there are new comments, the endpoint should push them using Server Sent Events mechanism.

## Running the app

First, you need to build the application using maven:

```java
mvn clean verify jib:dockerBuild
```

Application uses [AxonServer](https://axoniq.io/product-overview/axon-server) to store events, distribute commands and queries. 
We packaged our application in a Docker Container. Using Docker Compose we will run the application together with AxonServer:

```java
docker-compose up -d
```
