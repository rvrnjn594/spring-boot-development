# Unit Testing for Java Context

Learn how to write a unit test using the Spring test features.

> We'll cover the following:
>
> - spring-test and junit dependency
>   - @ContextConfiguration

## spring-test and junit dependency

We will start by replacing the springboot-starter-test dependency - which is automatically included in a Spring Boot project - with the **spring-test dependency along with the junit-jupiter-engine dependency**.

        <dependency>
            <groupId>org.springframework</groupId>
            <artifactId>spring-test</artifactId>
            <scope>test</scope>
        </dependency>

        <dependency>
            <groupId>org.junit.jupiter</groupId>
            <artifactId>junit-jupiter-engine</artifactId>
            <scope>test</scope>
        </dependency>

spring-test is the module in the Spring framework that helps us write unit tests.  
 Since the project was built using Spring Boot, removing the springboot-starter-test dependency leads to compilation errors due to usage of the @SpringBootTest annotation in src/test/java.  
 To remove the errors, you can comment out the @SpringBootTest annotation.

We will create a JUnit test case in src/test/java/ called RecommenderImplementationSpringTest.  
 The package structure of the test class must match that of the class being tested. The test class is shown:

        package io.datajek.spring.basics.movierecommendersystem.lesson4;

        class RecommenderImplementationSpringTest {
            @Test
            void testRecommendMovies() {
                //...
            }
        }

#### @ContextConfiguration

@SpringBootTest can be used to launch the context and get the bean in the test class when using Spring Boot.  
 To load the context using Spring, we will use @ContextConfiguration.  
 This loads only part of the configuration and is thus more suitable for unit testing.  
 Since our configuration is in the class MovieRecommenderSystemApplication, we will pass that as an argument to the @ContextConfiguration annotation.  
 This class is where the context is present.

After the context has been defined, we need to run it. This is done by using the @ExtendWith annotation with SpringExtension.class as its argument.  
SpringExtension provides a bridge between Spring and JUnit.

        // load context and run it
        @ExtendWith(SpringExtension.class)
        @ContextConfiguration(classes = MovieRecommenderSystemApplication.class)
        class RecommenderImplementationSpringTest {

        }

The rest of the test is the same as the test created using Spring Boot.  
The @Autowired annotation is used to get the RecommenderImplementation bean from the context once it is launched and loaded.  
Then the assertArrayEquals() method is used to test whether the actual output of recommendMovies() is the same as the expected output.

        //load context
        @ExtendWith(SpringExtension.class)
        @ContextConfiguration(classes=MovieRecommenderSystemApplication.class)
        class RecommenderImplementationSpringTest {

            @Autowired
            private RecommenderImplementation recommenderImpl;

            @Test
            public void testRecommendMovies() {
                assertArrayEquals(new String[] {"Finding Nemo", "Ice Age", "Toy Story"}, recommenderImpl.recommendMovies("Finding Dory"));
            }
        }

A class which does not provide a constructor or setter injection for its dependencies can only be tested by loading the Spring context.  
 However, if a constructor is present (as in the case of RecommenderImplementation class shown above), the object can be created without loading the context and it can also be mocked.
