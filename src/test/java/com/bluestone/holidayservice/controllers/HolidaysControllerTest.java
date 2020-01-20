package com.bluestone.holidayservice.controllers;

import com.bluestone.holidayservice.model.CommonHolidayRequest;
import com.bluestone.holidayservice.model.CommonHolidayResponse;
import com.bluestone.holidayservice.service.CommonHolidaysService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.Optional;

@RunWith(MockitoJUnitRunner.class)
public class HolidaysControllerTest {

    @Mock
    public CommonHolidaysService service;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void shouldReturn200WithCommonHoliday() {
        final CommonHolidaysService service = Mockito.mock(CommonHolidaysService.class);
        final String newYearHoliday = "New year";
        final CommonHolidayRequest request = new CommonHolidayRequest("PL", "SE", LocalDate.now());
        Mockito
            .when(service.getNextCommonHoliday(request))
            .thenReturn(Optional.of(new CommonHolidayResponse(LocalDate.now(), newYearHoliday, newYearHoliday)));
        final HolidaysController controller = new HolidaysController(service);
        final ResponseEntity<CommonHolidayResponse> response = controller.getNextCommonHoliday(request);
        Assert.assertEquals(response.getStatusCode(), HttpStatus.OK);
        Assert.assertNotNull(response.getBody());
        Assert.assertEquals(response.getBody().getFirstCountryHolidayName(), newYearHoliday);
    }

    @Test
    public void shouldReturn404WhenNoCommonHolidayFound() {
        final CommonHolidayRequest request = new CommonHolidayRequest("PL", "SE", LocalDate.now());
        Mockito
                .when(service.getNextCommonHoliday(request))
                .thenReturn(Optional.empty());
        final HolidaysController controller = new HolidaysController(service);
        try {
            controller.getNextCommonHoliday(request);
        } catch (final ResponseStatusException statusEx) {
            Assert.assertEquals(statusEx.getStatus(), HttpStatus.NOT_FOUND);
        }
    }
}