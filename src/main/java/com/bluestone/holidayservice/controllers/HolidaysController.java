package com.bluestone.holidayservice.controllers;

import com.bluestone.holidayservice.model.CommonHolidayRequest;
import com.bluestone.holidayservice.model.CommonHolidayResponse;
import com.bluestone.holidayservice.service.CommonHolidaysService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
public class HolidaysController {

    private final CommonHolidaysService holidaysService;

    @GetMapping("/commonholidays")
    public ResponseEntity<CommonHolidayResponse> getNextCommonHoliday(@Valid final CommonHolidayRequest request) {
        final Optional<CommonHolidayResponse> response = holidaysService.getNextCommonHoliday(request);
        if (!response.isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No common holiday found for given countries");
        }
        return new ResponseEntity<>(response.get(), HttpStatus.OK);
    }
}
