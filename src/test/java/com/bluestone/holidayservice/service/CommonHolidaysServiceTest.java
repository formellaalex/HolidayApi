package com.bluestone.holidayservice.service;

import com.bluestone.holidayservice.configuration.HolidayApiProperties;
import com.bluestone.holidayservice.model.CommonHolidayRequest;
import com.bluestone.holidayservice.model.CommonHolidayResponse;
import com.bluestone.holidayservice.model.HolidaysApiQueryParams;
import com.bluestone.holidayservice.model.HolidaysApiResponse;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.util.Assert;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

class CommonHolidaysServiceTest {

    private static final String API_URL = "http://www.holiday-api.com";
    private static final String API_KEY = "key";
    private static final String FIRST_COUNTRY_CODE = "PL";
    private static final String SECOND_COUNTRY_CODE = "DE";

    @Test
    public void getNextCommonHolidayCommon() {
        final LocalDate commonDate = LocalDate.parse("2019-03-22");
        final CommonHolidayRequest request = new CommonHolidayRequest(
                FIRST_COUNTRY_CODE, SECOND_COUNTRY_CODE, LocalDate.parse("2019-01-01")
        );
        final List<LocalDate> firstCountryHolidays = Lists.newArrayList(
                LocalDate.parse("2019-01-19"), LocalDate.parse("2019-02-02"), commonDate
        );
        final List<LocalDate> secondCountryHolidays = Lists.newArrayList(
                LocalDate.parse("2019-01-14"), LocalDate.parse("2019-02-01"),
                LocalDate.parse("2019-02-22"), commonDate
        );
        final RestTemplate restTemplate = Mockito.mock(RestTemplate.class);
        mockRestTemplateForResponse(restTemplate, FIRST_COUNTRY_CODE, firstCountryHolidays);
        mockRestTemplateForResponse(restTemplate, SECOND_COUNTRY_CODE, secondCountryHolidays);
        final HolidayApiProperties properties = new HolidayApiProperties();
        properties.setApiKey(API_KEY);
        properties.setApiUrl(API_URL);
        final CommonHolidayResponse result = new CommonHolidaysService(restTemplate, properties)
                .getNextCommonHoliday(request).get();
        Assert.isTrue(result.getDate().equals(commonDate), "Found date is not common for both countries.");
    }

    @Test
    public void commonHolidayNotFound() {
        final CommonHolidayRequest request = new CommonHolidayRequest(
                FIRST_COUNTRY_CODE, SECOND_COUNTRY_CODE, LocalDate.parse("2019-01-01")
        );
        final List<LocalDate> firstCountryHolidays = Lists.newArrayList(LocalDate.parse("2019-01-19"));
        final List<LocalDate> secondCountryHolidays = Lists.newArrayList(
                LocalDate.parse("2019-01-14"), LocalDate.parse("2019-02-01"), LocalDate.parse("2019-02-22")
        );
        final RestTemplate restTemplate = Mockito.mock(RestTemplate.class);
        mockRestTemplateForResponse(restTemplate, FIRST_COUNTRY_CODE, firstCountryHolidays);
        mockRestTemplateForResponse(restTemplate, SECOND_COUNTRY_CODE, secondCountryHolidays);
        final HolidayApiProperties properties = new HolidayApiProperties();
        properties.setApiKey(API_KEY);
        properties.setApiUrl(API_URL);
        final Optional<CommonHolidayResponse> result = new CommonHolidaysService(restTemplate, properties)
                .getNextCommonHoliday(request);
        Assert.isTrue(!result.isPresent(), "The should be no common date for both countries.");
    }

    private void mockRestTemplateForResponse(
            final RestTemplate restTemplate, final String countryCode, final List<LocalDate> dates
    ) {
        final HolidaysApiResponse holidaysApiResponse = new HolidaysApiResponse();
        holidaysApiResponse.setHolidays(
                dates.stream().map(date -> createHoliday(countryCode, date)).collect(Collectors.toList())
        );
        Mockito.when(
            restTemplate.getForObject(
                Mockito.eq(
                    UriComponentsBuilder.newInstance()
                            .uri(URI.create(API_URL))
                            .queryParam(HolidaysApiQueryParams.KEY.toString(), API_KEY)
                            .queryParam(HolidaysApiQueryParams.COUNTRY.toString(), countryCode)
                            .queryParam(HolidaysApiQueryParams.YEAR.toString(), dates.get(0).getYear())
                            .queryParam(HolidaysApiQueryParams.PUBLIC.toString(), true)
                            .build().toUri()
                ), Mockito.eq(HolidaysApiResponse.class)
            )
        ).thenReturn(holidaysApiResponse);
    }

    private HolidaysApiResponse.Holiday createHoliday(final String countryCode, final LocalDate date) {
        final HolidaysApiResponse.Holiday holiday = new HolidaysApiResponse.Holiday();
        holiday.setCountry(countryCode);
        holiday.setDate(date.format(DateTimeFormatter.ISO_DATE));
        holiday.setName("Some day name");
        return holiday;
    }
}