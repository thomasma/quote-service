# Spring Boot App that retrieves a random quote service RESTful service

*thanks to jokes from http://api.icndb.com*

*Pre-Requsites (do this only once and only if interested in running this sample using Spring Cloud...otherwise ignore this section)*

Run this API application using

    mvn spring-boot:run

### Get random quote

    curl -XGET  http://localhost:5000/quotes/

## Actuator endpoint

    curl http://localhost:5000/health 
    curl http://localhost:5000/env
    curl http://localhost:5000/info
    curl http://localhost:5000/metrics