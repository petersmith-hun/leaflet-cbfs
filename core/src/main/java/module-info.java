open module leaflet.app.cbfs.core {
    requires leaflet.component.bridge.api;
    requires leaflet.component.bridge.implementation;
    requires leaflet.component.rest.backend.api;
    requires leaflet.component.rest.backend.client;
    requires leaflet.component.rest.failover.api;

    requires java.sql;
    requires java.ws.rs;
    requires com.fasterxml.jackson.core;
    requires com.fasterxml.jackson.databind;
    requires com.fasterxml.jackson.jaxrs.json;
    requires h2;
    requires metrics.annotation;
    requires metrics.core;
    requires metrics.graphite;
    requires metrics.spring;
    requires org.apache.commons.lang3;
    requires slf4j.api;
    requires spring.beans;
    requires spring.context;
    requires spring.core;
    requires spring.jdbc;
    requires spring.tx;

    exports hu.psprog.leaflet.cbfs.config;
    exports hu.psprog.leaflet.cbfs.domain;
    exports hu.psprog.leaflet.cbfs.service;
    exports hu.psprog.leaflet.cbfs.service.snapshot.impl;
}