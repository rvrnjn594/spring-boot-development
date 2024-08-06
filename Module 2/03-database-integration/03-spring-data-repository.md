# Spring Data Repository

Learn the use of the spring data repository to implement a ready-made persistence tier with almost no code.

> We'll cover the following
>
> - Objective
> - Introduction
> - Billionaire club enhancement
>   - Add Maven dependency
>   - Scan JPA repository
>   - Add repository interface
>   - Wiring repository in service
> - Key takeaways

## Objective

In the previous chapter, we have designed a persistence layer using generic & abstract DAO.

This article will focus on designing a persistence layer using Spring Data JPA.

## Introduction

As we discussed in a previous lesson, designing a persistence layer with abstract DAO requires us to write some amount of boilerplate code. It is found that this pattern is used in every project. Why not provide such a programming model as part of the framework?

The capability is provided by Springdata, which makes it possible to remove the DAO implementations entirely.

To leverage the Spring Data programming model with JPA, our repository should extend one of the available Spring Data JPA repository interfaces.  
 **By extending the interface, we can perform basic CRUD operations and more without even writing a single line of code.**

Isn’t that awesome?

Let’s enhance our old-school Billionaire’s Club application to utilize Spring Data JPA.

## Billionaire club enhancement

Let’s modify our old Billionaire club Application to use spring Data JPA.

The functional flow of the application will still remain the same however instead of our own BillionairesRepository.java & BillionairesJpaRepository.java we will use the Spring Data JPA feature.

#### Add Maven dependency

Add below Maven dependency in pom.

        <dependency>
            <groupId>org.springframework.data</groupId>
            <artifactId>spring-data-jpa</artifactId>
            <version>1.11.23.RELEASE</version>
        </dependency>

> **Note:** It’s not the Spring Boot starter dependency, because we are updating the old school Billionaire’s Club application.

#### Scan JPA repository

In order to scan Data JPA Repository special annotation to be applied to the configuration class.

Add @EnableJpaRepositories(basePackages = "com.rest.dao")annotation on DaoConfig.java.

This annotation will help spring to scan the data repository class in package com.rest.dao.

#### Add repository interface

        package com.rest.dao;

        import com.rest.domain.Billionaires;
        import org.springframework.data.jpa.repository.JpaRepository;

        public interface BillionaireSpringDataJpaRepository extends JpaRepository<Billionaires, Long> {
        }

If you are wondering where the implementation of the interface is:  
 Spring Data JPA injects implementation for this interface condition that repository interface must extend one of the existing interfaces from the Spring data package.

In our example, we are extending JpaRepository.java

If we observe the decompiled source of the JpaRepository class, we’ll notice it contains all basic methods to perform CRUD operations on billionaire entities.
`
//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

        package org.springframework.data.jpa.repository;

        import java.io.Serializable;
        import java.util.List;
        import org.springframework.data.domain.Example;
        import org.springframework.data.domain.Sort;
        import org.springframework.data.repository.NoRepositoryBean;
        import org.springframework.data.repository.PagingAndSortingRepository;
        import org.springframework.data.repository.query.QueryByExampleExecutor;

        @NoRepositoryBean
        public interface JpaRepository<T, ID extends Serializable> extends PagingAndSortingRepository<T, ID>, QueryByExampleExecutor<T> {
            List<T> findAll();

            List<T> findAll(Sort var1);

            List<T> findAll(Iterable<ID> var1);

            <S extends T> List<S> save(Iterable<S> var1);

            void flush();

            <S extends T> S saveAndFlush(S var1);

            void deleteInBatch(Iterable<T> var1);

            void deleteAllInBatch();

            T getOne(ID var1);

            <S extends T> List<S> findAll(Example<S> var1);

            <S extends T> List<S> findAll(Example<S> var1, Sort var2);
        }

Is it not the same thing we designed in our own persistence layer. With Spring data JPA framework, that abstraction is available to us readymade.

#### Wiring repository in service

Now we need to use repository in our service method.

        @Service
        public class BillionairesServiceImpl implements BillionairesService {

            @Autowired
            private BillionaireSpringDataJpaRepository billionairesJPADao;

## Key takeaways

- Spring data is a new elegant way to implement a data tier with almost no code.
- We need to implement one of the existing interfaces provided by SpringData JPA to implement our custom repository.
- Custom repository is an interface(not a class) and implementation is provided by spring data.
