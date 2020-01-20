package com.bluestone.holidayservice.web;

import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.mock.http.client.MockClientHttpResponse;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;

public class RestTemplateErrorHandlerTest {

    @Test
    public void shouldCheckIfIsClientError() throws IOException {
        Assert.assertTrue(
                new RestTemplateErrorHandler().hasError(new MockClientHttpResponse(new byte[]{}, HttpStatus.BAD_REQUEST))
        );
        Assert.assertFalse(
                new RestTemplateErrorHandler().hasError(new MockClientHttpResponse(new byte[]{}, HttpStatus.INTERNAL_SERVER_ERROR))
        );
    }

    @Test
    public void handleError() throws IOException {
        final HttpStatus status = HttpStatus.BAD_REQUEST;
        final String errorMessage = "Entered data was invalid.";
        final String jsonMessage = "{\"code\": " + status.value() + ", \"error\": \"" + errorMessage + "\"}";
        try {
            new RestTemplateErrorHandler()
                    .handleError(new MockClientHttpResponse(jsonMessage.getBytes(), status));
        } catch (ResponseStatusException ex) {
            Assert.assertEquals(status, ex.getStatus());
            Assert.assertEquals(errorMessage, ex.getReason());
        }
    }
}