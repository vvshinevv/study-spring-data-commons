## 4. Working with Spring Data Repositories

The goal of the Spring Data repository abstraction is to significantly reduce the amount of boilerplate code required to implement data access layers for various persistence stores. 

SpringData 저장소 추상화의 목표는 다양한 지속성 저장소에 대한 데이터 액세스 계층을 구현하는 데 필요한 상용구 코드의 양을 크게 줄이는 것입니다.

```
Spring Data repository documentation and your module

This chapter explains the core concepts and interfaces of Spring Data repositories.
The information in this chapter is pulled from the Spring Data Commons module. 
It uses the configuration and code samples for the Java Persistence API (JPA) module.
You should adapt the XML namespace declaration and the types to be extended to the equivalents of the particular module that you use. 
“Namespace reference” covers XML configuration, which is supported across all Spring Data modules that support the repository API. 
“Repository query keywords” covers the query method keywords supported by the repository abstraction in general. 
For detailed information on the specific features of your module, see the chapter on that module of this document.


SpringData 저장소 문서 및 모듈

이 장에서는 스프링 데이터 저장소의 핵심 개념과 인터페이스를 설명합니다.
이 장의 정보는 Spring Data Commons 모듈에서 가져 왔습니다.
JPA (Java Persistence API) 모듈에 대한 구성 및 코드 샘플을 사용합니다.
XML 네임 스페이스 선언과 확장 할 유형을 사용하는 특정 모듈에 상응하는 것으로 조정해야합니다.
"네임 스페이스 참조"는 저장소 API를 지원하는 모든 스프링 데이터 모듈에서 지원되는 XML 구성을 다룹니다.
"리포지토리 쿼리 키워드"는 일반적으로 리포지토리 추상화에서 지원하는 쿼리 메서드 키워드를 다룹니다.
모듈의 특정 기능에 대한 자세한 내용은이 문서의 해당 모듈에 대한 장을 참조하십시오.
```

### 4.1. Core concepts
The central interface in the Spring Data repository abstraction is Repository. It takes the domain class to manage as well as the ID type of the domain class as type arguments. This interface acts primarily as a marker interface to capture the types to work with and to help you to discover interfaces that extend this one. The CrudRepository interface provides sophisticated CRUD functionality for the entity class that is being managed.

SpringData 저장소 추상화의 중심 인터페이스는 Repository입니다. 관리 할 도메인 클래스와 도메인 클래스의 ID 유형을 유형 인수로 사용합니다. 이 인터페이스는 주로 작업할 유형을 캡처하고 이 인터페이스를 확장하는 인터페이스를 발견하는 데 도움이되는 마커 인터페이스 역할을 합니다. CrudRepository 인터페이스는 관리중인 엔티티 클래스에 대한 정교한 CRUD 기능을 제공합니다.


#### Example 5. `CrudRepository` Interface
```java
public interface CrudRepository<T, ID> extends Repository<T, ID> {

  <S extends T> S save(S entity);      // 1

  Optional<T> findById(ID primaryKey); // 2

  Iterable<T> findAll();               // 3

  long count();                        // 4

  void delete(T entity);               // 5

  boolean existsById(ID primaryKey);   // 6

  // … more functionality omitted.
}
```
1. Saves the given entity.
2. Returns the entity identified by the given ID.
3. Returns all entities.
4. Returns the number of entities.
5. Deletes the given entity.
6. Indicates whether an entity with the given ID exists.


> We also provide persistence technology-specific abstractions, such as JpaRepository or MongoRepository. Those interfaces extend CrudRepository and expose the capabilities of the underlying persistence technology in addition to the rather generic persistence technology-agnostic interfaces such as CrudRepository.
> 
> JpaRepository 또는 MongoRepository와 같은 지속성 기술별 추상화를 제공합니다. 이러한 인터페이스는 CrudRepository를 상속받고 CrudRepository와 같은 다소 일반적인 지속성 기술에 구애받지 않는 인터페이스 외에도 기본 지속성 기술의 기능을 노출합니다.


On top of the CrudRepository, there is a PagingAndSortingRepository abstraction that adds additional methods to ease paginated access to entities:

CrudRepository 위에는 엔티티에 페이지를 매긴 액세스를 용이하게 하기 위해 추가 메서드를 추가하는 PagingAndSortingRepository 추상화가 있습니다.

#### Example 6. `PagingAndSortingRepository` interface
```java
public interface PagingAndSortingRepository<T, ID> extends CrudRepository<T, ID> {

  Iterable<T> findAll(Sort sort);

  Page<T> findAll(Pageable pageable);
}
```

To access the second page of `User` by a page size of 20, you could do something like the following:

20 페이지 크기로`User`의 두 번째 페이지에 액세스하려면 다음과 같이 할 수 있습니다.

```java
PagingAndSortingRepository<User, Long> repository = // … get access to a bean
Page<User> users = repository.findAll(PageRequest.of(1, 20));
```
In addition to query methods, query derivation for both count and delete queries is available. The following list shows the interface definition for a derived count query:

쿼리 메서드 외에도 개수 및 삭제 쿼리 모두에 대한 쿼리 파생을 사용할 수 있습니다. 다음 목록은 파생 카운트 쿼리에 대한 인터페이스 정의를 보여줍니다.

#### Example 7. Derived Count Query
```java
interface UserRepository extends CrudRepository<User, Long> {
  long countByLastname(String lastname);
}
```

The following listing shows the interface definition for a derived delete query:

#### Example 8. Derived Delete Query
```java
interface UserRepository extends CrudRepository<User, Long> {

  long deleteByLastname(String lastname);

  List<User> removeByLastname(String lastname);
}
```

