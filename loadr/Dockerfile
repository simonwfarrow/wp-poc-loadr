FROM java:8-jdk-alpine
COPY ./target/* /srv/
WORKDIR /srv
ENTRYPOINT ["java","-jar","/srv/loadr-1.0.0.jar","$TOPIC", "$CONCURRENCY" ,"$PAYLOAD", "$NUMEVENTS"]