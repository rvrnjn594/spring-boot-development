# Creating a REST Controller

Learn how to create a controller to map requests to out REST API.

> We'll cover the following:
>
> 1. @RESTController
> 2. Adding request mapping
> 3. Testing the RESTful endpoint

In this lesson, we will **create a REST service with a request mapping /welcome** and send it a request from a REST client (web browser or Postman). This service will respond with a welcome message.

## 1. @RestController

The first step is creating a controller class called PlayerController with the @RestController annotation.  
 This annotation is an extension of @Controller annotation.

The **@RestController annotation has support for REST requests and responses and automatically handles the data binding** between the Java POJOs and JSON.

        @RestController
        public class PlayerController {

        }

## 2. Adding request mapping

Next, set up a mapping for the request using the @GetMapping annotation as follows:

        @GetMapping("/welcom")
        public String welome() {
            return "Tennis Player REST API";
        }

The above code creates a RESTful endpoint /welcome which can be accessed from the REST client and returns the message string to the client.  
The @GetMapping annotation is discussed in detail in the next lesson.

## 3. Testing the RESTful endpoint

> We will test the REST endpoint with a web browser as well as Postman.  
>  You can run the code given below and access the results at the link shown below the code widget.
>
> If you are working on a local machine, simply type the URL localhost:8080/welcome in the web browser and it will return the response.
