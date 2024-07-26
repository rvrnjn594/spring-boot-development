# JPQL Named Query

Learn how to write a named query in Java Persistence Query Language (JPQL).

> We'll cover the following:
>
> - @NamedQuery
>   - Using a named query for SELECT \* query

A named query is defined on an entity, which in our case is the Player class. The named query will be used in the getAllPlayers() method.

## @NamedQuery

To create a named query, we will use the @NamedQuery annotation on the Player class.  
This annotation requires two parameters:  
 the name of the query and the query itself.

When using JPA, we will write the query in JPQL instead of SQL. JPQL uses entities in place of tables. Since we want to return a list of Player objects, the query will be "SELECT p FROM Player p".

        @Entity
        @NamedQuery(name="get_all_players", query="select p from Player p")
        public class Player {
            //...
        }

Now we can use the named query in the getAllPlayers() method.

### Using a named query for SELECT \* query

In the getAllPlayers() method, we will use the createNamedQuery() method.  
We need to pass the name of the query and specify what the query will return. The name of the query as defined above is get_all_players().  
 This query will return objects of the Player class. The createNamedQuery() returns a TypedQuery, which we will assign to a variable called getAll.

Then, we can use the getResultList() method to return a list of players as follows:

        public List<Player> getAllPlayers() {
            TypedQuery<Player> getAll = entityManager.createNamedQuery("get_all_players", Player.class);
            return getAll.getResultList();
        }

We will now call getAllPlayers() in the run() method to display the records in the Player table.

        @Override
        public void run(String... args) throws Exception {
            logger.info("Inserting Player: {}", repo.insertPlayer(new Player("Djokovic", "Serbia", Date.valueOf("1987-05-22"), 81)));
            logger.info("Inserting Player: {}", repo.insertPlayer(new Player("Monfils", "France", Date.valueOf("1986-09-01"), 10)));
            logger.info("Inserting Player: {}", repo.insertPlayer(new Player("Thiem", "Austria", Date.valueOf("1993-09-03"), 17)));

            logger.info("All Players Data: {}", repo.getAllPlayers());
        }

**TennisPlayerApplication.java**

        package io.datajek.springdatajpa;

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
            PlayerRepository repo;
            public static void main(String[] args) {
                SpringApplication.run(TennisPlayerApplication.class, args);
            }
            @Override
            public void run(String... args) throws Exception {
                //insert players
                logger.info("\n\n>> Inserting Player: {}\n", repo.insertPlayer(
                    new Player("Djokovic", "Serbia", Date.valueOf("1987-05-22"), 81)));
                logger.info("\n\n>> Inserting Player: {}\n", repo.insertPlayer(
                    new Player("Monfils", "France", Date.valueOf("1986-09-01"), 10)));
                logger.info("\n\n>> Inserting Player: {}\n", repo.insertPlayer(
                    new Player("Thiem", "Austria",
                                        new Date(System.currentTimeMillis()), 17)));
                //update player
                logger.info("\n\n>> Updating Player with Id 3: {}\n", repo.updatePlayer(
                    new Player(3, "Thiem", "Austria", Date.valueOf("1993-09-03"), 17)));
                //get player
                logger.info("\n\n>> Player with id 3: {}\n", repo.getPlayerById(3));
                //delete player
                repo.deletePlayerById(2);
                //get all players
                logger.info("\n\n>> All Players Data: {}", repo.getAllPlayers());
            }
        }

**PlayerRepository.java**

        package io.datajek.springdatajpa;

        import javax.persistence.EntityManager;
        import javax.persistence.PersistenceContext;
        import javax.transaction.Transactional;
        import org.springframework.stereotype.Repository;

        @Repository
        @Transactional
        public class PlayerRepository {

            @PersistenceContext
            EntityManager entityManager;

            public Player insertPlayer(Player player){
                return entityManager.merge(player);
            }

            public Player updatePlayer(Player player){
                return entityManager.merge(player);
            }

            public Player getPlayerById(int id) {
                return entityManager.find(Player.class, id);
            }

            public void deletePlayerById(int id){
                Player player = entityManager.find(Player.class, id);
                entityManager.remove(player);
            }

            public List<Player> getAllPlayers() {
                TypedQuery<Player> getAll = entityManager.createNamedQuery("get_all_players", Player.class);
                return getAll.getResultList();
            }

        }

**Player.java**

        package io.datajek.springdatajpa;

        import java.sql.Date;
        import java.sql.Time;

        import javax.persistence.Entity;
        import javax.persistence.GeneratedValue;
        import javax.persistence.Id;
        import javax.persistence.NamedQuery;

        @Entity
        @NamedQuery(name="get_all_players", query="SELECT p FROM Player p")
        public class Player {
            @Id
            @GeneratedValue
            private int id;
            private String name;
            private String nationality;

            private Date birthDate;
            private int titles;

            public Player( ) {

            }

            public Player(String name, String nationality, Date birthDate, int titles) {
                super();
                this.name = name;
                this.nationality = nationality;
                this.birthDate = birthDate;
                this.titles = titles;
            }

            public Player(int id, String name, String nationality, Date birthDate, int titles) {
                super();
                this.id = id;
                this.name = name;
                this.nationality = nationality;
                this.birthDate = birthDate;
                this.titles = titles;
            }

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getNationality() {
                return nationality;
            }

            public void setNationality(String nationality) {
                this.nationality = nationality;
            }

            public Date getBirthDate() {
                return birthDate;
            }

            public void setBirthDate(Date birthDate) {
                this.birthDate = birthDate;
            }

            public int getTitles() {
                return titles;
            }

            public void setTitles(int titles) {
                this.titles = titles;
            }

            @Override
            public String toString() {
                return "\nPlayer [id= " + id + ", name= " + name + ", nationality= " + nationality + ", birthDate= " + birthDate
                        + ", titles= " + titles + "]";
            }

            public void setBirthDate(Time time) {
                // TODO Auto-generated method stub
            }
        }

**application.properties**

        spring.datasource.url=jdbc:h2:mem:testdb
        spring.h2.console.enabled=true
        spring.h2.console.settings.web-allow-others=true
        spring.jpa.show-sql=true
