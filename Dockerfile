FROM openjdk:11-jre-slim

ARG APP_USER=leaflet
ARG APP_HOME=/opt/cbfs
ARG APP_EXECUTABLE=leaflet-cbfs.jar

RUN addgroup --system --gid 1000 $APP_USER
RUN adduser --system --no-create-home --gid 1000 --uid 1000 $APP_USER
RUN mkdir -p $APP_HOME
ADD web/target/$APP_EXECUTABLE $APP_HOME

WORKDIR $APP_HOME
RUN chmod 744 $APP_HOME
RUN chmod 644 $APP_EXECUTABLE
RUN chown $APP_USER:$APP_USER $APP_HOME
RUN chown $APP_USER:$APP_USER $APP_EXECUTABLE

USER $APP_USER

ENTRYPOINT java ${JVM_ARGS} -jar $APP_EXECUTABLE
