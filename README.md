# Stock API
This is a * Java RESTful Web Service * for Stock API.

## Requirements

For building and running the application you need:

- [JDK 8](https://www.oracle.com/java/technologies/javase/javase-jdk8-downloads.html)
- [Maven 3](https://maven.apache.org)

## How to build and run

In order to build please do so:

```shell
mvn clean install
```

After successful build, please do so:

```shell
mvn spring-boot:run
```

Alternatively you can run jar from terminal like so:

```shell
java -jar target/stock-api-0.0.1-SNAPSHOT.jar
```

In order to test please do so:

```shell
mvn test
```

## API Endpoint Documentation
- [Swagger](http://localhost:8080/swagger-ui.html)