# Mockito Annotations

Learn about some of the annotations provided by Mockito to make test shorter and more readable.

> we'll cover the following:
>
> - @Mock
> - @InjectMocks
> - @ExtendWith

There are some Mockito annotations that help minimize the code for creating and injection mocks.  
 While writing test, there can be some repetitive statements.  
 We will use annotations provided by Mockito to get rid of the redundant code and make our tests more readable.

## @Mock

The @Mock annotation is used to create a mock without calling the Mockito.mock() method.

Instead of creating a mock inside every test, we can move this statement outside and use the @Mock annotation on it.  
 If we create mocks in the following way, we are using repetitive statements in every test:

        StudentDao studentDaoMock = mock(StudentDao.class);

Rather than repeating the above statement in every test, we can create the mocks outside the test method using the @Mock annotation as follows:

        @Mock
        StudentDao studentDaoMock;

By using the @Mock annotation, we will create the mock once instead of creating it in every test.

## @InjectMocks

This annotation automatically injects a mock object into the object being tested. We have been passing the mock object to the StudentService object as a constructor argument in the test method as follows:

        StudentService studentService = new StudentService(studentDaoMock);

We can do the dependency injection outside the test method and use the @InjectMocks annotation as follows:

        @InjectMocks
        StudentService studentService;

The tests become shorter when we remove the create and inject mock steps from the test method.  
 We can further shorten the method by making the total variable inline.

For comparison, a test written with and without annotations is shown below:

        //without annotations
        @Test
        void testFindTotal() {
            StudentDao studentDaoMock = mock(StudentDao.class);
            when(studentDaoMock.getMarks()).thenReturn(new int[] {15, 20, 5});

            StudentService studentService = new StudentService(studentDaoMock);
            int total = studentService.findTotal();
            assertEquals(40, total);
        }

Now, let's have a look at the test method using annotation:

        //with annotations
        @Mock
        StudentDao studentDaoMock;

        @InjectMocks
        StudentService studentService;

        @Test
        void testFindTotal() {
            when(studentDaoMock.getMarks()).thenReturn(new int[] {15, 20, 5});
            assertEquals(40, studentService.findTotal());
        }

The test method is short and the test is more readable when Mockito annotations are used.

## @ExtendWith

When the @Mock annotation is used, by default, JUnit does not evaluate it.  
We need to use the @ExtendWith annotation and pass MockitoExtension.class as an argument to integrate Mockito with the JUnit 5 extension model.  
 MockitoExtension enables the evaluation of the @Mock annotations to initialize mocks, which was previously done using the Mockito.mock() method.

Using the Mockito extension, mocks are initialized before each test method and validation is performed after each test method to check if the mock was used in the method.

        @ExtendWith(MockitoExtension.class)

The three annotations make the tests very simple and readable.

For the sake of comparison, the tests written using repetitive stubbing and mocking are also reproduced below addition to the ones written using Mockito annotations.

**StudentServiceMockTest.java**

        package io.datajek.unittesting;

        import static org.junit.jupiter.api.Assertions.*;
        import org.junit.jupiter.api.Test;

        import static org.mockito.Mockito.mock;
        import static org.mockito.Mockito.when;

        class StudentServiceMockTest {

            @Test
            void testFindTotal() {
                StudentDao studentDaoMock = mock(StudentDao.class);
                when(studentDaoMock.getMarks()).thenReturn(new int[] {15, 20, 5});

                StudentService studentService = new StudentService(studentDaoMock);
                int total = studentService.findTotal();
                assertEquals(40, total);
            }

            @Test
            void testFindTotal_EmptyArray() {
                StudentDao studentDaoMock = mock(StudentDao.class);
                when(studentDaoMock.getMarks()).thenReturn(new int[] {});

                StudentService studentService = new StudentService(studentDaoMock);
                int total = studentService.findTotal();
                assertEquals(0, total);
            }
        }

**StudentServiceMockitoAnnotation.java**

        package io.datajek.unittesting;

        import static org.junit.jupiter.api.Assertions.assertEquals;
        import static org.mockito.Mockito.when;

        import org.junit.jupiter.api.Test;
        import org.junit.jupiter.api.extension.ExtendWith;
        import org.mockito.InjectMocks;
        import org.mockito.Mock;
        import org.mockito.junit.jupiter.MockitoExtension;

        @ExtendWith(MockitoExtension.class)
        class StudentServiceMockitoAnnotationsTest {

            @Mock
            StudentDao studentDaoMock;

            @InjectMocks
            StudentService studentService;

            @Test
            void testFindTotal() {
                when(studentDaoMock.getMarks()).thenReturn(new int[] {15, 20, 5});
                assertEquals(40, studentService.findTotal());
            }

            @Test
            void testFindTotal_EmptyArray() {
                when(studentDaoMock.getMarks()).thenReturn(new int[] {});
                assertEquals(0, studentService.findTotal());
            }
        }
