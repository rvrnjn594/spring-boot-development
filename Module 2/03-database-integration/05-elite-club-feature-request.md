# Elite Club Feature Request

Search feature requests for the Elite Club application.

> We'll cover the following
>
> - Elite club new feature

## Elite club new feature

Our Elite Club application is running fine in production, but we are getting feedback from our users that they also want a feature to search for a club. The requirement of the search functionality is as follows:

- The search function must return all clubs starting with the given name.
- The search must be case insensitive.
- The search must return the result in ascending order.

From the requirement, it looks like the functionality provided by the basic CRUD repository will not suffice. We need to build queries to accommodate.

Next, we will explore different approaches for building queries using Spring Data JPA.
