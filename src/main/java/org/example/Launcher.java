package org.example;

import io.vertx.core.Vertx;
import org.example.banking.MainVerticle;

public class Launcher {
    public static void main(String[] args) {
        Vertx vertx = Vertx.vertx();
        vertx.deployVerticle(new MainVerticle());
    }
}
