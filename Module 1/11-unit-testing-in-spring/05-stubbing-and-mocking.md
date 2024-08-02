# Stubbing and Mocking

Learn about the stubs and mocks and why mocks are preferred over stubs.

> We'll cover the following:
>
> - The need for stubbing and mocking
> - Creating a stub
> - Mockito dependency
> - Creating a mock

## The need for stubbing and mocking

In unit testing, we should be able to test every application layer independently.  
 A test for the service layer should not be dependent on the data layer.  
 We use stubbing and mocking to test a part of our application by mocking an external service or an application layer.  
 There are different mocking frameworks available like Mockito, JMock, and EasyMock

> Suppose we have a StudentService class which has a findTotal() method to perform the sum of an array.  
>  We have another class StudentDao that has a method to return data about student marks.
>
> In the findTotal() method, we can fetch data using the StudentDao object and then find the total.
>
> Now every time the JUnit test runs, the StudentDao class will connect to a database and get marks’ data. This approach has two glaring issues.  
>  On one hand, it makes the test execution time longer, and on the other hand, it makes the test susceptible to failure in case the database connection is down.
>
> In such a scenario, our test will fail because of database connectivity issues and not because of any mistake in the logic of the code under test.  
> To avoid this, we can either stub or mock the data source and simply test our code without interacting with the database.
>
> In the same manner, if our application connects to a cloud service, we can mock the service instead of actually connecting to it every time.
>
> We will create a method findTotal() in the StudentService class to find the total of an array. It uses the StudentDao class that returns an array of marks obtained by a student. Our method uses the data returned by the StudentDao class to find the total of the array.

        public class StudentService {
            private StudentDao studentDao;

            // other methods

            int findTotal() {
                // fetch student marks from a database
                int[] array = studentDao.getMarks();
                int sum = 0;
                for (int value : array) {
                    sum += value;
                }
                return sum;
            }
        }

The StudentDoa class has a getMarks() method along with other methods.  
 We are not concerned with the actual implementation of getMarks() because right now, we are testing the StudentService class.

        public class StudentDao {
            // ..

            int[] getMarks() {
                // fetch and return data
                return new int[] {};
            }
        }

To test the findTotal() method, we will create a new JUnit test called StudentServiceStubTest with a method testfindTotal() as follows:

        class StudentServiceStubTest {
            @Test
            void testfindTotal() {
                StudentService studentService = new StudentService();
                int total = studentService.findTotal();
            }
        }

If the test is run like this, we will get a NullPointerException because we do not have any implementation of the getMarks() method.  
 Stubs provide a way to create a dummy implementation.

## Creating a stub

To test the findTotal() method, we can create a stub class that extends the Dao class. We can write an implementation of the getMarks() method and return some dummy data back so that the findTotal() method can process the data.

        class StudentDaoStub extends StudentDao {
            @Override
            public int[] getMarks() {
                return new int[] {15, 20, 5};
            }
        }

Since StudentDao is a dependency of the StudentService class, we will pass the stub class to it.  
 For this, we need a constructor in the StudentService class to initialize StudentDao.

        public StudentService(StudentDao studentDao) {
            super();
            this.studentDao = studentDao;
        }

Now we can pass the stub class as a constructor argument when creating the StudentService object inside our test method.  
 Since the result returned by the stub is already known, we can now check if the expected and actual values are the same using the assertEquals() method.

        @Test
        void testfindTotal() {
            StudentService studentService = new StudentService(new StudentDaoStub());
            int total = arrayMethods.findTotal();
            assertEquals(40, total);
        }

Using stubs, we can imitate the StudentDao class and test our method.  
 In the same ways, stubs can also be created for an interface.

This approach has some disadvantages.

- First, to make other test cases, we need to create another stub or another implementation of the StudentDao class.  
   Creating multiple versions of the same class is cumbersome.
- Second, if the stub is an implementation of an interface and new methods are added to the interface, we will need to update the stub and provide an implementation for the unimplemented methods.

These problems are tackled by mocks.

## Mockito dependency

We can use the mocking support provided by Spring Boot or add the dependency in pom.xml.  
 Since we created our project using Spring Initializr, JUnit and Mockito are automatically added as dependencies in the Maven dependencies folder and are available under the scope of test.

If this is not the case, the mockito dependency can be added as follows:

        <dependency>
            <groupId>org.mockito</groupId>
            <artifactId>mockito-core</artifactId>
            <version>2.10.0</version>
            <scope>test</scope>
        </dependency>

## Creating a mock

We will create a test for the findTotal() method using Mockito.  
 For this, we first create a new JUnit test called StudentServiceMockTest.

The mock() method of org.mockito.Mockito creates a mock of a class if you pass it as an argument.  
 Then, you can use the mock to do whatever you want it to. We will mock the StudentDao class as follows:

        StudentDao studentDaoMock = mock(StudentDao.class);

The mock() method needs a static import of org.mockito.Mockito.mock.

Two methods used on the mock are when() and thenReturn(), which help create a test scenerio.  
 When a method is called on studentDaoMock, return an array of numbers as follows:

        when(studentDoaMock.getMarks()).thenReturn(new int[] {15, 20, 5});

The when() method needs a static import org.mockito.Mockito.when.  
 Now we can pass the mock as a constructor argument to the StudentService object.

The complete test method is shown below:

        @Test
        void testfindTotal() {
            StudentDao studentDaoMock = mock(StudentDao.class);
            when(studentDaoMock.getMarks()).thenReturn(new int[] {15, 20, 5});

            StudentService studentService = new StudentService(studentDaoMock);
            int total = studentService.findTotal();
            assertEquals(40, total);
        }

To create a stub, we need to create a complete class to implement the method and return values for the test case.  
 In case of mocking, no implemenations is needed.  
 A mock can easily be replaced for other test case scenerios as well.  
 We can mock the data just by using the when() and thenReturn() methods.

Here is another test when an empty array is returned by studentDaoMock:

        @Test
        void testfindTotal_EmptyArray() {
            StudentDao studentDaoMock = mock(StudentDao.class);
            when(studentDaoMock.getMarks()).thenReturn(new int[] {});

            StudentService studentService = new StudentService(studentDaoMock);
            assertEquals(0, total);
        }

In this manner, we can mock the class and create tests for other scenerios as well.  
 Here, we have used constructor injection for injecting studentDaoMock in StudentService.  
 Setter injection can also be used. We created a mock for a class but mocks can also be created for interfaces.
