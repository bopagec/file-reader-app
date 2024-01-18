## Solution
* Developed a Spring Boot Command Line Runner App that accepts `filePath` as an 
argument of the `csv` file that contains customer details.

* Developed a Spring Boot REST API to support the followings
    * `POST` endpoint to accept Customer to save on the `h2 in memorhy SQL database`
    * `GET` endpoint to get Customer given `customerRef`

## How To Run

#### get `customer-service-api` up and running with h2 in memory database console
* git clone https://github.com/bopagec/customer-service-api.git
* cd customer-service-api
* git checkout main
* mvn clean install
* mvn spring-boot:run
* see the h2 console http://localhost:8080/h2-console (leave password box blank and press connect)

#### get `file-reader-app` up and running test with `csv` file as an input argument
* https://github.com/bopagec/file-reader-app.git
* cd file-reader-app
* git checkout main
* mvn clean install
* mvn spring-boot:run -Dspring-boot.run.arguments="--filePath=customers.csv"


## GIT Repos
code is in the main branch in both repos
* https://github.com/bopagec/file-reader-app.git ( main)
* https://github.com/bopagec/customer-service-api.git (main)

## Tech Stack 
The following Technologies have been used to develop both applications.
* Spring Boot v3
* Spring Data JPA
* WebFlux for WebClient
* Lombok
* Java 17
* H2 in memory SQL DB
* apache-commons-csv
* JUnit 5
* Mockito