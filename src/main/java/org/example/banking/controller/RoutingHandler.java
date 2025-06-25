package org.example.banking.controller;

import io.vertx.ext.web.RoutingContext;

@FunctionalInterface
public interface RoutingHandler {
    void handle(RoutingContext context) throws Exception;
}
