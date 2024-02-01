# booking-service

## Description

This a simple booking service that allows you to book or block properties. 

It is a RESTful Java 17 application that uses Spring Boot and H2 database.

Its developed using layered architecture, with an app, service, repository layers. Divided into modules, with a domain modules.


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

```http
GET /properties
GET /properties/{id}
POST /properties
DELETE /properties/{id}
PUT /properties/{id}
```

```http
GET /properties/{id}/bookings
GET /properties/{id}/bookings/{id}
POST /properties/{id}/bookings
DELETE /properties/{id}/bookings/{id}
PUT /properties/{id}/bookings/{id}
```

```http
GET /properties/{id}/blocks
GET /properties/{id}/blocks/{id}
POST /properties/{id}/blocks
DELETE /properties/{id}/blocks/{id}
PUT /properties/{id}/blocks/{id}
```

A postman collection is provided in the `postman` folder for testing purposes.

## Possible improvements
 
### Application structure(if it was a real project with production purposes)

- Don't use memory database, having a relational with some migration tool to version and execute structure changes(in pipeline or integration tests).
- Use proper environment variables for configuration.
- Use some APM tool to monitor the application. (Like datadog, newrelic, etc)
- Use some log management tool to centralize logs. (Like ELK)
- 

### Design improvements

- Richardson Maturity Model level 3 TODO - Add HATEOAS to the responses
- Include pagination in the list responses
- Use better Lombok annotations to avoid boilerplate code
- 

### Code improvements

- Even if is possible to deal properly with booking cancelling and rebooking using the `/properties/{id}/bookings/{id}` endpoint, it would be better to have a specific endpoint for those, isolating rules and making it easier to consumers.
- Don't allow to book or block a property past dates (maybe isolating booking history to a different microservice)
- 
