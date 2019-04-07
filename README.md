This is wallet grpc project , I developed this project using Intellij IDE and these technologies:

Springboot;
Gradle;
Oracle Database;
Mysql Database
H2 (memory database);
Swagger;
Lombok;
Actuator;
Google GRPC;
Spring rest.

This project is formed by two SpringBoot Applications:

 -Wallet-server: this is the server that receive the grpc request sent by client and process it. This project has unit test for services layer (UserService.class and WalletService.class).
  Notes about this project:
	- This project is configured to use 9091 port;
	- You can just send requests to this application using client application (GRPC).

 
 
 - Wallet-client: this project is responsible to create the grpc request to server applicaton and receive the http request, you can check these request types and payload in this url:
 http://localhost:8081/wallet-client/swagger-ui.html. Also, in this project there is end to end tests.
 Notes about this project:
	- To running end to end test you need have wallet-server project up and running;
	- In application.properties file in this project there are the configurations to point the server project: grpc.server.port=9091 and grpc.server.address=127.0.0.1.
	- This project is configured to use 8081 port.


Notes in both applications:

	- Both applications using the same database, actually you have options to use Oracle, Mysql or H2 (virtual database), each spring profile is configured to use a databse type;
	- To running these applications you need to choose a Spring profile: oracle, mysql or h2, in Intellij you need an environment variable to start the projects like: spring.profiles.active=mysql, using docker this profile configuration stay in docker-compose file.
	- To use docker you need follow these steps:
		- Build the application;
		- Build docker image with this command: docker build -t wallet-server . or docker build -t wallet-client . (you need to run this command in root project that you want to create the docker image);
		- Execute the docker-compose file with this command: docker-compose up (you need to run this command in root project that you want to create the docker image).
	- You can check if application are running using the actuator feature:
			- http://localhost:9091/wallet-server/actuator/health
			- http://localhost:8081/wallet-client/actuator/health
	- The projects are configured to create the database tables structure every time that the applications are started, if you want to disable it, you need to comment (#) or delete this line "spring.jpa.hibernate.ddl-auto=update" on application.properties.


Database schema:
	The database schema is very simple, it is compost by these tables:
	
		- User
		
		- Wallet

If you have questions, please feel free to contact me.