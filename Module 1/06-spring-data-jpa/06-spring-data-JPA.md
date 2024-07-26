# Spring Data JPA

Learn about the methods provided by JpaRepository to further simplify the CRUD operations.

> We'll cover the following:
>
> - JpaRepository interface
>   - save() method
>   - findById() method
>   - findAll() method
>   - deleteById() method

We have written methods to perform CRUD operations on the Player entity. If we add more entities to the project like Coach and Tournament, we will have to write the same code for CRUD operations and plug a different entity type.

The methods that we implemented as part of the CRUD operations are all generic methods. The logic of the methods remains the same, and only the entity changes.

Spring Data identified this duplication of code when writing repositories and created some predefined repositories. The developer provides the entity type and its primary key and Spring Data comes up with the CRUD methods for the entity. Spring Data JPA adds a layer of abstraction over the JPA provider (Hibernate in our case).

The JpaRepository interface extends the Repository interface. It contains the API provided by CrudRespository as well as the PagingAndSortingRepository for CRUD operations along with pagination and sorting of records.
