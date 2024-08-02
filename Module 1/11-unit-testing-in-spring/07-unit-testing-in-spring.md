# Unit Testing in Spring

Learn how to write two simple unit tests using the JUnit framework to test a method.

> We'll cover the following:
>
> - Creating a test file
> - Initializing the bean
>   - Using assertArrayEquals() to check the output

When writing tests, the foremost thing to remember is that the test code should always be separate from the production code.  
 We will write all our tests in src/test/java.  
 This will ensure that the test code is never part of the deployable jar or war and stays in our repository.

We will now write unit tests for the RecommenderImplementation class. The code of the class is reproduced below:

        @Component
        public class RecommenderImplementation {

            @Autowired
            private Filter filter;

            public RecommenderImplementation(Filter filter) {
                super();
                this.filter = filter;
            }

            //use a filter to find recommendations
            public String[] recommendMovies (String movie) {
                String[] results = filter.getRecommendations(movie);
                return results;
            }
        }

This class has a dependency on the Filter interface, which has two implementations, ContentBasedFilter and CollaborativeFilter.  
 Autowiring of the Filter is done by type using the @Primary annotation.

Both implementations of the interface are reproduced below. We have used hardcoded return values in both classes.

        @Component
        @Primary
        public class CollaborativeFilter implements Filter{
            public String[] getRecommendations(String movie) {
                //logic of collaborative filter
                return new String[] {"Finding Nemo", "Ice Age", "Toy Story"};
                }
        }

Let's implement the ContentBasedFilter interface:

        @Component
        public class ContentBasedFilter implements Filter{
            public String[] getRecommendations(String movie) {
                //implement logic of content based filter
                return new String[] {"Happy Feet", "Ice Age", "Shark Tale"};
                }
        }

## Creating a test file

We will begin by creating a JUnit test case in src/test/java.  
To create a unit test for the RecommenderImplementation class, we need to match the same package structure in the test file.  
The test is called RecommenderImplementationUnitTest.  
 Since we want to test the recommendMovies() method, we will rename the test method to testRecommendMovies().

        class RecommenderImplementationUnitTest {
            @Test
            void testRecommenderMovies() {

            }
        }

Since this test is for the RecommenderImplementation class, we need an object of this class which will be used to call the method being tested (recommendMovies).

The following snippet shows the outline of our unit test:

        class RecommenderImplementationUnitTest {
            @Test
            void testRecommendMovies() {
                //1. Initialize the object
                RecommenderImplementation recommenderImpl;
                //2. Call method on the bean
                String[] actualResult = recommenderImpl.recommendMovies("Finding Dory");
                //3. Check if the result is as expected
            }
        }

## Initializing the bean

To initialize the RecommenderImplementation bean, we will use constructor injection in the test method.  
Since we are initializing the bean with CollaborativeFilter, we will rename the test for readability.

        @Test
        void testRecommendMovies_withCollaborativeFilter() {
            RecommenderImplementation recommenderImpl = new RecommenderImplementation(
                new CollaborativeFilter());
            //2. Call method on the bean
            String[] actualResult = recommenderImpl.recommendMovies("Finding Dory");
            //3. Check if the result is as expected
        }

Having a constructor in the class enables us to initialize the dependency without having to load the Spring Boot application context.  
 When writing code, **field injection should be avoided because then the bean can only be initialized by loading the context**.

#### Using assertArrayEquals() to check the output

Inside the test method, we will call the recommendMovies() method with an input string. The input doesn’t really matter because we have hardcoded the return values.  
 Since our method returns an array, we will use the assertArrayEquals() method to compare the expected values with actual values.

        @Test
        void testRecommendMovies_withCollaborativeFilter() {
            RecommenderImplementation recommenderImpl = new RecommenderImplementation(new CollaborativeFilter());
            String[] actualResult = recommenderImpl.recommendMovies("Finding Dory");
            assertArrayEquals(new String[] {"Finding Nemo", "Ice Age", "Toy Story"}, actualResult);
        }

The test is shown in the code widget below.  
When run, the test succeeds.  
To check if it is actually working, you can either change the values returned by the method or change the expected values in the assertArrayEquals() method.

Since we have two implementations of the Filter interface, we can write another unit test to check the output of the recommendMovies() method when ContentBasedFilter is used as follows:

        @Test
        void testRecommendMovies_withContentBasedFilter() {
            RecommenderImplementation recommenderImpl = new RecommenderImplementation(new ContentBasedFilter());
            assertArrayEquals(new String[] {"Happy Feet", "Ice Age", "Shark Tale"}, recommenderImpl.recommendMovies("Finding Dory"));
        }

Here, we have initialized the RecommenderImplementation object with ContentBasedFilter, which leads to a different output when the recommendMovies() method is called.
