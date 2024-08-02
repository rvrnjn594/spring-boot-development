# Simplify Dependency Management

Learn how Spring Boot simplifies dependency management by providing opinionated starter dependencies.

> We'll cover the following:
>
> - Issues with dependency management
> - Spring boot starters
> - Key takeaways

## Issues with dependency management

A set of JARs is required for a Java application to function properly. If we’ve spent any time developing software, we’re probably familiar with the headaches that come with dependency management.

Any feature we want to introduce in a Java based application will require a number of dependencies.  
 For example, a set of dependencies must be added for our application to expose a simple REST endpoint.

This problem multiplies when we consider versions. One version of a particular dependency can work with only a specific version of other dependencies.

What if we happen to upgrade a library to a newer version?  
 It will have a cascading effect on all the dependent libraries, which would eventually lead to a game of dependency “Whack-a-Mole.”

## Spring boot starters

Spring Boot starters are **Bills of Material (BOM)** which provide packaged dependency of JARs compatible with each other.

Whenever we build an API, we usually do the following:

- Expose endpoints
- Listen for requests
- Respond to them
- Exchange data in one or more interchange formats like JSON, XML Atom etc...

The above development model is fairly consistent and has become an industry-wide approach. It’s captured in a Spring Boot starter, just like other similar patterns.

Whenever we include any spring-boot-starter.\* dependencies, all the necessary transitive dependencies that have been thoroughly tested and promised to work together are brought up

        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
            <version>2.3.1.RELEASE</version>
        </dependency>

With this one dependency, our application is web-enabled and ready to expose the endpoint.  
 This is without the need for manual configuration of the dispatcher servlet, content negotiator, serializer, deserializer, etc.

## Key takeaways

- Spring boot simplifies dependency management using the bill of materials (BOM) starter dependencies.
