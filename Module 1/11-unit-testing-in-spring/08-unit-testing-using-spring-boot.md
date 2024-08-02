# Unit Testing Using Spring Boot

Learn how to write a test using the @SpringBootTest annotation and why it should not be used for unit testing.

> We'll cover the following:
>
> - spring-boot-starter-test dependency
> - Launching application context and autowiring bean
> - When to use @SpringBootTest

When we create an application using Spring Boot, we get the **springboot-starter-test dependency that comes with Mockito and AssertJ as testing libraries.**  
 We also automatically get a test file with the @SpringBootTest annotation.

Unit testing should not be done using Spring Boot.  
The main purpose of unit testing is to test a method or class.  
 However, **@SpringBootTest loads the entire context, which makes the test lengthy and defeats the purpose of unit testing**.  
 **This feature of Spring Boot should be used in integration testing** where we test across multiple layers.

The purpose of this lesson is only to show how a unit test can be written using the @SpringBootTest annotation.

## spring-boot-starter-test dependency

The pom.xml file if a Spring Boot project has the following dependency on spring-boot-starter-test.

        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <scope>test</scope>
        </dependency>

We will begin by creating a JUnit test case in src/test/java, taking care that we match the package structure of the class being tested with the test file.  
 The test is called RecommenderImplementationSpringBootTest as we want to show how to use test features provided by Spring Boot. We will rename the test method testRecommendMovies().

        package io.datajek.spring.basics.movierecommendersystem.lesson4;

        import org.springframework.boot.test.context.SpringBootTest;
        @SpringBootTest
        class RecommenderImplementationSpringBootTest {
            @Test
            void testRecommendMovies() {
                //...
            }
        }

Since this test is for the RecommenderImplementation class, we will begin by defining an object to this class.

        class RecommenderImpplementationSpringBootTest {
            private RecommenderImplementation recommenderImpl;

            @Test
            void testRecommendMovies() {
                // ..
            }
        }

In this lesson, we will learn how the bean can be obtained from the application context.  
The following snippet shows the outline of our unit test:

        //1. Launch context
        class RecommenderImplementationSpringBootTest {

            //2. Load bean from context
            private RecommenderImplementation recommenderImpl;

            @Test
            void testRecommendMovies() {
                //3. Call method on the bean
                //4. Check if the result is as expected
            }
        }

## Launching application context and autowiring bean

@SpringBootTest launches the entire application context, which means that all beans get loaded.  
 Thus, we can simply use the @Autowire annotation to get the RecommenderImplementation bean.

        @Autowired
        private RecommenderImplementation recommenderImpl;

The **@Autowired will automatically find and inject the dependency rather than us having to manually do it** (like in the previous lesson where we used constructor injection to initialize the bean).

Inside the test method, we will call the recommendMovies() method with an input string and then use the assertArrayEquals() method to compare the expected and actual values.

        @Test
        void testRecommendMovies() {
            assertArrayEquals(new String[] {"Finding Nemo", "Ice Age"}, recommenderImpl.recommendMovies("Finding Dory"));
        }

This simple test class is shown in the code widget below.  
You can compare the running time of this test with the running time of the test created when using the Spring application.

This **unit test will take longer because we are launching the application context to run the test**.

## When to use @SpringBootTest

The **value of @SpringBootTest is in testing the whole application** because _it loads the complete context like in the production environment_.  
 For a unit test, there is no need to load the complete context.  
 In fact, loading the entire context will have an impact on performance since unit tests should only take a few milliseconds to run.
