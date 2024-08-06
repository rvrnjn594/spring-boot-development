# Elite Club Search Feature

Enhance Elite Club application with custom query leveraging knowledge from the previous lesson.

> We'll cover the following
>
> - Feature implementation
>   - Add search method in the service interface
>   - Add method in repository
>   - @Query annotation
>   - Invoke repository method
>   - Verify the feature
> - Live application
> - Key takeaways

In the previous chapter, we learned how to build queries and fetch data from DB using various techniques.

In this chapter, we are going to utilize our knowledge and implement a search feature.

To implement our feature we will use the @Query annotation technique. There are two reasons for that:

1. As per our requirements, the search should be case insensitive. The Named Method approach does not have a mechanism to distinguish between lower and upper case column values, thus we cannot use this approach.
2. Named Query and @Query annotations are similar but, I personally prefer @Query annotation because it it is the midpoint between the other two approaches. Moreover, the visibility of the query in the repository on the top of the method gives us easy navigation and debuggability.

## Feature implementation

To implement our feature, we are required to perform the following changes in our codebase

1. Add searchClub() method in EliteClubService interface.
2. Add findClubs() method in EliteClubRepository.
3. Annotate the above method with @Query annotation and write JPAQL.
4. Call the repository method from our service and return the List<ClubDTO>.

#### Add search method in the service interface

To implement this our first step will be to add a searchClub() method in the service interface. The signature of the service interface looks like the following code snippet:

        List<ClubDTO> searchClub(String searchTerm);

#### Add method in repository

We need to add one method in EliteClubRepository to be called from service implementation. Signature of repository method is exactly similar to service method signature like below:

        List<EliteClub> findClubs(String searchTerm);

#### @Query annotation

The most important step is to add @Query annotation on the top of the repository method with JPAQL inside it.  
 After the change method of EliteClubRepository will look like the code snippet below:

        @Query("SELECT x from EliteClub x WHERE lower(x.clubName) LIKE :searchTerm order by x.clubName asc")
        List<EliteClub> findClubs(@Param("searchTerm") String searchTerm);

#### Invoke repository method

The last step is to invoke the repository from the service interface.  
 But there is one catch; our search result should be case insensitive and should find clubs that start with a search term.

Thus to incorporate the below change, we need to have one method to build input contact required by the repository. For the same, we have created one private method in the buildLikePattern service.

After implementing change code snippets will look like below:

        public List<ClubDTO> searchClub(String searchTerm) {
            LOG.info("Searching term {}", searchTerm);
            List<ClubDTO> result = eliteClubRepository.findClubs(buildLikePattern(searchTerm)).stream().map(c -> new ClubDTO(c.getClubName())).collect(Collectors.toList());
            LOG.info("Search Result: {} ", result);
            return result;
        }

        private String buildLikePattern(String searchTerm) {
            return searchTerm.toLowerCase() + "%";
        }

#### Verify the feature

To verify our changes we can utilize our EliteClubApplication#run() method.

Add the below snippet in the run method and verify the logs.

        List<ClubDTO> clubs = eliteClubService.searchClub("Bi");
        LOG.info("Search Result : {}",clubs);

> **Note:** Up until now, we haven’t exposed any endpoint for the feature therefore all the verification we will be doing from the console output.  
>  It is recommended that we browse through the source code to understand it better.

It is recommended that we [browse through the source code](../eliteclub/) to understand it better.  
[See here](https://www.educative.io/module/page/O7rwGNTE1LJD4RVVx/10370001/6017676650741760/4717718966370304)

If we verify logs, the following statement is printed

        2021-01-03 10:37:04.451  INFO 84 --- [           main] c.c.e.service.EliteClubServiceImpl       : Searching term Bi
        Hibernate: select eliteclub0_.id as id1_0_, eliteclub0_.club_name as club_nam2_0_ from elite_club eliteclub0_ where lower(eliteclub0_.club_name) like ? order by eliteclub0_.club_name asc
        2021-01-03 10:37:04.547  INFO 84 --- [           main] c.c.e.service.EliteClubServiceImpl       : Search Result: [ClubDTO{clubName='Billionaire'}]
        2021-01-03 10:37:04.548  INFO 84 --- [           main] com.club.eliteclub.EliteClubApplication  : Search Result : [ClubDTO{clubName='Billionaire'}]
        As we are searching for clubs starting with the name ‘Bi’, we found the Billionaire Club.

## Key takeaways

In this section, we have covered

- How to build a persistence layer using the old school way.
- How Spring Data JPA helps us to build a persistence layer with little to no code.
- Different ways to build a query with Spring Data.
- Added search feature in our EliteClub application based on Spring Data JPA.
