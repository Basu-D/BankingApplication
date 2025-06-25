package org.example.banking.util;

import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RequestBody;
import io.vertx.ext.web.RoutingContext;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class ValidationUtilTest {

    @Test
    void shouldThrowIfRequestBodyIsEmpty() {
        RoutingContext context = Mockito.mock(RoutingContext.class);
        RequestBody requestBody = Mockito.mock(RequestBody.class);

        Mockito.when(context.body()).thenReturn(requestBody);
        Mockito.when(context.body().asJsonObject()).thenReturn(null);
        assertThrows(IllegalArgumentException.class,
                () -> ValidationUtil.validateAccountCreationRequest(context));
    }

    @Test
    void shouldThrowIfNameIsBlank() {
        JsonObject body = new JsonObject().put("name", "  ").put("initialBalance", 100);
        RoutingContext context = Mockito.mock(RoutingContext.class);
        RequestBody requestBody = Mockito.mock(RequestBody.class);

        Mockito.when(context.body()).thenReturn(requestBody);
        Mockito.when(context.body().asJsonObject()).thenReturn(body);

        assertThrows(IllegalArgumentException.class,
                () -> ValidationUtil.validateAccountCreationRequest(context));
    }

    @Test
    void shouldThrowIfInitialBalanceIsNegative() {
        JsonObject body = new JsonObject().put("name", "Test").put("initialBalance", -100);
        RoutingContext context = Mockito.mock(RoutingContext.class);
        RequestBody requestBody = Mockito.mock(RequestBody.class);

        Mockito.when(context.body()).thenReturn(requestBody);
        Mockito.when(context.body().asJsonObject()).thenReturn(body);

        assertThrows(IllegalArgumentException.class,
                () -> ValidationUtil.validateAccountCreationRequest(context));
    }
}
