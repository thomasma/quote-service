# Spring Boot App that retrieves a random quote service RESTful service

*thanks to jokes from http://api.icndb.com*

*Pre-Requsites (do this only once and only if interested in running this sample using Spring Cloud...otherwise ignore this section)*

Run this API application using

dev profile
    
    mvn clean spring-boot:run -Dspring.profiles.active=dev

test profile
    
    mvn clean spring-boot:run -Dspring.profiles.active=test


### Get random quote

    curl -XGET  http://localhost/quotes/

## Actuator endpoint

    curl http://localhost/actuator/health
    curl http://localhost/actuator/env
    curl http://localhost/actuator/info
    curl http://localhost/actuator/metrics