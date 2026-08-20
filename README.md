# Instructions for candidates

This is the Java version of the Payment Gateway challenge. If you haven't already read this [README.md](https://github.com/cko-recruitment/) on the details of this exercise, please do so now.

## Requirements
- JDK 17
- Docker

## Template structure

src/ - A skeleton SpringBoot Application

test/ - Some simple JUnit tests

imposters/ - contains the bank simulator configuration. Don't change this

.editorconfig - don't change this. It ensures a consistent set of rules for submissions when reformatting code

docker-compose.yml - configures the bank simulator


## API Documentation
For documentation openAPI is included, and it can be found under the following url: **http://localhost:8090/swagger-ui/index.html**

**Feel free to change the structure of the solution, use a different library etc.**

## Implementation Details

The functional requirements are implemented using the domain driven design.
Package structure:

```text
com.checkout.payment.gateway
├── application     - main application service    
├── domain          - contains business logic, framework free
├── presentation    - controllers and request/response models
└── infrastructure  - wiring in dependencies and jackson configurations, repository, bank client API
```

Exposed APIs can be viewed in OpenAPI documentation.

Key points:
- the application supports only 3 currencies GBP, USD and EUR. This is based on the following requirement from main challenge document:
> Ensure your submission validates against no more than 3 currency codes
- edge cases around Bank API such timeout and others are not handled, any error returned by Bank API will result in rejected authorisation and payment will not be created
- if invalid data is supplied in request body, 400 response will be returned (the validation is performed inside domain model tiny types)
- if not supported currency is used or the expiry date is not in the future, 422 response will be returned