# Assert Methods

Learn about a variety of assert methods used to test if the actual output matches the expected output.

> We'll cover the following:
>
> - assertEquals()
> - assertArrayEquals()
> - assertTrue()
> - assertNull()
> - fail()
> - assertThrows()
>   - assertAll()

There are a number of assert methods that are available with the import of the Assertions.\* package.

## assertEquals()

The assertEquals() method compares two values - the expected value and the actual value - to determine whether or not both are the same.  
 A test method from the last lesson using assertEquals() is reproduced here:

        @Test
        public void testfindIndex_numberNotInArray() {
            ArrayMethods arrayMethods = new ArrayMethods();
            assertEquals(-1, arrayMethods.findIndex(new int[]{8,4,5}, 1));
        }

There are many variations of assertEquals() with different data types.  
 An optional third String argument can be added that contains a message about what the test is for.  
 This feature is handy for a code base spanning thousands of lines, where the developer might not know what a failing test intended to do.  
 Suppose the following test fails because we want the expected output to be 1, whereas the actual value that was returned is -1.  
 In this case, our message will explain what the method under test is supposed to do.

        assertEquals(1, arrayMethods.findIndex(new int[] {8, 4, 5}, 1), "The findIndex method finds the index of a given number");

## assertArrayEquals()

This method takes two arrays and compares them element by element and finds out if both are the same or not.  
 For example, if there is a sortArray() method to sort an array of numbers, the assertArrayEquals() method can be used as follows:

        assertArrayEquals(new int[] {0,1,3,7}, arrayMethods.sortArray(new int[] {3,1,7,0}));

## assertTrue()

assertTrue() takes a condition as argument and checks if it is true.  
 In the same manner, assertFalse() checks for a condition to be false.  
 This can easily be achieved through the assertEquals() method as well.  
 These are just convenience methods to use when testing for a Boolean condition to be either true or false.  
 The same result can be achieved through the assertEquals() method. Both approaches are shown below:

        @Test
        void testAssert() {
            Boolean condition = true;
            assertEquals(true, true);
            assertEquals(condition);
        }

## assertNull()

assertNull() and assertNotNull() are used to check for null values. The same can be achieved using assertEquals() and passing null as the expected value, but as mentioned above, these methods are just for convenience.

        @Test
        void testAssert() {
            String str = null;
            assertEquals(null, str);
            assertNull(str);
        }

## fail()

The fail() method fails the test, no matter what.  
It can be used to mark an unfinished test, so that the developer is notified that it needs to be completed.

        @Test
        public void testSortArray() {
            fail("unimplemented method");
        }

## assertThrows()

Sometimes we may want to assert exceptions. The assertThrows() method is used to check if an exception is thrown.  
 It takes the executable, which causes the exception along with the exception that we are expecting.

For example, suppose we add a method printArray to our class as follows:

        void printArray(int[] array, int index) {
            System.out.println(array[index]);
        }

To test if the ArrayIndexOutOfBound exception is thrown, we can write the following test:

        @Test
        public void testfindIndex_indexOutOfBound() {
            ArrayMethods arrayMethods = new ArrayMethods();
            assertThrows(ArrayIndexOutOfBoundsException.class, ()->arrayMethods.printArray(new int[] {1, 8, 5}, 3));
        }

The test contains the type of the exception that we are expecting (ArrayIndexOutOfBoundException.class) and a lambda expression that will evaluate to the exception.

## assertAll()

Notice that we have very simple tests that just assert some basic stuff.  
 We can combine all of them in one method rather than having a separate method for each test case.  
 Consider the following test written for the findIndex() method.  
 In the test, we are combining scenarios like finding the index when the number is in the array, when it is not in the array, and when the array is empty.  
 Instead of writing three tests, each testing one scenario, we can use an assertAll() to combine all the assertions using lambda expressions as follows:

        @Test
        public void testfindIndex() {
            ArrayMethods arrayMethods = new ArrayMethods();
            assertAll(
                () -> assertEquals(1, arrayMethods.findIndex(new int[] {8, 4, 5}, 4)),
                () -> assertEquals(-1, arrayMethods.findIndex(new int[] {8, 4, 5}, 1)),
                () -> assertEquals(-1, arrayMethods.findIndex(new int[] {}, 1));
            );
        }
