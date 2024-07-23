# UPDATE Query

Learn how to update an existing record in the database.

> We'll cover the following:
>
> - JdbcTemplate update() method

## JdbcTemplate update() method

> The update() method of the JdbcTemplate is used to execute INSERT as well as UPDATE queries.  
>  So, to update a record in the table, we will create a method updatePlayer(), which will be similar to the insertPlayer() method.
>
> The only difference is **in the query**, which will change from INSERT to an UPDATE query.

The updatePlayer() method will take a Player object and update the row corresponding to the Id value of the object.  
 As mentioned in the previous lesson, the update() method of the JdbcTemplate returns the number of rows affected by the query.

            public int updatePlayer(Player player) {
                String sql = "UPDATE PLAYER" + "SET Name = ?, Nationality = ?, Birth_date = ?, Titles = ? " + "WHERE ID = ?";

                return jdbcTemplate.update(sql, new Object[] {player.getName(), player.getNationality(), new Timestamp(player.getBirthDate().getTime()), player.getTitle(), player.getId()});
            }

Notice that we have changed the order of getter methods to match the order in which values will be passed to the query.

> To execute the UPDATE query in the run() method, we will initialize a Player object with the values that we want to update and pass it to the updatePlayer() method.
>
> The code given below changes the birthDate attribute of the player.

            @Override
            public void run(String... args) throws Exception {
                //Inserting a player
                logger.info("Inserting Player 4: {}", dao.insertPlayer(
                                    new Player(4, "Thiem", "Austria",
                                                new Date(System.currentTimeMillis()), 17)));

                //Updating a player
                logger.info("Updating Player with Id 4: {}",  dao.updatePlayer(new Player(4, "Thiem", "Austria",Date.valueOf("1993-09-03"), 17)));

                //View player by Id
                logger.info("Players with Id 4: {}", dao.getPlayerById(4));
            }

When the application is run, updatePlayer() method returns 1, which means that the UPDATE query affected on row in the table.  
 We can display the details of the player with id 4 using the getPlayerById() to verify that the birthDate has been updated.

> The database can be viewed in the web browser by typing localhost:8080/h2-console or http://127.0.0.1:8080/h2-console.  
>  In the login page that shows up, make sure that the JDBC URL is the same as the one that we provided in the applications.properties file (jdbc:h2:mem:testdb). If not, then change it to jdbc:h2:mem:testdb and click connect to go to the database console. This will open up the interface of the database.
