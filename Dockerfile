FROM java:openjdk-8u91-jdk
MAINTAINER milan.savic@axoniq.io
ADD target/meetup*.jar app.jar
ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/./urandom","-jar","app.jar"]
