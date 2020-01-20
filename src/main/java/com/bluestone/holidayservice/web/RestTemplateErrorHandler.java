package com.bluestone.holidayservice.web;

import com.bluestone.holidayservice.model.HolidaysApiError;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.io.IOUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.ResponseErrorHandler;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import static org.springframework.http.HttpStatus.Series.CLIENT_ERROR;

public class RestTemplateErrorHandler implements ResponseErrorHandler {

    @Override
    public boolean hasError(ClientHttpResponse httpResponse) throws IOException {
        final HttpStatus.Series series = httpResponse.getStatusCode().series();
        return series == CLIENT_ERROR;
    }

    @Override
    public void handleError(ClientHttpResponse httpResponse)
            throws IOException {
        final HolidaysApiError apiError = new ObjectMapper().readValue(
                IOUtils.toString(httpResponse.getBody(), StandardCharsets.UTF_8), HolidaysApiError.class
        );
        throw new ResponseStatusException(httpResponse.getStatusCode(), apiError.getError());
    }
}