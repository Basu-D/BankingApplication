package org.example.banking.verticles;

import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.client.WebClient;
import io.vertx.junit5.VertxExtension;
import io.vertx.junit5.VertxTestContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.*;

@Tag("integration")
@ExtendWith(VertxExtension.class)
public class MainVerticleTest {

    private WebClient client;

    @BeforeEach
    void setup(Vertx vertx, VertxTestContext testContext) {
        vertx.deployVerticle(new MainVerticle(), testContext.succeeding(id -> testContext.completeNow()));
        client = WebClient.create(vertx);
    }

    private void createTestAccount(WebClient client, VertxTestContext ctx, Handler<AsyncResult<String>> handler) {
        JsonObject createRequest = new JsonObject()
                .put("name", "TestUser")
                .put("initialBalance", 500);

        client.post(8888, "localhost", "/api/account/create")
                .sendJsonObject(createRequest, ar -> {
                    if (ar.succeeded()) {
                        String accountId = ar.result().bodyAsJsonObject().getString("accountId");
                        handler.handle(Future.succeededFuture(accountId));
                    } else {
                        handler.handle(Future.failedFuture(ar.cause()));
                    }
                });
    }

    @DisplayName("Should create new account and return accountId")
    @Test
    void testCreateAccount(VertxTestContext ctx) {
        JsonObject request = new JsonObject()
                .put("name", "TestUser")
                .put("initialBalance", 1000);

        client.post(8888, "localhost", "/api/account/create")
                .sendJsonObject(request, ar -> {
                    if (ar.succeeded()) {
                        var response = ar.result();
                        assertEquals(201, response.statusCode());
                        assertNotNull(response.bodyAsJsonObject().getString("accountId"));
                        ctx.completeNow();
                    } else {
                        ctx.failNow(ar.cause());
                    }
                });
    }

    @DisplayName("Should read correct balance")
    @Test
    void testReadBalanceAfterCreate(VertxTestContext ctx) {
        createTestAccount(client, ctx, ar -> {
            if (ar.failed()) {
                ctx.failNow(ar.cause());
                return;
            }

            String accountId = ar.result();

            client.get(8888, "localhost", "/api/account/" + accountId + "/balance")
                    .send(balanceResp -> {
                        if (balanceResp.failed()) {
                            ctx.failNow(balanceResp.cause());
                            return;
                        }

                        JsonObject json = balanceResp.result().bodyAsJsonObject();
                        assertEquals(500.0, json.getDouble("balance"));
                        ctx.completeNow();
                    });
        });
    }

    @DisplayName("Should deposit the correct amount and return updated balance")
    @Test
    void testDepositAmount(Vertx vertx, VertxTestContext ctx) {
        createTestAccount(client, ctx, ar -> {
            if (ar.failed()) {
                ctx.failNow(ar.cause());
                return;
            }

            String accountId = ar.result();
            JsonObject deposit = new JsonObject().put("amount", 200);

            client.post(8888, "localhost", "/api/account/" + accountId + "/deposit")
                    .sendJsonObject(deposit, depositResp -> {
                        if (depositResp.failed()) {
                            ctx.failNow(depositResp.cause());
                            return;
                        }

                        JsonObject data = depositResp.result().bodyAsJsonObject();
                        assertEquals(700.0, data.getDouble("balance"));
                        ctx.completeNow();
                    });
        });
    }

    @DisplayName("Should withdraw the correct amount and return updated balance")
    @Test
    void testWithdrawAmount(Vertx vertx, VertxTestContext ctx) {
        createTestAccount(client, ctx, ar -> {
            if (ar.failed()) {
                ctx.failNow(ar.cause());
                return;
            }

            String accountId = ar.result();
            JsonObject withdraw = new JsonObject().put("amount", 300);

            client.post(8888, "localhost", "/api/account/" + accountId + "/withdraw")
                    .sendJsonObject(withdraw, withdrawResp -> {
                        if (withdrawResp.failed()) {
                            ctx.failNow(withdrawResp.cause());
                            return;
                        }

                        JsonObject data = withdrawResp.result().bodyAsJsonObject();
                        assertEquals(200.0, data.getDouble("balance"));
                        ctx.completeNow();
                    });
        });
    }

    @Test
    void testReadBalanceInvalidAccountId(Vertx vertx, VertxTestContext ctx) {
        String invalidId = "nonexistent-id-123";

        client.get(8888, "localhost", "/api/account/" + invalidId + "/balance")
                .send(resp -> {
                    if (resp.failed()) {
                        ctx.failNow(resp.cause());
                        return;
                    }

                    assertEquals(404, resp.result().statusCode());
                    JsonObject error = resp.result().bodyAsJsonObject();
                    assertEquals("Account Not Found for accountId: " + invalidId, error.getString("error"));
                    ctx.completeNow();
                });
    }

}
