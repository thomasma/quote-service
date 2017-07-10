Spring Boot App. Retrieve random quote service RESTful service. 

# Pre-Requsites (do this only once and only if interested in running this sample using Spring Cloud...otherwise ignore this section)
## Run a local Eureka Service Registry
Retrieve a copy of the Eureka server project and run that localy to stand up a local instance.

    git clone https://github.com/thomasma/eureka-service-registry
    mvn clean spring-boot:run
    http://localhost:8761
    http://localhost:8761/eureka/apps (XML feed from Eureka)

## Run a local Hysterix Dashboard 
Retrieve a copy of the hystrix-dashboard sample server project and run that localy to stand up a local server.

    git clone https://github.com/spring-cloud-samples/hystrix-dashboard.git
    mvn clean spring-boot:run
    From your browser go to http://localhost:7979/

## To run the app 
Download and run Mongodb. 

    mongod --dbpath ~/data/mongodata/

# Run the Application
Run this API application using

    mvn spring-boot:run

## Interact with app using curl
### Get random quote
    curl -XGET  http://localhost:5000/quotes/

## Actuator endpoint
    curl http://localhost:5000/health 
    curl http://localhost:5000/env
    curl http://localhost:5000/info
    curl http://localhost:5000/metrics
    
