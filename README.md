# Common holidays service

## Task content

```
Holiday Information Service
Using any framework you are comfortable with in Java create a web service
with holiday information.
Use any available API of your choice to retrieve information about holidays.
This service should accept two country codes and a date in a format YYYY-
MM-DD and return next holiday after the given date that will happen on the
same day in both countries.
The response should be a JSON with date and names of the holidays in both
countries. Example response:
{
date: “2016-03-27”,
name1: ”Niedziela Wielkanocna”,
name2: “Paske”
}
Only support country codes that are available in used API.
If you have to make any assumptions about the behavior of the service, please
describe them.
The API key should be provided in your service and easy to configure. This
code should be written in a maintainable way and as close to production ready
code as possible.
Provide all the source code, build script and instructions how to run your code.
This task should not take you more than 4-5 hours to complete.
```
TL;DR: This service allows for finding holidays that happens the same day in two provided countries.

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
