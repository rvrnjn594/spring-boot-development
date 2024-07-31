# After Aspect

Learn how to perform a task after an intercepted method call has been executed and what to do in case it throws an exception.

> We'll cover the following:
>
> - @AfterReturning annotation
> - @AfterThrowing annotation
> - @After annotation

Logging is a cross-cutting concern for which aspects can be created. In this lesson, we will create another aspect that will log the values returned after the methods have been executed.

We will create an aspect called LoggingAspect by creating a Java class and mark it with @Aspect and @Configuration annotations.

        @Aspect
        @Configuration
        public class LoggingAspect {

        }

## @AfterReturning annotation

The LoggingAspect class will have a method LogAfterExecution(), which will print a message if the method is successfully executed.

To tell spring-aop that this method needs to be called after the intercepted method call is executed, we will use the @AfterReturning annotation.

        @AfterReturning("execution(* io.datajek.springaop.movierecommenderaop.data.*.*(..))")
        public void LogAfterExecution(JoinPoint joinPoint) {
            logger.info("Method {} complete", joinPoint);
        }

We can get the return values of the method here by using the returning tag. Now that pointcut is not the only argument in the @AfterReturning annotation, we will use tags to differentiate arguments as follows:

        @AfterReturning(
          value = "execution(* io.datajek.springaop.movierecommenderaop.business.*.*(..))",
          returning = "result")
        public void LogAfterExecution(JoinPoint joinPoint, Object result) {
            logger.info("Method {} returned with: {}", joinPoint, result);
        }

value contains the pointcut expression and returning contains the value that is returned by the executing method, which is stored in result and passed to the LogAfterExecution method. Note that result is of type Object because different methods will have different return types.

We can use result to log the return value of all intercepted methods. If the application is run, two methods from the business layer are intercepted and the return values are also displayed.

## @AfterThrowing annotation

To intercept an exception, another annotation, @AfterThrowing, is used. We can get the result of the exception using the throwing tag.

We will use this pointcut on a method called LogAfterException() as follows:

        @AfterThrowing(
            value = "execution(* io.datajek.springaop.movierecommenderaop.business.*.*(..))",
            throwing = "exception")
            public void LogAfterException(JoinPoint joinPoint, Object exception) {
                    logger.info("Method {} returned with: {}", joinPoint, exception);
            }

This advice will be executed in case there is an exception in any method call from the business layer.

## @After annotation

The @After annotation is a generic annotation that is used in both scenarios, whether the method execution is successful or results in an exception. The method LogAfterMethod() demonstrates the use of this annotation.

        @After("execution(* io.datajek.springaop.movierecommenderaop.business.*.*(..))")
        public void LogAfterMethod(JoinPoint joinPoint) {
            logger.info("After method call {}", joinPoint);
        }

Since @After is a generic annotation, if the application is run, this method also gets executed alongside the LogAfterExecution() method marked with the @AfterReturning annotation.
