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

#### Example 6. `PagingAndSortingRepository` interface
```java
public interface PagingAndSortingRepository<T, ID> extends CrudRepository<T, ID> {

  Iterable<T> findAll(Sort sort);

  Page<T> findAll(Pageable pageable);
}
```
