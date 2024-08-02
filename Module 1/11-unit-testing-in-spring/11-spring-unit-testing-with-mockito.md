# Spring Unit Testing with Mockito

Let's rewrite the unit test by mocking the dependency using Mockito.

> We'll cover the following:
>
> - mockito-core dependency
> - Mocking an interface

The RecommenderImplementation class has a dependency on the Filter interface.  
 Ideally, when writing unit tests, we should only focus on the class under test and mock the dependencies instead of initializing the dependency.  
 We will now write a test for the same class using Mockito.

## mocktito-core dependency

To test using the Mockito framework, we need the mockito-core dependency, which can be added to pom.xml as follows:

        <dependency>
            <groupId>org.mockito</groupId>
            <artifactId>mockito-core</artifactId>
            <scope>test</scope>
        </dependency>

Note that **this dependency is automatically available if springboot-starter-test is included** in the pom.xml file.

We are testing the RecommenderImplementation class that depends on the Filter interface and gets data from it by calling the getRecommendations() method of the interface.  
 This is not good practice because we are not testing the interface right now.  
 We should not be calling the interface method when testing our class.

        public String[] recommendMovies(String movie) {
            // call interface method
            String[] results = filter.getRecommendations(movie);
            // rank the recommendations based
            return results;
        }

The getRecommendations() method has hardcoded return values, but in reality, this is something that changes based on the input string.  
 If the functionality of the method changes or if another 3rd party implementation of the interface is used, our unit test will start failing. We do not want this kind of dependency for our unit test.

The same scenario can be applied when a class in the business layer calls a method of a class in the data layer.  
For unit tests, we should not go across layers.

## Mocking an interface

Mockito can be used when we want to test the interface with different kinds of data.  
 We will mock the interface using @Mock annotation as follows:

        @Mock
        private Filter mockFilter;

This mockFilter can be injected in the bean using the @InjectMocks annotation.

        @InjectMocks
        private RecommenderImplementation recommenderImpl;

We need the @ExtendWith annotation with MockitoExtension.class to enable the use of the annotations mentioned above.

Now we are in a position to mock the results of the getRecommendations() method using the when() and thenReturn() methods on the mock object.  
 We will mock the case when no recommendation is found.

        when(mockFilter.getRecommendations("Finding Dory")).thenReturn(new String[] {});

Here, we are intercepting the call to getRecommendations() and returning a hard-coded result without calling the interface method.

The test for a case when no recommendation is returned is given below:

        @Test
        void testRecommendMovies_noRecommendationsFound() {
            when(mockFilter.getRecommendations("Finding Dory")).thenReturn(new String[] {});
            assertArrayEquals(new String[] {}, recommenderImpl.recommendMovies("Findng Dory"));
        }

Because the mock returned an empty array, the output of the recommendMovies() method will also be an empty array.  
 The code is given in the code widget below.

Using Mockito, the tests take less time as there is no need to load the application context. Whenever possible, try to avoid using Spring in unit testing as it makes the turnaround time longer.  
 Spring also provides annotations for testing a particular layer like @WebMvcTest and @DataJpaTest that loads only a part of the context and mock the rest of the dependencies.
