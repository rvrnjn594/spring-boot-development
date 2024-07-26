# Creating a Repository

Learn how to create a repository - a class that manages entities.

> We'll cover the following:
>
> - @Repository annotation
> - Enabling transaction management
> - EntityManager and @PersistenceContext annotation

In JPA terms, a repository is a class that manages the entities.

## @Repository

We will create a PlyerRepository class to manage the Player entity and to store and retrieve data.  
 We can mark this class with the @Repository annotation.

        @Repository
        public class PlayerRepository {

        }

## Enabling transaction management

Database queries contain multiple steps.  
 We will also enable transaction management to allow all steps in a query to succeed. In case of an error or runtime exception, all steps will be rolled back.  
 Transactions are implemented at the business layer, but in this example, we will implement them at the repository level. Spring provides all the boilerplate code to start, commit, and roll back a transaction, which can also be integrated with JPA’s transaction management.

This is enabled using the @Transactional annotation on a method or a class.

        @Repository
        @Transactional
        public class PlayerRepository {

        }

Using this annotation on the PlayerRepository class, all the methods will be executed in a transactional context.  
So if we have INSERT and UPDATE in a single method, something called an EntityManager will keep track of both of them.  
(if one of them fails, the whole operation will be rolled back.)

## EntityManager and @PersistenceContext annotation

A JPA EntityManager manages connection to a database as well as to database operations.  
 EntityManager is associated with a PersistenceContext.  
 All operations that are performed in a specific session are stored inside the PersistenceContext.

EntityManager is the interface to the Persistence Context. All operations on the entity go through the EntityManager.

We will declare an EntityManager object in our class and mark it with the @PersistenceContext annotation.

        public class PlayerRepository {
            @PersistenceContext
            EntityManager entityManager;
            // ..
        }

EntityManager provides a number of methods that perform SELECT, INSERT, UPDATE, and DELETE queries.

In this lesson, we created a repository and an instance of EntityManager. Now we are ready to perform operations on the database.

The code shown below, when run, creates an empty table of players. We will insert data in the next step.

> The database can be viewed in the web browser by typing localhost:8080/h2-console or http://127.0.0.1:8080/h2-console. In the login page that shows up, make sure that the JDBC URL is the same as the one that we provided in the applications.properties file (jdbc:h2:mem:testdb). If not, change it to jdbc:h2:mem:testdb and click connect to go to the database console. This will open up the interface of the database.

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

        }

**Player.java**

        package io.datajek.springdatajpa;

        import java.sql.Date;
        import java.sql.Time;

        import javax.persistence.Entity;
        import javax.persistence.GeneratedValue;
        import javax.persistence.Id;

        @Entity
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

**TennisPlayerApplication.java**

        package io.datajek.springdatajpa;

        import org.springframework.beans.factory.annotation.Autowired;
        import org.springframework.boot.SpringApplication;
        import org.springframework.boot.autoconfigure.SpringBootApplication;

        @SpringBootApplication
        public class TennisPlayerApplication {

            public static void main(String[] args) {
                SpringApplication.run(TennisPlayerApplication.class, args);
            }

        }
