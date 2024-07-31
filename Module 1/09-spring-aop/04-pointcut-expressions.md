# Pointcut Expressions

Learn about the pointcut expressions and gain a deeper understanding of how method calls get intercepted.

> We'll cover the following:
>
> - Intercepting all method calls in a package
> - Intercepting all method calls
> - Intercepting calls using return type
> - Intercepting calls to a specified method
> - Intercepting calls with specific method arguments
> - Combining pointcut expressions

The way pointcuts are defined is important because it decides the method calls that will be intercepted. If we have two packages, we can define which method calls will be intercepted.

If we remove the last part of the package name, all calls will be intercepted.

## Intercepting all method calls in a package

We have the following pointcut expression:

        @Before("execution(* io.datajek.springaop.movierecommenderaop.business.*.*(..))")
        public void before(JoinPoint joinPoint) {
            //intercept a call
            logger.info("Intercepted call before execution of: {}", joinPoint);
            //access check logic
        }

This pointcut intercepted calls belonging to the business package. Since we used \* in place of the class and method names, all calls to methods in the business package were intercepted. If we change the package from business to data, only calls to methods in the data package will be intercepted.

## Intercepting all method calls

If we remove the name of the package (business), the pointcut expression becomes:

        @Before("execution(* io.datajek.springaop.movierecommenderaop..*.*(..))")

This will intercept all calls in the movierecommenderaop package. The MovieRecommenderAopApplication.run() method will also get intercepted because it is in the movierecommenderaop package. All method calls in the business and data layer also get intercepted.

## Intercepting calls using return type

Say we want to intercept calls to all methods that return a String value. This can be done by specifying String as the return type in the pointcut expression as follows:

        @Before("execution(String io.datajek.springaop.movierecommenderaop..*.*(..))")

Here we are looking at calls in all subpackages of the movierecommenderaop package and matching the return type of the method calls to String. If this expression is used, four method calls will be intercepted, two belonging to the business package and two from the data package.

## Intercepting calls to a specific method

If we want to intercept calls to all methods that have the word Filtering in it, we will use the following pointcut expression:

        @Before("execution(String io.datajek.springaop.movierecommenderaop..*.*Filtering(..))")

The wildcard \* used in place of the method name will match all methods that have the word Filtering in it. Calls to contentBasedFiltering() and collaborativeFiltering() will be intercepted. If we change the word Filtering to Filter, no method calls will match our criterion.

## Intercepting calls with specific method arguments

Consider the following pointcut expression:

        @Before("execution(* io.datajek.springaop.movierecommenderaop..*.*(String))")

This pointcut will match method calls having one parameter of String type. We can modify this expression to match all method calls with String as the first argument as follows:

        @Before("execution(*io.datajek.springaop.movierecommenderaop..*.*(String,..))")

## Combining pointcut expressions

The && , || and ! symbols can be used to combine different pointcut expressions.

        @Before("execution(_ io.datajek.springaop.movierecommenderaop.._._Filtering(..)) || execution(String io.datajek.springaop.movierecommenderaop.._.\*(..))")

This pointcut expression will match method calls that have the word Filtering in them as well as those that return a String. In all, four methods will be intercepted, matching either of the two conditions.

All the pointcut expressions discussed above are provided in the code widget below. You can use them or create your own pointcuts and play around with them to see which method calls get intercepted.
