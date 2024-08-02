# Debugging Auto-Configuration

In order to debug any production issue, we should know what auto-configurations are applied to the application. Learn how to debug the Spring Boot auto-configuration.

> We'll cover the following
>
> - Auto-configuration report
>
>   1.  Application property approach
>   2.  Command-line approach
>   3.  Actuator approach
>
> - Billionaire spring boot auto-configuration report
> - Try it yourself
> - Key takeaways

## Auto-configuration report

Upon starting the application, we don’t get a lot of information of how Spring Boot decided to compose our applications through auto-configuration.

For debugging purposes, it’s essential to know which auto-configurations are applied to the application.

The following are three ways in which Spring Boot generates the auto-configuration report.

#### 1. Application property approach

Set below property in application.properties or application.yml.

        debug=true

#### 2. Command-line approach

Or, if we don’t want to use the properties file approach, we can trigger the auto-configuration report by starting the application with the --debug switch.

        java -jar <my_application>.jar --debug

Both the approaches above print the auto-configuration report on the console during startup.  
 The third is an actuator-based approach that provides the auto-configuration report whenever we need it for diagnostic purposes.

#### 3. Actuator approach

Another way to debug auto-configuration is to add a Spring Boot actuator to our project. Add below the dependency in POM.

        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-actuator</artifactId>
        </dependency>

Add the property below into application.properties

        management.endpoints.web.exposure.include=\*

The /actuator/conditions endpoint will show the details of all the beans that are auto-configured, and those that are not.

_Don’t worry too much about the actuator yet. We have separate bonus lessons to cover it._

## spring-boot-auto-configuration report

#### Auto-configuration report details

The auto-configuration report consists of four sections.

- **Positive matches:** Where @Conditional evaluated to true and the configuration classes have been included
- **Negative matches:** Where @Conditional evaluated to false and the configuration classes have not been included
- **Exclusions:** The configuration classes that we explicitly excluded on the application side
- **Unconditional classes:** These classes will always be part of the application configuration

## Key takeaways

- Spring Boot provides three ways of printing auto-configuration reports.
- The auto-configuration report consists of three sections: positive match, negative match, and exclusion.
