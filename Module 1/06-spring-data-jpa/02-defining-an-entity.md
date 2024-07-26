# Defining an Entity

Learn how to create an entity and look at different JPA annotations for defining a relational mapping.

> We'll cover the following:
>
> - JPA dependency
> - @Entity
> - @Table
>   - @Id and @GeneratedValue
>   - @Column
> - JPA schema creation

## JPA dependency

To use Spring Data JPA, we will add the starter JPA dependency to the pom.xml file as follows:

    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-data-jpa</artifactId>
    </dependency>

When the pom.xml file is saved, we can see the JPA API in the Maven Dependencies folder. This API defines a lot of different annotations like @Entity, @Column, @Table, etc.  
 Hibernate is an implementation of the JPA API which automatically gets configured in our application.  
 The hibernate-core jar can be seen in the Maven Dependencies folder.

> We will take the tennis player database example to understand Spring Data JPA. We will make a copy of the Player and TennisPlayerApplication classes and save them to a new package, io.datajek.springdatajpa.
>
> As for the PlayerDao class, we will implement the functionality of all methods using the JPA API.

## @Entity

> In our example, we have a Player class that lists attributes of a tennis player like his name, nationality, date of birth, and a number of titles won.
>
> We need to tell JPA that the objects of this class need to be mapped to a table in a database. JPA will create a table with the same name as the class and create columns for all the members of the class.
>
> Every instance of the Player class will become a row in the Player table. We will use the @Entity annotation to map this class to the Player table.
>
>       @Entity
>       @Table(name="Player")
>       public class Player {}

## @Table

In case we want to map this class to a table with a different name, we can use the @Table annotation and provide the name of the table to which the bean maps to, as shown in the code above.  
 Since the name of the entity and table match, we do not need the @Table annotation.

#### @Id and @GeneratedValue

Every table in a realational database has a primary key.  
In our case, the Id attribute uniquely identifies each object.

The @Id annotation is used to indicate the primary key.  
We can also let JPA generate the primary key value when we insert rows.

The @GeneratedValue annotation will automatically generate Id values.

        @Entity
        public class Player {
            @Id
            @GenerateValue
            private int id;
            private String name;
            private String nationality;
            private Date birthData;
            private int titles;
            // ..
        }

Now whenever a new row is inserted, we do not need to supply the Id value. We need another constructor that initializes all attributes except Id.

        // constructor without Id attribute
        public Player (String name, String nationality, Date birthDate, int titles) {
            super();
            this.name = name;
            this.nationality = nationality;
            this.birthData = birthDate;
            this.titles = titles;
        }

The Player class now has three constructors; a no-arg constructor, one that initializes all five attributes, and one that initializes all attributes except the primary key.

#### @Column

Another annotation provided by the JPA API is @Column annotation, which is used to define column mappings.  
@Column annotation mentions the name of the column that matches an attribute of the class.

        @Entity
        public class Player {
            @Id
            @GeneratedValue
            private int Id;
            private String name;

            @Column(name='nationality')
            private String nationality;

            private Date birthDate;
            private int titles;
            // ..
        }

In our example, the column mapping annotation is not needed since the column names match.

## JPA schema creation

The Spring Boot autoconfiguration triggers a schema update and creates a table by the same name as the class marked with the @Entity annotation.  
 When using JPA, we do not need to create a table.  
 We will comment out the table creation query in schema.sql as it is not needed anymore.

After running the application, it can be confirmed from the H2 console - http://localhost:8080/h2-console (using JDBC URL : jdbc:h2:mem:testdb) - that a player table has been created.

The output log shows the table creation query:

        > Hibernate: drop table if exists player CASCADE
        > Hibernate: drop sequence if exists hibernate_sequence
        > Hibernate: create sequence hibernate_sequence start with 1 increment by 1
        > Hibernate: create table player (id integer not null, birth_date date, name varchar(255), nationality varchar(255), titles integer not null, primary key (id))

Here's complete code:

**application.properties**

        #Logging.level.org.springframework = debug
        spring.datasource.url=jdbc:h2:mem:testdb
        spring.h2.console.enabled=true
        spring.jpa.show-sql=true

**schema.sql**

        /*CREATE TABLE Player
        (
        ID INTEGER NOT NULL,
        Name VARCHAR(255) NOT NULL,
        Nationality VARCHAR(255) NOT NULL,
        Birth_date TIMESTAMP,
        Titles INTEGER,
        PRIMARY KEY (ID)
        );*/

**Player.java**

        package io.datajek.springdatajpa;

        import java.sql.Date;
        import java.sql.Time;

        import javax.persistence.Entity;
        import javax.persistence.GeneratedValue;
        import javax.persistence.Id;
        import javax.persistence.NamedQuery;

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
        }

**TennisPlayerApplication.java**

        package io.datajek.springdatajpa;

        import org.springframework.boot.SpringApplication;
        import org.springframework.boot.autoconfigure.SpringBootApplication;

        @SpringBootApplication
        public class TennisPlayerApplication {

            public static void main(String[] args) {
                SpringApplication.run(TennisPlayerApplication.class, args);
            }
        }
