# Writing a Unit Test

Learn how to write a unit test with JUnit 5.

> We'll cover the following:
>
> - Creating a test class
>   - @Test annotation
>   - fail() method
> - Writing a unit test
> - Assertions
> - Creating multiple test cases

To understand how unit testing is done with JUnit, we will create a simple class ArrayMethods.  
We will write some methods in this class and then test those using JUnit.

Our class has methods to find the index of a given number in the array and a method to search if a number is present in the array.

        public class ArrayMethods {
            int findIndex(int[] array, int number) {
                //...
                return index;
            }

            boolean searchForNumber(int[] array, int numberToSearchFor) {
                boolean found = false;
                //...
                return found;
            }
        }

> The findIndex() method takes an integer array and a number and returns the index at which the number is found in the array.
>
> The searchArray() method accepts an array of integers and a number to search for and returns true if the number is found.
>
> JUnit framework allows us to call the findIndex() method with an array containing three integers, say 8, 4, and 5, and look for 4 in the array.  
>  The index where 4 is found, (1) is returned.

To test the findIndex() method, we will create a unit test.

The **tests should always be separate from the source code**.  
 They should be in a different folder **so that they are not deployed to production along with the source code**.

There are two ways of creating a class for testing purposes.

- One is to manually create a class in the src/test/java folder and write tests.
- The other is to use the IDE support.  
   Almost all IDEs have support for JUnit 5. So instead of us creating the test class, the IDE will do that for us and place it in src/test/java.

## Creating a test class

For the class that we want to test, a JUnit test can be created in the same way that a class or interface is created.  
We will call the test ArrayMethodsTest.  
 Conventionally, the name of the test is the name of the class being tested with the word Test appended to it.  
 This is for convenience, so that we know which class we are testing.  
 It also **lets the developer use the package visibility features of Java**, where protected methods that are not visible to classes outside the package become visible to the test class.

#### @Test annotation

The @Test annotation is present before the test method.  
 If signifies that the method is a unit test. When JUnit encounters this annotation, it knows it has to run the test.  
 So if we click on the project and choose "Run as JUnit test", JUnit will know which methods it has to run.

#### fail() method

By default, the test method contains the fail() method.  
 If the test is run, it will fail because of the fail() method and the text message **Not implemented yet** will be displayed.

        @Test
        public void test() {
            fail("Not implemented yet");
        }

If the test is run after removing the fail() method, it will succeed.  
 JUnit works by checking if any of the tests have failed.  
 The test will be a success unless any checks fail.  
 In the JUnit world, absence of failure is success.

In IDEs, a colored bar is the visual representation of the status of the execution.  
 A green bar shows up in case a test succeeds, and a red one indicates a failing test.

## Writing a unit test

Every unit test involves a few simple steps.  
 First and foremost, we need to create an object of the class under test and use the object to call the method that we want to test. Then, we set up the inputs which constitute a test scenario, execute the method, and get results.  
 Finally, we compare the results to the expected value.

Let’s say we want to test the findIndex() method of the ArrayMethods class. The steps of the test are shown below:

        @Test
        public void testfindIndex_numberInArray() {
            // 1. create object of the class
            ArrayMethods arrayMethods = new ArrayMethods();
            // 2. call method
            int result = arrayMethods.findIndex(new int[] {8, 4, 5}, 4);
            // 3. compare the actual results to the expected
        }

## Assertions

JUnit provides us with a number of assertions, which are methods that test whether the expectation and reality are the same.  
 One way to confirm if the actual results are the same as expected is to print a message on the console.  
 For huge applications, scanning through logs is a cumbersome process.  
 JUnit’s way of reporting the status of the test is by using one of the assert methods.

> To verify if the result of the above test is 1.  
>  The JUnit assert method assertEquals() can be used. It compares expected and actual values and generates a report in case the two values do not match.
>
> The **two input arguments to the assertEquals() method are the expected result and the actual value** returned by the method.

        assertEquals(expected, actual);

In our case, the expected value from our input array is 1 and the value actually returned by the search method is stored in the variable result.

        assertEquals(1, result);

Now let’s assume there is a change in the source code and a different answer is returned (change return index to return ++index). This test will start failing if the actual value does not match the expected value and we will get an assertion error:

        Expected <1> but was <2>.

An advantage of continuously running the tests is that we can get notified of any bugs that have been introduced in our code and fix them right away.

## Creating multiple test cases

Following the same simple steps, we can test other test cases to check when a value is not in the array and when an empty input array is sent to the method.

The tests can be improved by making the result variable inline, i.e., by calling the search method inside the assertEquals() method as follows:

        @Test
        public void testfindIndex_numberNotInArray() {
            ArrayMethods arrayMethods = new ArrayMethods();
            assertEquals(-1, arrayMethods.findIndex(new int[]{8,4,5}, 1));
        }

Let's have a look at another method:

        @Test
        public void testfindIndex_emptyArray() {
            ArrayMethods arrayMethods = new ArrayMethods();
            assertEquals(-1, arrayMethods.findIndex(new int[]{}, 1));
        }

JUnit will run all methods marked with the @Test annotation.  
 There is no particular order in which the tests run.  
 JUnit creates a new test instance every time a test runs.  
 So if we have three tests, there will be three instances of the ArrayMethods class.
