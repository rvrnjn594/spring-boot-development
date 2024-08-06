## Generic Persistence Tier

Learn to design a reusable persistence tier for elegant data access and less code clutter.

> We'll cover the following
>
> - Objective
> - Problem
> - Solution
> - Persistence layer design
> - Key takeaways

## Objective

This chapter will focus on designing the data tier by using a single, generic Data Access Object (DAO) for all JPA entities in the system, which will result in elegant data access, with no unnecessary clutter or verbosity.

## Problem

In most of the applications developed in the past, the design of the data layer consists of multiple classes.

However, the application can only use simple CRUD operations on the database.  
 Hence the design of the persistence layer forces the system to duplicate the logic across multiple DAO classes.

This violates the DRY(Do Not Repeat Yourself) principle.

> **Note:** There is always one to one mapping between DAO & Entity.

## Solution

The solution is to implement some abstraction that can be used by all the DAO in the system for operations common across them.

These multiple implementations can usually be replaced by a single parametrized DAO.

## Persistence layer design

Let’s design one such abstract DAO using JPA. The design requires us to create a couple of classes.

1. AbstractJpaDao.java: Contains abstraction code.
2. GenericDao.java: A concrete implementation of abstraction.

Code is shown below for one such persistence layer.

**AbstractJpaDao.java**

        public abstract class AbstractJpaDao< T extends Serializable > {

            private Class< T > clazz;

            @PersistenceContext
            EntityManager entityManager;

            public void setClazz( Class< T > clazzToSet ) {
                this.clazz = clazzToSet;
            }
            public T findOne( Long id ){
                return entityManager.find( clazz, id );
            }
            public List< T > findAll(){
                return entityManager.createQuery( "from " + clazz.getName() )
                .getResultList();
            }
            public void save( T entity ){
                entityManager.persist( entity );
            }
            public void update( T entity ){
                entityManager.merge( entity );
            }
            public void delete( T entity ){
                entityManager.remove( entity );
            }
            public void deleteById( Long entityId ){
                T entity = getById( entityId );
                delete( entity );
            }
        }

**ApplicationService.java**

        @Service
        class ApplicationService implements IApplicationService{

            IGenericDao<Foo> dao;

            @Autowired
            public void setDao(IGenericDao<Foo> daoToSet) {
                dao = daoToSet;
                dao.setClazz(Foo.class);
            }

            // ...
        }

**GenericJpaDao.java**

        @Repository
        @Scope( BeanDefinition.SCOPE_PROTOTYPE )
        public class GenericJpaDao< T extends Serializable > extends AbstractJpaDao< T > implements IGenericDao< T > {
            //...
        }

## Key takeaways

- Simplifying the Data Access Layer by providing a single, reusable implementation of a generic DAO promotes cleaner design.

The result is a streamlined persistence layer, with no unnecessary clutter.
