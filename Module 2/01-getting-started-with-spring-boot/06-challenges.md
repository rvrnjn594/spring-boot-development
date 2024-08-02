# Challenges

Spring Boot by default uses Tomcat on port 8080. Can we use the Jetty server on port 7070?

> We'll cover the following:
>
> - Problem statement

## Problem statement

The strategic direction from our organization is to **use the Jetty server for all the applications**.  
 In order to make the Elite Club application abide by those standards, it should be **upgraded to use Jetty instead of Tomcat**.

Implement the changes in the below application.

Good luck!

#### Use Jetty instead of Tomcat

Exclude Tomcat dependency from the spring-boot-starter-web dependency and add spring-boot-starter-jetty dependency in Maven POM.

        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
            <exclusions>
                <exclusion>
                    <groupId>org.springframework.boot</groupId>
                    <artifactId>spring-boot-starter-tomcat</artifactId>
                </exclusion>
            </exclusions>
        </dependency>

        <dependency>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-starter-jetty</artifactId>
        </dependency>

#### Use port 7070

Adding **property server.port=7070** in the applicaiton.properties should run the application on 7070.
