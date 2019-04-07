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

 - Wallet-server: this is the server that receive the grpc request sent by client and process it. This project has unit test for services layer (UserService.class and WalletService.class).
  Notes about this project:
	- This project is configured to use 9091 port;
	- You can just send requests to this application using client application.
 
 - Wallet-client: this project is responsible to create the grpc request to server applicaton and receive the http request.

 Notes about this project:
 
	- In this project we have two endpoints:
		- Concurrency-controller: That receive a post to simulate a lot of users accessing application;
		- Wallet-controller : This controller have these operations:
			- get - /wallet/balance: To get a balance of one user;
			- post - /wallet/deposit: To deposit value in a specific user;
			- post - /wallet/withdraw: To withdraw operation in a specific user.
		For more details you can acess this url: http://localhost:8081/wallet-client/swagger-ui.html to access swagger documentation, in this documentation you can see the payloads every details about these endpoints;
	- In this project there are integrantion test and to running it you need to have wallet-server project up and  running with the same profile that wallet-client application, you can change it, you just need to configure the profile in ActiveProfiles annotation in IntegrationTest class of wallet-client application and the wallet-server application need to be running the same profile that wallet-client;
	- I putted @Ignore in test method of IntegrationTest class because in the gradle build will try to run the tests and if you don't have wallet-server application configured and runnig the build will failed;
	- In application.properties file in this project there are the configurations to point the server project: grpc.server.port=9091 and grpc.server.address=127.0.0.1;
	- This project is configured to use 8081 port.

Notes in both applications:

	- To generate grpc files you need to execute gradle build;
	- Both applications using the same database, actually you have options to use Oracle, Mysql or H2 (virtual database), each spring profile is configured to use a database type;
	- You need configure your database in profiles files: application-h2.properties and application-mysql.properties.
	- To running these applications you need to choose a Spring profile: oracle, mysql or h2, in Intellij you need an environment variable to start the projects like: spring.profiles.active=mysql, using docker this profile configuration stay in docker-compose file.
	- To use docker you need follow these steps:
		- Build the application;
		- Build docker image with this command: docker build -t wallet-server . or docker build -t wallet-client . (you need to run this command in root project that you want to create the docker image);
		- Execute the docker-compose file with this command: docker-compose up (you need to run this command in root project that you want to create the docker image).
	- You can check if applications are running using the actuator feature:
			- http://localhost:9091/wallet-server/actuator/health
			- http://localhost:8081/wallet-client/actuator/health
	- The projects can create the database tables structure every time that the applications are started, if you want to do it, you need to discomment( remove #) in this line "spring.jpa.hibernate.ddl-auto=update" on application.properties (in wallet-client or wallet-server).


Database schema:
	The database schema is very simple, it is compost by these tables:
	
		- Users
			user_id: int(11) PK
			user_name: varchar(255)
		
		- Wallet
			wallet_id: int(11) PK
			balance: decimal(19,2)
			currency: int(11)
			user_id: int(11)

If you have questions, please feel free to contact me.