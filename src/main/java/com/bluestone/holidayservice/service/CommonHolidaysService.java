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
    public CommonHolidaysService(RestTemplate holidaysApiRestTemplate, HolidayApiProperties properties) {
        this.holidaysApiRestTemplate = holidaysApiRestTemplate;
        this.properties = properties;
    }

    public Optional<CommonHolidayResponse> getNextCommonHoliday(final CommonHolidayRequest request) {
        final int year = request.getDate().getYear();
        return calculateCommonHoliday(
            getHolidaysForCountry(request.getFirstCountryCode(), year),
            getHolidaysForCountry(request.getSecondCountryCode(), year),
            request.getDate()
        );
    }

    private Optional<CommonHolidayResponse> calculateCommonHoliday(
        final List<HolidaysApiResponse.Holiday> firstCountryHolidays,
        final List<HolidaysApiResponse.Holiday> secondCountryHolidays,
        final LocalDate beginDate
    ) {
        Optional<CommonHolidayResponse> result = Optional.empty();
        int iter = seekToDateAfter(beginDate, firstCountryHolidays);
        if (iter < 0) {
            return result;
        }
        int jter = 0;
        while (iter < firstCountryHolidays.size() && jter < secondCountryHolidays.size()) {
            final HolidaysApiResponse.Holiday firstHoliday = firstCountryHolidays.get(iter);
            final HolidaysApiResponse.Holiday secondHoliday = secondCountryHolidays.get(jter);
            if (firstHoliday.getDate().compareTo(secondHoliday.getDate()) > 0) {
                jter += 1;
            }
            else if (firstHoliday.getDate().compareTo(secondHoliday.getDate()) < 0) {
                iter += 1;
            } else {
                result = Optional.of(new CommonHolidayResponse(
                    LocalDate.parse(firstHoliday.getDate()), firstHoliday.getName(), secondHoliday.getName()
                ));
                break;
            }
        }
        return result;
    }

    private int seekToDateAfter(final LocalDate beginDate, final List<HolidaysApiResponse.Holiday> holidays) {
        for (int i = 0; i < holidays.size(); i += 1) {
            if (holidays.get(i).getDate().compareTo(beginDate.format(DateTimeFormatter.ISO_DATE)) > 0) {
                return i;
            }
        }
        return -1;
    }

    private List<HolidaysApiResponse.Holiday> getHolidaysForCountry(final String countryCode, final int year) {
        UriComponents uriComponents = UriComponentsBuilder.newInstance()
            .uri(URI.create(properties.getApiUrl()))
            .queryParam(HolidaysApiQueryParams.KEY.toString(), properties.getApiKey())
            .queryParam(HolidaysApiQueryParams.COUNTRY.toString(), countryCode)
            .queryParam(HolidaysApiQueryParams.YEAR.toString(), year)
            .build();
        return holidaysApiRestTemplate.getForObject(
                uriComponents.toUri(),
                HolidaysApiResponse.class
        ).getHolidays();
    }
}
