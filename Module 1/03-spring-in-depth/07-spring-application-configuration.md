# Spring Application Configuration

Learn how to configure a Spring application.

> We'll cover the following:
>
> - spring-core dependency
> - spring-context dependency
> - @Configuration
>   > - AnnotationConfigApplicationContext
>   > - @ComponentScan
> - Closing the application context

We created the Movie Recommender System using Spring Boot. Let's look at how it can be run using the core features of Spring.  
 We will remove the dependencies autoconfigured by Spring Boot and replace them with the dependencies needed for a Spring project.

> For the code example shown in this lesson, we have created a sub-package called lesson13 inside the package io.datajek.spring.basics.movierecommendersystem.
>
> The package contains MovieRecommenderSystemApplication.java, Filter.java, ContentBasedFilter.java, CollaborativeFilter.java, and RecommenderImplementation.java files.

## spring-core dependency

spring-core provides the fundamental features of Spring framework like **dependency injection and Inversion of Control.**

Since we created our application using Spring Initializer, it has the spring-boot-starter dependency in the pom.xml file.  
 This dependency brings in Spring Boot functionality.

> In this lesson, we will replace it with spring-core.
>
>               <!-- remove the following dependency:
>                   <dependency>
>                   <groupId>org.springframework.boot</groupId>
>                       <artifactId>spring-boot-starter</artifactId>
>                   </dependency>
>               -->
>               <!-- replace it with this dependency: -->
>               <dependency>
>                   <groupId>org.springframework</groupId>
>                   <artifactId>spring-core</artifactId>
>               </dependency>

**spring-core defines the bean factory and forms the base of the Spring framework.**

## spring-context dependency

**To be able to use ApplicationContext, we need to add another dependency** called spring-context as follows:

            <dependency>
                <groupId>org.springframework</groupId>
                <artifactId>spring-context</artifactId>
            </dependency>

By removing the spring-boot-starter dependency, the @SpringBootApplication annotation will no longer be available when we run the Java application.  
 However, this annotation can be used in test scope because we still have the spring-boot-starter-test dependency in pom.xml.

## @Configuration

@SpringBootApplication cannot be used in the Java application file anymore as we have replaced the spring-boot-starter dependency with spring-core in step 1.  
 This annotation defined the application configuration in Spring Boot.

In the Java realm, we use @Configuration and import the org.springframework.context.annotation.Configuration jar.

## AnnotationConfigApplicationContext

If we run the application now, the following compilation error is encountered:

**Note:** Unresolved compilation problem: SpringApplication cannot be resolved.

The SpringApplication class creates the application context. It belongs to the org.springframework-boot package.  
 When using the spring-core, the application context is created using AnnotationConfigApplicationContext class as follows:

        // ApplicationContext manages the beans and dependencies

        /* Change this:
            ApplicationContext appContext = SpringApplication.run(MovieRecommenderSystemApplication.class, args)
        */
        // to this
        AnnotationConfigApplicationContext appContext = new AnnotationConfigApplicationContext(MovieRecommenderSystemApplication.class)

## @ComponentScan

If we try to run the application now, it throws the **NoSuchBeanDefinition** exception, which means that the application context is unable to locate beans declared using @Component.  
 We need to help it in component scanning by providing the @ComponentScan annotation on the MovieRecommenderSystemApplication class.

Look at the following image:

    ![@SpringBootApplication combines three annotations](./images/7-1-@SpringBootApplication%20combines%20three%20annotations.png)

The @SpringBootApplication annotation performs multiple tasks.  
 Now that it has been removed, we need to explicitly use the @ComponentScan annotation to guide Spring to the package which contains the beans.

After making these changes, we are able to run the same application without using Spring Boot!

## Closing the application context

**Spring Boot automatically closes the application context in the end**, but now we need to close the context as follows:

        // close the application context
        appContext.close();

Another way is to use a try catch block around the statement creating appContext.  
 In this way, any error will result in the context being automatically closed.

        try (AnnotationConfigApplicationContext appContext =
            new AnnotationConfigApplicationContext (MovieRecommenderSystemApplication.class)) {
                // ...
            }

The changes that we made in this lesson that enabled us to run a Spring Boot application as a basic Spring application are as follows:

- Removing the spring-boot starter dependency and replacing it with spring-core and spring-context.
- Replacing @SpringBootApplication with @Configuration and @ComponentScan.
- Replacing the SpringApplication class with the AnnotationConfigApplicationContext class.

        package io.datajek.spring.basics.movierecommendersystem.lesson13;
        import java.util.Arrays;
        import org.springframework.context.annotation.AnnotationConfigApplicationContext;
        import org.springframework.context.annotation.ComponentScan;
        import org.springframework.context.annotation.Configuration;

        //@SpringBootApplication
        @Configuration
        @ComponentScan
        public class MovieRecommenderSystemApplication {
            public static void main(String[] args) {
                //ApplicationContext manages the beans and dependencies
                //ApplicationContext appContext = SpringApplication.run(MovieRecommenderSystemApplication.class, args);
                AnnotationConfigApplicationContext appContext = new AnnotationConfigApplicationContext(MovieRecommenderSystemApplication.class);

                //use ApplicationContext to find which filter is being used
                RecommenderImplementation recommender = appContext.getBean(RecommenderImplementation.class);

                //call method to get recommendations
                String[] result = recommender.recommendMovies("Finding Dory");

                //display results
                System.out.println(Arrays.toString(result));
                appContext.close();
            }
        }
