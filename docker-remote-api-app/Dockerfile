FROM openjdk:14-ea-12-jdk-alpine3.10
LABEL MAINTAINER="Maksim Kostromin <daggerok@gmail.com>"
HEALTHCHECK CMD wget --quiet --tries=1 --spider http://127.0.0.1:8080/ || exit 1
EXPOSE 8080
CMD ["/bin/ash"]
ENTRYPOINT ["ash", "/tmp/docker-remote-api-app/bin/docker-remote-api-app"]
WORKDIR /tmp/docker-remote-api-app
COPY ./build/install/docker-remote-api-app /tmp/docker-remote-api-app
