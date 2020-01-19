package com.bluestone.holidayservice.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@AllArgsConstructor
public class CommonHolidayResponse {

    private final LocalDate date;
    private final String firstCountryHolidayName;
    private final String secondCountryHolidayName;
}
