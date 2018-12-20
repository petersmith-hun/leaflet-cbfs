open module leaflet.app.cbfs.web {
    requires leaflet.app.cbfs.core;
    requires leaflet.component.rest.failover.api;
    requires leaflet.component.tlp.appender;

    requires com.fasterxml.jackson.databind;
    requires slf4j.api;
    requires spark.core;
    requires spring.beans;
    requires spring.context;
}