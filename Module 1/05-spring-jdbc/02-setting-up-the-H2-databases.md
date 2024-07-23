# Setting up the H2 Database

Learn how to connect to the in-memory H2 database, create tables, and populate data.

> We'll cover the follwing:
>
> - 1. Configuring database connection
> - 2. Creating a table
> - 3. Inserting data

In this lesson, we will set up the in-memory H2 database. Let's have a look the required steps:

## 1. Configuring database connection

- The in-memory database H2 has automatically been configured in our application. The URL can be found from the console log. This value is randomly generated each time the server is started.  
   To make the database URL a constant, we need to configure this in applicaiton.properties as follows:

            spring.datasource.url = jdbc:h2:mem:testdb

- The next task is connecting to the H2 database. One of the reasons for using Spring Boot is that its autoconfiguration feature looks at the H2 dependency and automatically configures a connection to the H2 database.  
   The H2 console can be enabled in the application.properties file as follows:

            spring.h2.console.enabled = true

- The database can be viewed in the web browser by typing localhost:8080/h2-console or http://127.0.0.1:8080/h2-console.  
   In the login page that shows up, make sure that the JDBC URL is the same as the one that we provided in the application.properties file (jbdc:h2:mem:testdb).  
   If not, change it to jdbc:h2:mem:testdb and click "Connect" to go to the database console.

This will open up the interface of the database.

## 2. Creaing a table

- Now that we have connected to our database, it is time to create a table.  
   We can do that in H2 web console by using a CREATE TABLE query, but since this is an in-memory database, all changes will be lost when the application is terminated.  
   A better way is to define the query in a SQL file.

  For this purpose, create an SQL file in src/main/resources called schema.sql. This file will be called whenever the application is launched and will initialize the database.  
   It contains all the DDL (Data Definition Language) queries.  
   We will create a table called Player in this file:

        Create table Player (
            ID integer not null,
            Name varchar(255) not null,
            Nationality varchar(255) not null,
            Birth_date timestamp,
            Title integer,
            PRIMARY KEY (ID)
        );

  After saving this file, if we connect to the H2 console again, we will see the Player table with five columns. To view this table, we can write a simple query as follows:

        Select * from Player;

  It will display the message that _The table is empty at the moment._

## 3. Inserting data

- We can insert data in the table from the H2 console. However, it will be lost when the application stops. To avoid inserting data every time we restart the application, we will create another SQL file in the src/main/resources folder and add all our INSERT queries to that file.  
   This file is called **data.sql** and it contains all DML (Data Manipulation Language) queries.

              INSERT INTO Player (ID, Name,
              Nationality, Birth_date, Titles)
              VALUES(1, 'Djokovic', 'Serbia', '1987-05-22', 81);

              INSERT INTO Player (ID, Name,
              Nationality, Birth_date, Titles)
              VALUES(2, 'Monfils', 'France', '1986-09-01', 10);

              INSERT INTO Player (ID, Name,
              Nationality, Birth_date, Titles)
              VALUES(3, 'Isner', 'USA', '1985-04-26', 15);

  Now if we run the application, the data is read from the data.sql file and the Player table is populated. We can run SELECT \* query to confirm that our table has 3 rows.

In the login page that shows up, make sure that the JDBC URL is the same as the one that we specified in step 1 (jdbc:h2:mem:testdb).
