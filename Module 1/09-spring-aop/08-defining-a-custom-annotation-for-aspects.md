## Defining a Custom Annotation for Aspects

Learn how to create our own annotation and use it in aspects to intercept method calls.

> We'll cover the following
>
> - A custom annotation
> - Example

## A custom annotation

An aspect can be used to track the execution time of a method that intercepts calls to a particular layer. Another approach could be to define a custom annotation on any method for which we want to track the execution time. spring-aop allows us to create our own annotations and define aspects to implement them.

## Example

Suppose we want to call our annotation @MeasureTime. We will create this annotation in the same folder as the other aspects. This creates an interface:

        package io.datajek.springaop.movierecommenderaop.aspect;

        public @interface MeasureTime {

        }

We will restrict the use of this annotation to methods only. This can be achieved using the @Target annotation, with ElementType set to METHOD. Other options include class, package, field, etc.

        @Target(ElementType.METHOD)

We would also like the annotation information to be available at runtime. We will use the @Retention annotation to define a retention policy as follows:

        @Retention(RetentionPolicy.RUNTIME)

Now that we have defined our annotation, we can add it to the JoinPointConfig file and create a pointcut as follows:

        @Pointcut("@annotation(io.datajek.springaop.movierecommenderaop.aspect.MeasureTime)")
        public void measureTimeAnnotation() {}

We can now use this pointcut to calculate the execution time of only chosen methods. Previously, the ExecutionTimeAspect looked like this:

        public class ExecutionTimeAspect {
            private Logger logger = LoggerFactory.getLogger(this.getClass());

            @Around("io.datajek.springaop.movierecommenderaop.aspect.JoinPointConfig.businessLayerPointcut()")
            public Object calculateExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
                //...
            }
        }

Instead of tracking the time of business layer methods, we will intercept methods using the @MeasureTime annotation:

        public class ExecutionTimeAspect {
            private Logger logger = LoggerFactory.getLogger(this.getClass());
            @Around("io.datajek.springaop.movierecommenderaop.aspect.JoinPointConfig.measureTimeAnnotation()")
            public Object calculateExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
                //...
            }
        }

Since we haven’t used the annotation anywhere in the code yet, no call will be intercepted. Suppose we want to track the time for the contentBasedFiltering() method of the FilteringTechnique1 class and the getMovieDetails() method of the Movie class. We will use our custom annotation on these methods:

        @MeasureTime
        public String contentBasedFiltering() {
            String movieDetails = movie.getMovieDetails();
            return movieDetails;
        }

Let's try it for the second one:

        @MeasureTime
        public String getMovieDetails() {
            return "movie details";
        }

If the application is run after adding @MeasureTime, calls to both these methods will be intercepted, and the execution time will be measured.
