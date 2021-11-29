[![Java CI with Maven](https://github.com/pintotomas/online-store/actions/workflows/main.yml/badge.svg)](https://github.com/pintotomas/online-store/actions/workflows/main.yml)
# online-store
API for an online store with java and grpc

## Technologies

- Java 11

- PostgreSQL 13 (Optional)

- Apache Maven 3.6

- Docker 20.10.6 (Optional)

- docker-compose 1.21.2 (Optional)

## Running instructions

1) Install java 11
2) Install PostgreSQL (Optionally you can go to src/main/docker and execute docker-compose up to run a dockerized db)
3) Setup the database in postgre, then specify the database url, username and passwords in resources/liquibase.properties and resources/META-INF/persistence.xml
4) Run the command mvn clean install 
5) Run the command mvn liquibase:update to execute migrations
6) Go to the target folder and run the three servers:
	- java -cp ProductServer.jar server.ProductServer (will run on port 8080)
	- java -cp CartServer.jar server.CartServer (will run on port 8081)
	- java -cp OrderServer.jar server.OrderServer (will run on port 8082)
7) If you are using BloomRPC import the proto files in src/main/proto to make requests

## Things to improve

- Allow to specify server ports
- Add diagrams
- There is some repeated code when generating some responses and on the DAO objects that can easily be refactored

