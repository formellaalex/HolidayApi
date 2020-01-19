package com.bluestone.holidayservice.model;

import com.bluestone.holidayservice.validation.NotEqualCountriesConstraint;
import lombok.Getter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotBlank;
import java.time.LocalDate;

@Getter
@NotEqualCountriesConstraint
public class CommonHolidayRequest {

    @NotBlank
    private final String firstCountryCode;
    @NotBlank
    private final String secondCountryCode;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private final LocalDate date;

    public CommonHolidayRequest(final String firstCountryCode, final String secondCountryCode, final LocalDate date) {
        this.firstCountryCode = firstCountryCode;
        this.secondCountryCode = secondCountryCode;
        this.date = date;
    }
}
