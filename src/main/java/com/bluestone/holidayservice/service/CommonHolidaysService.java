package com.bluestone.holidayservice.service;

import com.bluestone.holidayservice.configuration.HolidayApiProperties;
import com.bluestone.holidayservice.model.CommonHolidayRequest;
import com.bluestone.holidayservice.model.CommonHolidayResponse;
import com.bluestone.holidayservice.model.HolidaysApiQueryParams;
import com.bluestone.holidayservice.model.HolidaysApiResponse;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import javax.inject.Inject;
import java.net.URI;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@Service
public class CommonHolidaysService {

    private RestTemplate holidaysApiRestTemplate;
    private HolidayApiProperties properties;

    @Inject
    public CommonHolidaysService(final RestTemplate holidaysApiRestTemplate, final HolidayApiProperties properties) {
        this.holidaysApiRestTemplate = holidaysApiRestTemplate;
        this.properties = properties;
    }

    public Optional<CommonHolidayResponse> getNextCommonHoliday(final CommonHolidayRequest request) {
        final int year = request.getDate().getYear();
        return calculateCommonHoliday(
            getAllHolidaysForCountry(request.getFirstCountryCode(), year),
            getAllHolidaysForCountry(request.getSecondCountryCode(), year),
            request.getDate()
        );
    }

    private Optional<CommonHolidayResponse> calculateCommonHoliday(
        final List<HolidaysApiResponse.Holiday> firstCountryHolidays,
        final List<HolidaysApiResponse.Holiday> secondCountryHolidays,
        final LocalDate beginDate
    ) {
        Optional<CommonHolidayResponse> commonHoliday = Optional.empty();
        int currentDateFirstCountryPos = seekToDateAfter(beginDate, firstCountryHolidays);
        if (currentDateFirstCountryPos < 0) {
            return commonHoliday;
        }
        int currentDateSecondCountryPos = 0;
        while (currentDateFirstCountryPos < firstCountryHolidays.size() && currentDateSecondCountryPos < secondCountryHolidays.size()) {
            final HolidaysApiResponse.Holiday firstHoliday = firstCountryHolidays.get(currentDateFirstCountryPos);
            final HolidaysApiResponse.Holiday secondHoliday = secondCountryHolidays.get(currentDateSecondCountryPos);
            if (firstHoliday.getDate().compareTo(secondHoliday.getDate()) > 0) {
                currentDateSecondCountryPos += 1;
            }
            else if (firstHoliday.getDate().compareTo(secondHoliday.getDate()) < 0) {
                currentDateFirstCountryPos += 1;
            } else {
                commonHoliday = Optional.of(new CommonHolidayResponse(
                    LocalDate.parse(firstHoliday.getDate()), firstHoliday.getName(), secondHoliday.getName()
                ));
                break;
            }
        }
        return commonHoliday;
    }

    private int seekToDateAfter(final LocalDate beginDate, final List<HolidaysApiResponse.Holiday> holidays) {
        for (int i = 0; i < holidays.size(); i += 1) {
            if (holidays.get(i).getDate().compareTo(beginDate.format(DateTimeFormatter.ISO_DATE)) > 0) {
                return i;
            }
        }
        return -1;
    }

    private List<HolidaysApiResponse.Holiday> getAllHolidaysForCountry(final String countryCode, final int year) {
        UriComponents uriComponents = UriComponentsBuilder.newInstance()
            .uri(URI.create(properties.getApiUrl()))
            .queryParam(HolidaysApiQueryParams.KEY.toString(), properties.getApiKey())
            .queryParam(HolidaysApiQueryParams.COUNTRY.toString(), countryCode)
            .queryParam(HolidaysApiQueryParams.YEAR.toString(), year)
            .queryParam(HolidaysApiQueryParams.PUBLIC.toString(), true)
            .build();
        return holidaysApiRestTemplate.getForObject(
                uriComponents.toUri(),
                HolidaysApiResponse.class
        ).getHolidays();
    }
}