### 4.2. Query Methods
Standard CRUD functionality repositories usually have queries on the underlying datastore. With Spring Data, declaring those queries becomes a four-step process:

 1. Declare an interface extending Repository or one of its subinterfaces and type it to the domain class and ID type that it should handle, as shown in the following example:
    
    ```java 
    interface PersonRepository extends Repository<Person, Long> { … }
    ```

 2.  Declare query methods on the interface.
    
    ```java
    interface PersonRepository extends Repository<Person, Long> {
        List<Person> findByLastname(String lastname);
    }
    ```
    
 3. Set up Spring to create proxy instances for those interfaces, either with JavaConfig or with XML configuration.
    
    a. To use Java configuration, create a class similar to the following:
    ```java
    import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

    @EnableJpaRepositories
    class Config { … }
    ```

    b. To use XML configuration, define a bean similar to the following:
    ```xml
    <?xml version="1.0" encoding="UTF-8"?>
    <beans xmlns="http://www.springframework.org/schema/beans"
        xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
        xmlns:jpa="http://www.springframework.org/schema/data/jpa"
        xsi:schemaLocation="http://www.springframework.org/schema/beans
        https://www.springframework.org/schema/beans/spring-beans.xsd
        http://www.springframework.org/schema/data/jpa
        https://www.springframework.org/schema/data/jpa/spring-jpa.xsd">
    
        <jpa:repositories base-package="com.acme.repositories"/>
    </beans>
    ```
    The JPA namespace is used in this example. If you use the repository abstraction for any other store, you need to change this to the appropriate namespace declaration of your store module. In other words, you should exchange jpa in favor of, for example, mongodb. 
    
    Also, note that the JavaConfig variant does not configure a package explicitly, because the package of the annotated class is used by default. To customize the package to scan, use one of the basePackage… attributes of the data-store-specific repository’s @Enable${store}Repositories-annotation.


 4. Inject the repository instance and use it, as shown in the following example:
    ```java
    class SomeClient {
    
      private final PersonRepository repository;
    
      SomeClient(PersonRepository repository) {
        this.repository = repository;
      }
    
      void doSomething() {
        List<Person> persons = repository.findByLastname("Matthews");
      }
    }
    ```
    
The sections that follow explain each step in detail:
 - Defining Repository Interfaces
 - Defining Query Methods
 - Creating Repository Instances
 - Custom Implementations for Spring Data Repositories


### 4.3. Defining Repository Interfaces
To define a repository interface, you first need to define a domain class-specific repository interface. The interface must extend `Repository and be typed to the domain class and an ID type. If you want to expose CRUD methods for that domain type, extend `CrudRepository instead of `Repository.`

### 4.3.1. Fine-tuning Repository Definition
Typically, your repository interface extends `Repository`, `CrudRepository, or `PagingAndSortingRepository. Alternatively, if you do not want to extend Spring Data interfaces, you can also annotate your repository interface with `@RepositoryDefinition`. Extending `CrudRepository` exposes a complete set of methods to manipulate your entities. If you prefer to be selective about the methods being exposed, copy the methods you want to expose from `CrudRepository` into your domain repository.

> 	Doing so lets you define your own abstractions on top of the provided Spring Data Repositories functionality.

The following example shows how to selectively expose CRUD methods (`findById` and save, in this case):


#### Example 9. Selectively exposing CRUD methods
```java
@NoRepositoryBean
interface MyBaseRepository<T, ID> extends Repository<T, ID> {

  Optional<T> findById(ID id);

  <S extends T> S save(S entity);
}

interface UserRepository extends MyBaseRepository<User, Long> {
  User findByEmailAddress(EmailAddress emailAddress);
}
```
In the prior example, you defined a common base interface for all your domain repositories and exposed `findById(…)` as well as `save(…)`.These methods are routed into the base repository implementation of the store of your choice provided by Spring Data (for example, if you use JPA, the implementation is `SimpleJpaRepository`), because they match the method signatures in `CrudRepository`. So the `UserRepository` can now save users, find individual users by ID, and trigger a query to find Users by email address.

> The intermediate repository interface is annotated with @NoRepositoryBean. Make sure you add that annotation to all repository interfaces for which Spring Data should not create instances at runtime.

### 4.3.2. Using Repositories with Multiple Spring Data Modules
Using a unique Spring Data module in your application makes things simple, because all repository interfaces in the defined scope are bound to the Spring Data module. Sometimes, applications require using more than one Spring Data module. In such cases, a repository definition must distinguish between persistence technologies. When it detects multiple repository factories on the class path, Spring Data enters strict repository configuration mode. Strict configuration uses details on the repository or the domain class to decide about Spring Data module binding for a repository definition:

 1. If the repository definition extends the module-specific repository, it is a valid candidate for the particular Spring Data module.
 2. If the domain class is annotated with the module-specific type annotation, it is a valid candidate for the particular Spring Data module. Spring Data modules accept either third-party annotations (such as JPA’s `@Entity`) or provide their own annotations (such as `@Document` for Spring Data MongoDB and Spring Data Elasticsearch).

The following example shows a repository that uses module-specific interfaces (JPA in this case):

#### Example 10. Repository definitions using module-specific interfaces
```java
interface MyRepository extends JpaRepository<User, Long> { }

@NoRepositoryBean
interface MyBaseRepository<T, ID> extends JpaRepository<T, ID> { … }

interface UserRepository extends MyBaseRepository<User, Long> { … }
```

`MyRepository` and `UserRepository` extend `JpaRepository` in their type hierarchy. They are valid candidates for the Spring Data JPA module.
