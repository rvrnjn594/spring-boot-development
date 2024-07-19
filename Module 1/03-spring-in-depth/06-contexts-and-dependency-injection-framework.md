# Contexts and Dependency Injection Framework

Learn how to replace Spring annotations with CDI annotations.

> We'll cover the following:
>
> - @Named
> - @Inject
> - Other CDI annotations

**Contexts and Dependency Injection (CDI) is an interface that standardizes dependency injection for Java EE.**  
 It defines **different annotations for dependency injection** like @Named, @Inject, @Scope, @Singleton, etc.  
 Different CDI implementation frameworks provide support and functionality for these annotations.

@Named is used **to define a bean** and @Inject is used **for autowiring one bean into another.**  
 Spring supports most of the annotations defined by CDI.

> For the code example shown in this lesson, we have created a sub-package called lesson12 inside the package io.datajek.spring.basics.movierecommendersystem.
>
> The package contains MovieRecommenderSystemApplication.java, Filter.java, ContentBasedFilter.java, CollaborativeFilter.java, and RecommenderImplementation.java files.  
>  To be able to use CDI annotations in our Spring application, we need to add a dependency in the pom.xml file as follows:

        <dependency>
            <groupId>javax.inject</groupId>
            <artifactId>javax.inject</artifactId>
            <version>1</version>
        </dependency>

The dependency is automatically downloaded when the pom.xml file is saved and is visible in the Mavan Dependencies folder as **_javax.inject-1.jar_**.  
 This jar lists the annotations defined by CDI.

Now, we can use these annotations in our application.

## @Named

Spring framework provides an implementation for some CDI annotations.

In Spring framework, a bean is declared using the @Component annotation.  
 However, it also supports the @Named annotation. This means we can replace the @Component from the RecommenderImplementation, ContentBasedFilter, and CollaborativeFilter classes and use @Named to declare components.

> Let's have a look at the annotation replacement in the RecommenderImplementation class first:

        import javax.inject.Named;

        @Named
        public class RecommenderImplementation {
            // ...
        }

> Now the ContentBasedFilter class:

        import javax.inject.Named

        @Named
        public class ContentBasedFilter {
            // ..
        }

> Let's do for CollaborativeFilter class now:

        import javax.inject.Named;

        @Named
        public class CollaborativeFilter {
            // ..
        }

## @Inject

Spring has the @Autowired annotation for dependency injection but it **also supports the equivalent CDI annotation, @Inject.**  
 So, the Filter dependency in RecommenderImplementation class declared using @Autowired can be declared using @Inject.

        import javax.inject.Inject;

        @Named
        public class RecommenderImplementation {
            @Inject
            @Qualifier("CF")
            private Filter filter;
            // ..
        }

In the main() method, we will print the beans to verify if the RecommenderImplementation bean is getting detected and the Filter dependency is getting autowired successfully.

        public static void main(String[] args) {
            // ...
            RecommenderImplementation recommender = appContext.getBean(RecommenderImplementation.class);
            System.out.println(recommender);
            System.out.println(recommender.getFilter());
            // ..
        }

When the application is run, the output is the same as when @Component and @Autowired annotations were used.

## Other CDI annotations

**Other annotations provided by CDI are @Qualifier, @Scope, and @Singleton.**

The @Qualifier annotation is used to break ties if two beans of the same type qualify to be injected in a dependency.

@Scope is used to set the scope of the bean, similar to the @Scope annotation in Spring.

The @Singleton annotation is used to explicitly set the scope to singleton in CDI annotation.  
 In Spring, we can specify singleton scope using the @Scope annotation.

**Both Spring and CDI annotations provide the same functionality.**  
 The only difference is that if the application is migrated to another framework, the CDI annotations can still be used, whereas **Spring annotations are specific to the Spring framework.**  
 Thus, CDI annotations are often preferred because CDI is a Java EE standard.

**MovieRecommenderSystemApplication.java**

        package io.datajek.spring.basics.movierecommendersystem.lesson12;
        import java.util.Arrays;
        import org.springframework.boot.SpringApplication;
        import org.springframework.boot.autoconfigure.SpringBootApplication;
        import org.springframework.context.ApplicationContext;

        @SpringBootApplication
        public class MovieRecommenderSystemApplication {
            public static void main(String[] args) {
                //ApplicationContext manages the beans and dependencies
                ApplicationContext appContext = SpringApplication.run(MovieRecommenderSystemApplication.class, args);

                //use ApplicationContext to find which filter is being used
                RecommenderImplementation recommender = appContext.getBean(RecommenderImplementation.class);

                System.out.println(recommender);
                System.out.println(recommender.getFilter());

                //call method to get recommendations
                String[] result = recommender.recommendMovies("Finding Dory");

                //display results
                System.out.println(Arrays.toString(result));
            }
        }

**RecommenderImplementation.java**

        package io.datajek.spring.basics.movierecommendersystem.lesson12;
        import javax.inject.Inject;
        import javax.inject.Named;
        import org.springframework.beans.factory.annotation.Qualifier;

        @Named
        public class RecommenderImplementation {
            @Inject
            @Qualifier("CF")
            private Filter filter;

            public Filter getFilter() {
                return filter;
            }
            //use a filter to find recommendations
            public String [] recommendMovies (String movie) {
                //print the name of interface implementation being used
                System.out.println("\nName of the filter in use: " + filter + "\n");
                String[] results = filter.getRecommendations("Finding Dory");
                return results;
            }
        }

**Filter.java**

        package io.datajek.spring.basics.movierecommendersystem.lesson12;

        public interface Filter {
            public String[] getRecommendations(String movie);
        }

**ContentBasedFilter.java**

        package io.datajek.spring.basics.movierecommendersystem.lesson12;
        import javax.inject.Named;
        import org.springframework.beans.factory.annotation.Qualifier;

        @Named
        @Qualifier("CBF")
        public class ContentBasedFilter implements Filter{
            //getRecommendations takes a movie as input and returns a list of similar movies
            public String[] getRecommendations(String movie) {
                //implement logic of content based filter
                return new String[] {"Happy Feet", "Ice Age", "Shark Tale"};
            }
        }

**CollaborativeFilter.java**

        package io.datajek.spring.basics.movierecommendersystem.lesson12;
        import javax.inject.Named;
        import org.springframework.beans.factory.annotation.Qualifier;

        @Named
        @Qualifier("CF")
        public class CollaborativeFilter implements Filter{
            public String[] getRecommendations(String movie) {
                //logic of collaborative filter
                return new String[] {"Finding Nemo", "Ice Age", "Toy Story"};
            }
        }
