# Self Executable JAR

Learn how an executable jar generated by Spring Boot enables designing an application that scales easily.

> We'll cover the following:
>
> - Executable JAR for easy deployment
>   - Advantage of fat JAR
>   - How a fat JAR is generated
> - Key takeaways

## Executable JAR for easy deployment

In the past, when monolithic application development was standard practice in the industry, the deployment of Java applications was a complex affair. The application had to be deployed in heavy application containers like Websphere, Weblogic, etc.

To make an application work, we had to:

1. Install and configure the application server
2. Install a database driver
3. Create a database connection
4. Create a connection pool
5. Build and test our application
6. Deploy our application and its dependency on the application server (WAR or EAR).

Spring Boot has collapsed all the above steps into just a few. How?

Spring Boot **generates a self-executable fat JAR** where all the dependencies are packaged inside the JAR (including the container).

> We don't deploy the Spring Boot application to the application container or the web container.

To verify it, let’s unzip the generated fat JAR and navigate to the BOOT-INF/lib directory. We will find two JAR,s tomcat-embed-coreandtomcat-embed-websocket`.

#### Advantage of fat JAR

What's so special about having a packaged container?  
 The answer is on-demand scalability.  
 We can scale up or scale down the application to any number of instances whenever needed.

We could never do that with the conventional approach!

#### How a fat JAR is generated

Spring Boot Maven plugin ensures that all dependencies are zipped into an output JAR, including the container.  
 All this is taken care of by one Maven plugin.

        <plugin>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-maven-plugin</artifactId>
        </plugin>

> Generated spring boot JAR is called Fat JAR or Uber JAR.

Once you have the fat JAR, it can be run on any system where Java virtual machine is available using the below command:

        Java -jar <SpringBootAppName.jar>

## Key takeaways

- There is no need to deploy the Spring Boot application to heavy containers.
- Spring Boot generates an executable far JAR.
- The spring-boot-maven-plugin is responsible for the generation of the fat JAR.
- We only need JVM to run the Spring Boot JAR.