# king-assessment
This is a Game System

## Description
The application an HTTP-based mini-game back-end in Java which registers game scores for different users and levels, with the capability to return high score lists per level. There shall also be a simple login system
in place (without any authentication).

## Technology
This is `Java` application running using `Spring` framework (`Spring Boot Starter`). `Tomcat` is used as embedded server. `Maven` is used as build automation system. `Lombok` is used to reduce boilerplate.

## Build
```
  $ mvn clean install 
```

## Run
Application can be simply started using `java -jar`. 

```
  $ java -jar target/king-assessment-0.0.1-SNAPSHOT.jar
```

...or use docker to run the application 
```
  $ docker build -t king/interviewtask .
  $ docker run -p 8080:8080 king/interviewtask
```

By default it will be available under `http://localhost:8080/`.

## REST Api
### Login to the system
Get list of all available features

#### Request
`GET /{userId}/login/`

#### Response
    Status: 200 OK
    Content-Type: application/json
    56708899-963b-4578-b0c6-cb3af22bb484
    
### Post a user's score to a level
This API can be called several times per user and level and does not return anything. Only
requests with valid session keys shall be processed.

#### Request
`POST {levelId}/score?sessionKey={sessionKey}`
`Body : <score>`

#### Response
    Status: 200 OK
    Content-Type: application/json
    
### Get a high score list for a level
Retrieves the high scores for a specific level. The result is a comma separated list in descending score
order. Because of memory reasons no more than 15 scores are to be returned for each level. Only
the highest score counts. ie: an user id can only appear at most once in the list. If a user hasn't
submitted a score for the level, no score is present for that user. A request for a high score list of a
level without any scores submitted shall be an empty string.

#### Request
`GET /{levelId}/highscorelist`

#### Response
    Status: 200 OK
    Content-Type: image/png
    CSV comes here

#### Swagger
`http://localhost:8080/v2/api-docs`

#### Test methods Naming Convention
`<methodUnderTest>_<concreteScenarioExercisedByTheTest>_<expectedOutcome>`
