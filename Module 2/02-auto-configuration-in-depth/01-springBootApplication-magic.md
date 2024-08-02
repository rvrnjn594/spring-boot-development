# @SpringBootApplication Magic

Discover the magic of @SpringBootApplication annotation and how it makes Spring Boot what it's known for.

> We'll cover the following
>
> - Application without @Springbootapplication annotation
> - Uncover @SpringBootApplication
> - @SpringBootConfiguration
> - @ComponentScan
> - @EnableAutoConfiguration
> - Curiosity questions
> - Key takeaways

Spring Boot has revolutionized how Java applications are created. Perhaps the **most important annotation is @SpringBootApplication**. Let’s dive in and find out why.

## Application without @Springbootapplication annotation#

Let’s do one experiment:  
 remove the @SpringBootApplication annotation from the below elite club application main class and see what happens.

As expected, **the application is unable to start.** We should see the below error.

        Caused by: org.springframework.context.ApplicationContextException: Unable to start ServletWebServerApplicationContext due to missing ServletWebServerFactory bean.

It’s evident that this annotation has special significance, which is why it’s applied to the class with the main method.

## Uncover @SpringBootApplication

If we decompile the source of this annotation, we can see the following snippets.

        @SpringBootConfiguration
        @EnableAutoConfiguration
        @ComponentScan(
            excludeFilters = {@Filter(type = FilterType.CUSTOM, classes = {TypeExcludeFilter.class}
        ), @Filter(
            type = FilterType.CUSTOM,
            classes = {AutoConfigurationExcludeFilter.class}
        )}
        )
        public @interface SpringBootApplication {

@SpringBootApplication is a combination of the following three annotations.

- @SpringBootConfiguration
- @EnableAutoConfiguration
- @ComponentScan

## @SpringBootConfiguration

This annotation is the **same as @Configuration annotation**.  
It **designates the class as a configuration class**.

## @ComponentScan

This **annotation enables component scanning**.  
Any **bean available in the classpath will be scanned** by the Spring Framework and **registered in the IoC container**.  
 For example, classes marked with @Component, @RestController, @Service will be registered with the Spring IoC container.

## @EnableAutoConfiguration

This **annotation enables the magical auto-configuration feature of Spring Boot**.  
 Now we know why the application failed during startup. Even though we have a starter-web dependency available in the classpath, the auto-configuration was turned off due to the missing @SpringBootApplication annotation.  
 As a result of this, **the framework failed to configure the DispatcherServlet and other required beans**.

---

Question: Do we need to use all three features of the @SpringBootApplication annotation?

Answer: None of these features are mandatory, and we may choose to replace this single annotation with any of the features it enables.  
 For instance, we may not want to use component scan in our application, which is perfectly fine.

Spring Boot does not force us to use all three features.

Question: How can we use only a few of the three features in our application?

Answer: On the same main class, we will remove @SpringBootApplication and add annotations for the feature we want to enable.

        @SpringBootConfiguration
        @EnableAutoConfiguration
        public class EliteClubApplication {

            public static void main(String[] args) {
                    SpringApplication.run(Application.class, args);
            }
        }

In this example, the application is just like any other Spring Boot application, except that component scanning is disabled.

Question: What if we remove the @EnableAutoConfiguration annotation?

Answer: We should not use Spring Boot if we have to disable this annotation.

This is because **removing this annotation will effectively downgrade our application to a conventional Springapplication**.  
The entire magic of Spring Boot lies in this annotation.

---

## Key takeaways

- @SpringBootApplication packs a lot of functionality. We can just add one annotation to enable all the features of the Spring Boot.
- Spring Boot does not force us to use all three features of this annotation.
- We are free to use any of the three features.

This is what makes Spring and Spring Boot amazing. It never forces us to use any specific feature.  
The framework is designed to be fully pluggable and customizable.  
 It clearly shows the proper use of the open-close principle in practice.
