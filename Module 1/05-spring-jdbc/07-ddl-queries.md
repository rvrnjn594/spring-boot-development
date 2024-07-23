# DDL Queries

Learn how to execute a DDL query using the JdbcTemplate class.

> We'll cover the following:
>
> - Create table query

Data Manipulation Language (DML) **queries are the one where we manipulate the data in the table.**

In the rare scenario, we might want to **create a table which is a Data Definition Language (DDL).**  
 In that case, we can use the execute() method of the JdbcTemplate.

## Create table query

> Let's say we want to create a Tournament table to store details of a tennis tournament.  
>  We will create a method called createTournamentTable() with a CREATE TABLE query.

            public void createTournamentTable() {
                String sql = "CREATE TABLE TOURNAMENT (ID INTEGER, NAME VARCHAR(50), LOCATION VARCHAR(50), PRIMARY KEY (ID))";

                jdbcTemplate.execute(sql);
                System.out.println("Table created");
            }

We can call the createTournamentTable() method in the run() method and check if a table has been created from the output log.

            @Override
            public void run(String... args) throws Exception {
                dao.createTournamentTable();
            }

> We can verify if the table has been created from the H2 console which shows an empty Tournament table.
>
> The database can be viewed in the web browser by typing localhost:8080/h2-console or http://127.0.0.1:8080/h2-console.  
>  In the login page that shows up, application.properties file (jdbc:h2:mem:testdb).
>
> If not, change it to jdbc:h2:mem:testdb and click "Connect" to go to the database console.
>
> This will open up the interface of the database.
