# XML Configuration with Java Annotations

Learn how to use a combination of XML context and Java annotations.

> We'll cover the following:
>
> - context:component-scan tag
> - context:component-config tag

> In the last lesson, we removed all Java annotations from our application and used the appContext.xml file to define beans and inject the dependency.  
>  However, if we want to detect beans defined by the @Component annotation and inject the dependencies using @Autowired annotation while using XML context, we can do that too.

In large projects, **_declaring a lot of beans using the bean tag is cumbersome, so annotation-based dependency injection was introduced in Spring 2.5_**.  
 This enabled automatic detection of beans having the @Component annotation.  
 **The context:component-scan tag is used to turn this feature on.**

> For the code example shown in this lesson, we use the lesson14 package from the previous lesson.

## context:component-scan tag

> Right now, we have declared three beans in appContext.xml.  
>  Suppose we want to declare the ContentBasedFilter and CollaborativeFilter beans using the @Component annotation instead of defining them using the bean tag in appContext.xml.
>
> Here's the ContentBasedFilter class:
>
>           @Component
>           pubic class ContentBasedFilter implements Filter {
>               // ..
>           }
>
> Here's the CollaborativeFilter class:
>
>           @Component
>           public class CollaborativeFilter implements Filter {
>               // ..
>           }

Just annotating the classes with @Component isn't enough for Spring to detect them as beans. **We need to trigger a component scan.**  
 In XML context, **the context:component-scan tag is used to activate component scanning.**

To be able **to use this tag, we will define a new schema and provide a shortcut name for it** _as context in appContext.xml_.

**NOTE:** By default, any tag that is used without any namespace (like bean) belongs to the default schema as mentioned here.

In the code shown below, the context namespace is defined and provide the schema location of the namespace.  
 Spring XML configuration for beans and context with schema definitions.

            <beans xmlns="http://www.springframework.org/schema/beans"
                xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
                xmlns:context="http://www.springframework.org/schema/context"
                xsi:schemaLocation="http://www.springframework.org/schema/beans
                http://www.springframework.org/schema/beans/spring-beans.xsd
                http://www.springframework.org/schema/context
                http://www.springframework.org/schema/context/spring-context.xsd">

The above code will replace the existing metadata information in appContext.xml.

Now, we can use the **context:component-scan** tag and remove the bean entries for the classes having @Component annotation from the configuration file.  
 (enabling component scan and defining a bean with property injection)

            <!-- enable component scan -->
            <context:component-scan
                base-package="io.datajek.spring.basics.movierecommendersystem.lesson14" />

            <bean id="recommenderImpl"
                class="io.datajek.spring.basics.movierecommendersystem.lesson14.RecommenderImplementation">
                <property name="filter" ref="collaborativeFilter"/>
            </bean>

Here, we are directing Spring to scan the io.datajek.spring.basics.movierecommendersystem.lesson14 package.  
 By default, **Spring will scan everything marked with @Component as well as the @Controller, @Repository, and @Service annotations.**

> In the main() method, we will print the list of beans and also check if autowiring is taking place.  
>  The recommenderImpl bean is declared in appContext.xml while its dependency is declared using @Component annotation.  
>  (defining the main() function)

            public static void main(String[] args) {
                ClassPathXmlApplicationContext appContext = new ClassPathXmlApplicationContext("appContext.xml");
                //check the beans which have been loaded
                System.out.println("\nBeans loaded:");
                System.out.println(Arrays.toString(appContext.getBeanDefinitionNames()));

                //retrieve bean from the application context
                RecommenderImplementation recommender =
                appContext.getBean("recommenderImpl", RecommenderImplementation.class);

                //print dependency
                System.out.println("\nDependency: " + recommender.getFilter());
                System.out.println();

                appContext.close();
            }

While the application is run, all beans defined using the bean tag and @Component annotation are detected.  
 The bean names are displayed using the getBeanDefinitionNames() method.

As can be seen from the output, the list of beans is quite long.  
 This is because the output shows all the beans in the IOC container. Autowiring is also taking place as expected.

## context:component-config tag
