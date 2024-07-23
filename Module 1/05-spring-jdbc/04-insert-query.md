# INSERT Query

Learn how to insert a record in the database using the JdbcTemplate class.

> We'll cover the following:
>
> - JdbcTemplate update() method

In this lesson, we are going to learn **how to write to the database using the methods of JdbcTemplate class**.

## JdbcTemplate update() class

To insert a row, we need to send a Player object as input parameter.

The update() method is used for an INSERT query.  
 This method takes the **SQL query as well as an array of objects containing the values that will be inserted.**  
The method returns an int value which shows the number of rows affected by the query.

            public int insertPlayer (Player player) {
                String sql = "Insert Into Player (Id, Name, Nationality, Birth_date, Titles)" + "VALUES (?, ?, ?, ?, ?)";

                return jdbcTemplate.update(sql, new Object[] {player.getId(), player.getName(), player.getNationality(), new Timestamp(player.getBirthDate().getTime()), player.getTitles()});
            }

We will execute the INSERT query in the run() method.  
 We will use the Player constructor with arguments to initialize a Player object and pass it to the method.  
We are passing the current date for the new record:

            @Override
            public void run(String... args) throws Exception {
                logger.info("Inserting Player 4: {}", dao.insertPlayer(new Player(4, "Thiem", "Austria", new Date(System.currentTimeMillis()), 17)));

                logger.info("All Players Data: {}", dao.getAllPlayers());
            }

> As can be seen from the output, the insertPlayer() method returns 1, which means that 1 row has been affected by the query.  
>  To verify the insert, we are calling the getAllPlayers() method again. This time 4 rows are returned. We can also execute the Select \* statement in the H2 console.
>
> The database can be viewed in the web browser by typing localhost:8080/h2-console or http://127.0.0.1:8080/h2-console. In the login page that shows up, make sure that the JDBC URL is the same as the one that we provided in the application.properties file (jdbc:h2:mem:testdb).  
>  If not, change it to jdbc:h2:mem:testdb and click connect to go to the database console.
>
> This will open up the interface of the databse.
