open module leaflet.app.cbfs.web {
    requires leaflet.app.cbfs.core;
    requires leaflet.component.rest.failover.api;
    requires leaflet.component.tlp.appender;

    requires com.fasterxml.jackson.annotation;
    requires com.fasterxml.jackson.databind;
    requires org.slf4j;
    requires spark.core;
    requires spring.beans;
    requires spring.context;
}