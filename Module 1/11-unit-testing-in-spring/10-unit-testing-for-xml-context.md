# Unit Testing for XML Context

Learn how to write a unit test that uses the XML configuration.

> We'll cover the following:
>
> - @ContextConfiguration with locations
> - Creating a test configuration

The @ContextConfiguration annotation is used to load Java as well as XML context. We created an XML application context file for the MovieRecommenderSystemApplication. Following is the XML configuration file:

        <beans xmlns="http://www.springframework.org/schema/beans"
                xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
                xmlns:context="http://www.springframework.org/schema/context"
                xsi:schemaLocation="http://www.springframework.org/schema/beans
                http://www.springframework.org/schema/beans/spring-beans.xsd
                http://www.springframework.org/schema/context
                http://www.springframework.org/schema/context/spring-context.xsd">

            <context:component-scan base-package="io.datajek.spring.basics.movierecommendersystem.lesson14" use-default-filters="false">
            </context:component-scan>
            <!-- enable detection of @Autowired annotation -->
            <context:annotation-config/>

            <bean id="filter" class="io.datajek.spring.basics.movierecommendersystem.lesson14.ContentBasedFilter"/>
            <bean id="filter2" class="io.datajek.spring.basics.movierecommendersystem.lesson14.CollaborativeFilter"/>
            <bean id="recommenderImpl" class="io.datajek.spring.basics.movierecommendersystem.lesson14.RecommenderImplementation"/>
        </beans>

In this file, we have mentioned the package for component scanning using the context:component-scan tag. We have also defined three beans: filter, filter2, and recommenderImpl.

## @ContextConfiguration with locations

We will create a test called RecommenderImplementationXmlConfigTest in src/test/java/ for testing the RecommenderImplementation class.  
 When using Java context, the @ContextConfiguration annotation took classes as an argument.  
 To load the XML configuration, we will provide locations as an argument. Since the config file is in the class path, the location can be given as follows:

        @ContextConfiguration(locations="/appContext.xml")

The above line will load the file appContext.xml that contains the defintion of beans and their dependencies.

The rest of the test will remain the same as done using the Java context:

        @ExtendWith(SpringExtension.class)
        @ContextConfiguration(locations="/appContext.xml")
        class RecommenderImplementationXmlConfigTest {

            @Autowired
            private RecommenderImplementation recommenderImpl;

            @Test
            void testRecommendMovies() {
                assertArrayEquals(new String[] {"Happy Feet", "Ice Age", "Shark Tale"}, recommenderImpl.recommendMovies("Finding Dory"));
            }
        }

## Creating a test configuration

Since we are reading the application context from a file, we can create a separate context for the purpose of testing.  
 All test contexts should be placed in src/test/resources.  
 A test context is useful if we want to override something defined in the context for the purpose of testing.

To create a test context, we first need to create a folder called resources under src/test.  
Next, we will create an XML file testContext.xml in src/test/resources.  
We can define a test context here or import the context from another file and then override it.

The following code shows how to import the application context from src/main/resources:

        <beans xmlns="http://www.springframework.org/schema/beans"
            xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
            xmlns:context="http://www.springframework.org/schema/context"
            xsi:schemaLocation="http://www.springframework.org/schema/beans
            http://www.springframework.org/schema/beans/spring-beans.xsd
            http://www.springframework.org/schema/context
            http://www.springframework.org/schema/context/spring-context.xsd">

            <import resource="classpath:appContext.xml"/>
            <!-- Now you can change anything in the application context and override it -->
            <!-- You can change/delete bean definitions -->
        </beans>

After importing the context, the beans can be defined for the purpose of testing and any bean from the original context can be overridden.

To use the test configuration defined above, we will use testContext.xml as an argument to the @ContextConfiguration annotation.

        @ExtendsWith(SpringExtension.class)
        @ContextConfiguration(location="/testContent.xml")
        class RecommenderImplementationXmlConfigTest {
            // ..
        }

In this way, we can define a separte configuration for the unit test.
