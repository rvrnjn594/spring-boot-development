## Around Aspect

Learn how to wrap an aspect around an intercepted method call with the @Around annotation.

> We'll cover the following
>
> - @Around annotation
>   - Example

## @Around annotation

There are aspects that are executed before and after the intercepted method calls. This lesson looks at another type of aspect, the around aspect, which is executed around the intercepted method call. This kind of aspect is useful if we want to perform a task before the intercepted method starts execution and after the method has returned.

#### Example

A good example of an around aspect is measuring the time taken by the method call to execute. We can note the time when the method call is intercepted, then allow the intercepted method to execute, and note the time after the method returns. Instead of having two separate annotations, @Before and @After, we can accomplish this task using the more advanced @Around annotation.

We will create a class called ExecutionTimeAspect. To mark it as an aspect and a configuration, we will use the @Aspect and @Configuration annotations as follows:

        @Aspect
        @Configuration
        public class ExecutionTimeAspect {

        }

In the ExecutionTimeAspect class, we will create a method called calculateExecutionTime(). The parameter type of this method will be ProceedingJoinPoint instead of JoinPoint, which we have used with methods marked with @Before and @After annotations. ProceedingJoinPoint allows the continuation of the execution. This method will return an Object that contains the values returned after the execution of the intercepted method call. The proceed() method of ProceedingJoinPoint should either be surrounded by a try catch block or should include a throws declaration with the method definition.

The logic of this method is shown below:

        public Object calculateExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
            //note start time
            long startTime = System.currentTimeMillis();

            //allow method call to execute
            Object returnValue = joinPoint.proceed();

            //time taken = end time - start time
            long timeTaken = System.currentTimeMillis() - startTime;

            logger.info("Time taken by {} to complete execution is: {}", joinPoint, timeTaken);
            return returnValue;
        }

The variable startTime notes the time when the call is intercepted. The proceed() method allows the intercepted method call to execute. The variable returnValue contains the values returned by the method. After the method call has returned, we calculate the execution time in a variable timeTaken.

Now, we will use the @Around annotation to define a pointcut for method calls for which we want the execution time to be tracked. If we want the time of all methods to be tracked, the following pointcut expression will be used:

        @Around("execution(* io.datajek.springaop.movierecommenderaop..*.*(..))")

When the application given in the code widget below is executed, it will print the execution time of every method.
