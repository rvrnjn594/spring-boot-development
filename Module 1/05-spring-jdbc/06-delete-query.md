# DELETE Query

Learn how to **use the update method to delete a record from the database.**

> We'll cover the following:
>
> - JdbcTemplate update() method

## JdbcTemplate update() method

> In the previous lesson, we learned that the update() method of the JdbcTemplate is used to execute INSERT as well as UPDATE queries.  
>  In this lesson, we will write a **query to delete a row based on the ID value using the update() method** of the JDBCTemplate class.

Let's call the method which implements the delete functionality, deletePlayerById().  
 This method returns an integer value of the number of rows that were affected by the query.

            public int deletePlayerById(int id) {
                String sql = "DELETE FROM PLAYER WHERE ID = ?";
                return jdbcTemplate.update(sql, new Object[] {id});
            }

We will execute the DELETE query in the run() method by calling the deletePlayerById() method:

            @Override
            public void run(String... args) throws Exception {
                logger.info("Deleting Player with Id 2: {}", dao.deletePlayerById(2));
                logger.info("All Players Data: {}", dao.getAllPlayers());
            }

> The log shows that deleteById method returns 1.  
>  We can confirm the deletion by checking the H2 console and executing the SELECT \* FROM PLAYER query or by calling the getAllPlayer method of PlayerDao class.

Apart from inserting, updating, and deleting records, the update method of JdbcTemplate can also be used to run stored procedures.  
 We can **define a stored procedure in the same manner as we have defined a query and then pass it as an argument to the update method.**

> The database can be viewed in the web browser by typing localhost:8080/h2-console or http://127.0.0.1:8080/h2-console.  
>  In the login page that shows up, make sure that the JDBC URL is the same as the one that we provided in the application.properties file (jdbc:h2:mem:testdb).
>
> In not, change it to jdbc:h2:mem:testdb and click connect to go to the database console.  
>  This will open up the interface of the database.

**TennisPlayerApplicatoin.java**

                package io.datajek.springdatajdbc;

                import java.sql.Date;
                import org.slf4j.Logger;
                import org.slf4j.LoggerFactory;
                import org.springframework.beans.factory.annotation.Autowired;
                import org.springframework.boot.CommandLineRunner;
                import org.springframework.boot.SpringApplication;
                import org.springframework.boot.autoconfigure.SpringBootApplication;

                @SpringBootApplication
                public class TennisPlayerApplication implements CommandLineRunner {
                    private Logger logger = LoggerFactory.getLogger(this.getClass());

                    @Autowired
                    PlayerDao dao;

                    public static void main(String[] args) {
                        SpringApplication.run(TennisPlayerApplication.class, args);
                    }

                    @Override
                    public void run(String... args) throws Exception {
                        logger.info("All Players Data: {}", dao.getAllPlayers());

                        logger.info("Deleting Player with Id 2: {}", dao.deletePlayerById(2));
                        logger.info("All Players Data: {}", dao.getAllPlayers());

                    }
                }

**PlayerDao.java**

                package io.datajek.springdatajdbc;

                import java.sql.ResultSet;
                import java.sql.SQLException;
                import java.sql.Timestamp;
                import java.util.List;

                import org.springframework.beans.factory.annotation.Autowired;
                import org.springframework.jdbc.core.BeanPropertyRowMapper;
                import org.springframework.jdbc.core.JdbcTemplate;
                import org.springframework.jdbc.core.RowMapper;
                import org.springframework.stereotype.Repository;

                @Repository
                public class PlayerDao {
                    @Autowired
                    JdbcTemplate jdbcTemplate;

                    public int deletePlayerById(int id){
                        String sql="DELETE FROM PLAYER WHERE ID = ?";
                        return jdbcTemplate.update(sql, new Object[] {id});
                    }

                    public int updatePlayer(Player player){
                        String sql = "UPDATE PLAYER SET Name = ?, Nationality = ?, Birth_date = ? , Titles = ? WHERE ID = ?";
                        return jdbcTemplate.update(sql, new Object[] {player.getName(), player.getNationality(),
                                            new Timestamp(player.getBirthDate().getTime()), player.getTitles(), player.getId()});
                    }

                    public int insertPlayer(Player player){
                        String sql = "INSERT INTO PLAYER (ID, Name, Nationality, Birth_date, Titles) VALUES (?, ?, ?, ?, ?)";
                        return jdbcTemplate.update(sql,
                            new Object[] {player.getId(), player.getName(), player.getNationality(),
                                            new Timestamp(player.getBirthDate().getTime()), player.getTitles()});
                    }

                    public Player getPlayerById(int id) {
                        String sql = "SELECT * FROM PLAYER WHERE ID = ?";
                        return jdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper<Player>(Player.class), new Object[] {id});
                    }

                    public List<Player> getAllPlayers() {
                        String sql = "SELECT * FROM PLAYER";
                        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<Player>(Player.class));
                    }

                }
