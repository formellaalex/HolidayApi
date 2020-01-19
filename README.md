# Common holidays service

This service allows for finding holidays that happens the same day in two provided countries.

## Prerequisites

Make sure you have JDK 1.8+ and Maven 3+ installed.

## Running the service

1. Go to main project folder. 
2. Build service with command `mvn clean install`
3. Run service with command `mvn spring-boot:run`

The service will run on localhost, under 8090 port.

## Using the service

The service gives us one endpoint, which returns common holiday between two provided countries.
If the common day doesn't exists - it returns proper error message with HTTP 404 status.

## Assumptions

I decided to search for common holidays until the end of the year of provided date.
That's because it's more clear for end user and some countries have movable feasts.

### Endpoints:

#### GET /commonholidays

Query parameters:
 - firstCountryCode - the code of first country 
 - secondCountryCode - the code of second country
 - date - the date after the common holiday should be found
 
Example: 
Calling the url `http://localhost:8090/commonholidays?firstCountryCode=PL&secondCountryCode=SE&date=2019-04-19`

will return:

```JSON
{
    "date": "2019-04-20",
    "firstCountryHolidayName": "Holy Saturday",
    "secondCountryHolidayName": "Holy Saturday"
}
```
where `date` is the common date, `firstCountryHolidayName` is the holiday name in first country 
and `secondCountryHolidayName` is the holiday name in second country.

## API information and limitations

I decided to use Holiday API (https://holidayapi.com/docs) because:
- It provides free developer account
- It gives enough error handling
- It's rather popular and still maintained

The limits are:
- 5000 requests per month
- We can search for holidays only for previous year (2019 in this case)

Other API's had poor error handling (Calendarific) or had no free account. 
