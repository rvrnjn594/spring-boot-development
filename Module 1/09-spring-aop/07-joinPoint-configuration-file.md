## JoinPoint Configuration File

Learn about storing all the pointcut expressions in a separate configuration file, considered best practice in AOP.

> We'll cover the following
>
> - For a specific layer
> - For multiple layers
> - For a bean

Creating aspects to intercept method calls and perform tasks before, after, or around the execution is a repetitive task that repeats the same pointcuts over and over again. In an application with a large number of aspects, this can become cumbersome.

A best practice related to AOP is to have a separate configuration file that defines all the pointcuts. This way, all the pointcut definitions will be in one place. Hence they will be easy to manage. We can then use the definitions in any aspect.

## For a specific layer

We will create a class called JoinPointConfig and define pointcuts using the @pointcut annotation. This annotation will be used on an empty method as follows:

        @Pointcut("execution(* io.datajek.springaop.movierecommenderaop.data.*.*(..))")
        public void dataLayerPointcut() {}

Now, we can use the dataLayerPointcut() method by providing its fully qualified name wherever we want to intercept execution of methods in the data layer.

Previously, we had been using pointcuts as follows:

        @AfterReturning("execution(* io.datajek.springaop.movierecommenderaop.data.*.*(..))")
        public void logAfterExecution(JoinPoint joinPoint) {
            //...
        }

Now, we will use the method that defines this pointcut in the configuration file as follows:

        @AfterReturning("io.datajek.springaop.movierecommenderaop.aspect.JoinPointConfig.dataLayerPointcut()")
        public void logAfterExecution(JoinPoint joinPoint) {
            //...
        }

In the same manner, we can create a pointcut configuration for intercepting method calls in the business layer as follows:

        @Pointcut("execution(* io.datajek.springaop.movierecommenderaop.business.*.*(..))")
        public void businessLayerPointcut() {}

The fully qualified name of businessLayerPointcut() can be used in place of the pointcut definition in AccessCheckAspect without affecting the output as follows:

        @Before("io.datajek.springaop.movierecommenderaop.aspect.JoinPointConfig.businessLayerPointcut()")
        public void before(JoinPoint joinPoint) {
            //. . .
        }

## For multiple layers

We can also combine pointcuts using the AND (&&), (OR) ||, and (NOT) ! operators. The method allLayerPointcut() will intercept calls belonging to either the business layer or the data layer.

        @Pointcut("io.datajek.springaop.movierecommenderaop.aspect.JoinPointConfig.dataLayerPointcut() || " + "io.datajek.springaop.movierecommenderaop.aspect.JoinPointConfig.businessLayerPointcut()")
        public void allLayersPointcut() {}

When this pointcut is used in AccessCheckAspect, four method calls are intercepted.

## For a bean

We can also define a pointcut to intercept calls belonging to a particular bean. Say we want to log the execution of all methods belonging to beans that have the word movie in their name. We can define a pointcut as follows:

        @Pointcut("bean(movie*)")
    	public void movieBeanPointcut() {}

When this pointcut is used in AccessCheckAspect, it will intercept calls from the Movie and MovieRecommenderAopApplication beans.
