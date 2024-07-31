## Defining an Aspect

Learn how to define an aspect and intercept message calls.

> We'll cover the following:
>
> - Aspect
> - Advice
> - Pointcut expression
> - Joinpoint

In this lesson, we will define an aspect to intercept all the calls to the business layer and log their output. We will create a number of aspects and place all of them in a separate package.

When we intercept method calls, we have the option to perform tasks before the method is executed as well as afterwards. To define an aspect for a cross-cutting concern, we will perform the following steps:

- Define aspect class.
- Write methods containing the advice to be executed when method calls are intercepted.
- Write pointcut expressions for intercepting method calls.

## Aspect

Suppose we want to intercept method calls to check if the user has access before the method gets executed. Access check is a cross-cutting concern and instead of implementing it in every method, we can write it as an aspect.

We will create a class called AccessCheckAspect and place it in the aspect package. To establish that this is a configuration class, we will use the @Configuration annotation. We will also add the @Aspect annotation to establish that this class is related to AOP.

        @Aspect
        @Configuration
        public class AccessCheckAspect {

        }

## Advice

The next step is to define a method that contains the logic of the steps that need to be carried out when a method call gets intercepted. We will create a method called userAccess() and print a message as follows:

        @Aspect
        @Configuration
        public class AccessCheckAspect {
            private Logger logger = LoggerFactory.getLogger(this.getClass());

            public void userAccess(JoinPoint joinPoint) {
                logger.info("Intercepted method call");
            }
        }

## Pointcut expression

User access needs to be checked before a method gets executed. We need the @Before annotation on our method. It ensures that the advice is run before the method is executed.

@Before needs an argument which specifies the method calls that will be intercepted. This is called the pointcut. Pointcuts are defined in the following format:

        execution(* PACKAGE.*.*(..))

The pointcut expression starts with a key word called a designator, which tells Spring AOP what to match. execution is the primary designator which matches method execution joinpoints.

- The first \* in the expression corresponds to the return type. \* means any return type.
- Then comes the package name followed by class and method names.
- The first \* after package means any class and the second \* means any method. Instead of \*, we could specify the class name and method name to make the pointcut expression specific.
- Lastly, parentheses correspond to arguments. (..) means any kind of argument.

Suppose we want to intercept calls to methods belonging to the business package. The pointcut expression, in this case, will be:

        @Before("execution(* io.datajek.springaop.movierecommenderaop.business.*.*(..))")

If we use this pointcut expression and run the application, the message in the method will be printed twice, indicating that two method calls have been intercepted.

The userAccess() method is invoked before the actual method. This method contains the logic for checking user access, which is not shown.

## Joinpoint

To find out which method calls have been intercepted, we will use a join point as an argument to the method. The joinpoint contains the name of the method that is intercepted. We can use the joinpoint to print the name of the method as follows:

        @Before("execution(* io.datajek.springaop.movierecommenderaop.business.*.*(..))")
        public void userAccess(JoinPoint joinPoint) {
            logger.info("Intercepted call before execution: {}", joinPoint);
        }

---

        package io.datajek.springaop.movierecommenderaop.data;

        import org.springframework.stereotype.Repository;

        @Repository
        public class User {

            public String getUserDetails() {
                return "user details";
            }
        }


        package io.datajek.springaop.movierecommenderaop.data;
        import org.springframework.stereotype.Repository;

        @Repository
        public class Movie {

            public String getMovieDetails() {
                return "movie details";
            }
        }


        package io.datajek.springaop.movierecommenderaop.business;

        import org.springframework.beans.factory.annotation.Autowired;
        import org.springframework.stereotype.Service;

        import io.datajek.springaop.movierecommenderaop.data.User;

        @Service
        public class FilteringTechnique2 {

            @Autowired
            private User user;

            public String collaborativeFiltering() {
                String userDetails =  user.getUserDetails();
                return userDetails;
            }
        }


        package io.datajek.springaop.movierecommenderaop.business;

        import org.springframework.beans.factory.annotation.Autowired;
        import org.springframework.stereotype.Service;

        import io.datajek.springaop.movierecommenderaop.data.Movie;

        @Service
        public class FilteringTechnique1 {

            @Autowired
            private Movie movie;

            public String contentBasedFiltering() {
                String movieDetails = movie.getMovieDetails();
                return movieDetails;
            }
        }


        package io.datajek.springaop.movierecommenderaop.aspect;

        import org.aspectj.lang.JoinPoint;
        import org.aspectj.lang.annotation.Aspect;
        import org.aspectj.lang.annotation.Before;
        import org.slf4j.Logger;
        import org.slf4j.LoggerFactory;
        import org.springframework.context.annotation.Configuration;

        @Aspect
        @Configuration
        public class AccessCheckAspect {

            private Logger logger = LoggerFactory.getLogger(this.getClass());

            @Before("execution(* io.datajek.springaop.movierecommenderaop.business.*.*(..))")
            public void before(JoinPoint joinPoint) {
                //	logger.info("Intercepted method call");
                logger.info("Intercepted call before execution of: {}", joinPoint);

                //access check logic
            }
        }


        package io.datajek.springaop.movierecommenderaop;

        import org.slf4j.Logger;
        import org.slf4j.LoggerFactory;
        import org.springframework.beans.factory.annotation.Autowired;
        import org.springframework.boot.CommandLineRunner;
        import org.springframework.boot.SpringApplication;
        import org.springframework.boot.autoconfigure.SpringBootApplication;

        import io.datajek.springaop.movierecommenderaop.business.FilteringTechnique1;
        import io.datajek.springaop.movierecommenderaop.business.FilteringTechnique2;

        @SpringBootApplication
        public class MovieRecommenderAopApplication implements CommandLineRunner{

            private Logger logger = LoggerFactory.getLogger(this.getClass());

            @Autowired
            private FilteringTechnique1 filter1;

            @Autowired
            private FilteringTechnique2 filter2;

            public static void main(String[] args) {
                SpringApplication.run(MovieRecommenderAopApplication.class, args);
            }

            @Override
            public void run(String... args) throws Exception {
                logger.info("{}",filter1.contentBasedFiltering());
                logger.info("{}",filter2.collaborativeFiltering());

            }
        }

It can be seen that the intercepted method calls are FilteringTechnique1.contentBasedFiltering() and FilteringTechnique2.collaborativeFiltering().

The pointcut that we defined is applicable on all methods in the business layer. We can also change the pointcut to intercept methods calls in the data layer:

@Before("execution(_ io.datajek.springaop.movierecommenderaop.data._.\*(..))")

In this case the method calls Movie.getMovieDetails() and User.getUserDetails() will be intercepted.

---
