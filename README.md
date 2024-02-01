# booking-service

## Description

This a simple booking service that allows you to book or block properties. 

It is a RESTful Java 17 application with Spring Boot and H2 database using JPA.

Its developed using layered architecture, with an app, service, repository layers. Divided into modules.


## Specifics

For overlapping considerations, the application allow an start date in the same day of an end date.
Considering for example check-in and check-out hours with proper distances to use the same day.


## Executing

You can use docker-compose provided in this repo to execute the application. Just run the following command:

```bash
docker-compose up
```

This will serve the application on `http://localhost:8888`.

If you need to build the application again for some reason, you must use the `--build` option:

```bash
docker-compose up --build
```

To execute the tests you can use

```bash
docker-compose run --entrypoint "mvn test" app  
```


## Endpoints

The application has the following endpoints:

### Property Endpoints

```http
GET /properties
GET /properties/{id}
POST /properties
DELETE /properties/{id}
PUT /properties/{id}
```

Body example:

```json
{
    "address": "123 Main St",
    "city": "Springfield",
    "ownerName": "Sly Stone"
}
```

### Booking Endpoints

```http
GET /properties/{id}/bookings
GET /properties/{id}/bookings/{id}
POST /properties/{id}/bookings
DELETE /properties/{id}/bookings/{id}
PUT /properties/{id}/bookings/{id}
```

Body example:

```json
{
  "startDate": "2022-12-10",
  "endDate": "2022-12-11",
  "guestName": "Stevie Wonder",
  "numberOfGuests": "2",
  "bookingState": "ACTIVE"
}
```
### Block Endpoints

```http
GET /properties/{id}/blocks
GET /properties/{id}/blocks/{id}
POST /properties/{id}/blocks
DELETE /properties/{id}/blocks/{id}
PUT /properties/{id}/blocks/{id}
```

Body example:

```json
{
  "startDate": "2022-12-01",
  "endDate": "2022-12-10",
  "reason" : "Painting"
}
```

A postman collection is provided in the `postman` folder for testing purposes.

## Possible improvements
 
### Application structure(if it was a real project with production purposes)

- Don't use memory database, having a relational with some migration tool to version and execute structure changes(in pipeline or integration tests).
- Use proper environment variables for configuration.
- Use some APM tool to monitor the application. (Like datadog, newrelic, etc)
- Use some log management tool to centralize logs. (Like ELK)

### Design improvements

- Include pagination in the list responses
- Use better Lombok annotations to avoid boilerplate code
- Track history of bookings and blocks (probably using events to be consumed by other microservices)
- Use a linter to enforce code style
- Use Richardson Maturity Model level 3, Adding HATEOAS to the responses, making it easier to consume the API. Linking related resources.



### Code improvements

- It's possible to deal properly with booking cancelling and rebooking using the `/properties/{id}/bookings/{id}` endpoint, but it would be better to have a specific endpoint for those, isolating rules and making it easier to consumers.
- Create a proper endpoint for all reservation in a property (bookings and blocks).
- Don't allow to book or block a property past dates (maybe isolating booking history to a different microservice)
- Have specific entities and endpoints for property owners and guests.
- Create unit testing for validations
- Create specific unit tests for entity mappers
- Proper API documentation
