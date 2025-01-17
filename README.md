# GraphQL Books

A modern GraphQL API showcasing best practices for building scalable and efficient APIs using Spring Boot and GraphQL Java.

**Project Requirements**

* Java 23
* Maven 3.6+
* Docker and Docker Compose
* PostgreSQL 17+

**Key Features:**

* Schema-first GraphQL API design
* JPA entity relationships
* Batch loading optimization
* Query by Example filtering
* Virtual thread support
* GraphQL client implementation
* Observability integration

## Introduction

This project demonstrates how to build a GraphQL API using Spring Boot that manages a library of books, authors, and reviews. 
It showcases how GraphQL can solve common REST API challenges like overfetching, underfetching, and multiple endpoint calls 
while providing a flexible and efficient way to query data.

## Agenda

- [Why GraphQL](#why-graphql)   
- [Getting Started](#getting-started)
- [Schema First Approach](#schema-first-development)
- [Schema Mapping Inspection Report](#schema-mapping-inspection-report)
- [Data Fetchers](#data-fetchers)
- [Union (Search)](#unions)
- [Performance Improvements](#performance-improvements)
- [Observability](#observability)
- [Data Integration](#data-integration)
- [Client App](#client-app)
- [Federation](#federation)
- [Resources](#resources)
- [Conclusion](#conclusion)

## Why GraphQL

* No more over-fetching
* Multiple Request for multiple resources
* Avoid REST API Explosion of endpoints
* Strongly-typed Schema
* Self Documenting
* Developer Tooling
* Avoids API Versioning

## Getting Started

* Project setup with Spring Boot 3.4.1
* Essential dependencies:
  * Spring Web 
  * Spring for GraphQL
  * Spring Data JPA
  * PostgreSQL
  * Actuator
  * DevTools
  * Zipkin
* Review & Discuss
  * pom.xml (Current Versions of Spring for GraphQL & GraphQL Java )
  * compose.yaml (Docker Compose configuration for local development)
  * application.yaml (Application configuration with GraphiQL UI enabled)
* Book Package
  * Book Class, Repository
* Author Package
  * Author Class, Repository
* Application / DataLoader

## Schema First Development

Unlike REST APIs where the contract is often determined by the implementation, GraphQL promotes a schema-first approach 
where we define our API contract before writing any code. This brings several key advantages:

1. Clear Contract: The schema serves as a single source of truth for both client and server teams
2. Design-Driven Development: Teams can agree on the API design before implementation begins
3. Type Safety: The strongly-typed schema ensures consistency across the entire API
4. Automatic Documentation: The schema is self-documenting and can generate API documentation
5. Better Tooling: IDEs and tools can provide better development experience with type information

### Schema Definition Language (SDL)

https://spec.graphql.org/October2021/

Our books API demonstrates core GraphQL schema concepts:

**Key Components:**

* Object Types & fields
  * Define the shape of your data
  * Properties marked with ! are non-nullable (type modifier)
  * Can reference other types (like Author in Book)
  * Object types are output types
* Scalar Types
  * Built-in: String, Int, Float, Boolean, ID
  * Can be extended with custom scalars
* Operation Types
  * Query
  * Mutation
  * Subscription
* Input Types
  * Special types for mutation arguments
  * Reusable across multiple mutations
* Interfaces, ENUMS, Unions

Create the object types Book & Author: 

```graphql
type Book {
    id: Int!
    title: String!
    author: Author!
}

type Author {
    id: ID!
    name: String!
    books: [Book!]!
}
```
Create a Book Query to return all the books in the system

```graphql
type Query {
    books: [Book!]!
}
```

## Schema Mapping Inspection Report

The Schema Mapping Report is a startup validation feature that ensures your Java implementation matches your GraphQL schema.

**Core Features:**

* Startup Validation
  * Verifies all GraphQL types have corresponding Java implementations
  * Checks for missing data fetchers
  * Reports any schema/code mismatches
* Data Fetcher Detection
  * Maps GraphQL fields to Spring controller methods
  * Recognizes @SchemaMapping, @QueryMapping, and @MutationMapping annotations 
* Smart Type Resolution 
  * Handles interfaces and unions 
  * Automatically discovers implementation types in same package

## Data Fetchers

* Spring's @SchemaMapping annotation usage
* Query resolvers with @QueryMapping
  * books, book
* Mutation resolvers with @MutationMapping
  * addBook
  * with variables first and then an input type
* GraphiQL interactive exploration

### GraphQL Query Examples

```graphql
# find all books
query {
  books {
    id
    title
  }
}

# find book with id of 1
query {
  book(id: 1) {
    id
    title
  }
}

# schema 
type Mutation {
    addBook(title: String!, authorId: Int!): Book!
}

# these are all output types
# what about input types
input BookInput {
    title: String!
    authorId: Int!
}

# query 
mutation {
  addBook(bookInput: {title:"new book", authorId:1}) {
    id
    title
  }
}

# variables
query findBookById($id: Int!) {
  book(id: $id) {
    id
    title
    author {
      id
      name
    }
  }
}
```

## Unions

* Implementation of the SearchItem union type
* Type resolution and polymorphic responses
* Search functionality across multiple types
* Dynamic result handling

```graphql
union SearchItem = Author | Book
```

```java
@Controller
public class SearchController {

    private static final Logger log = LoggerFactory.getLogger(SearchController.class);
    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;

    public SearchController(BookRepository bookRepository, AuthorRepository authorRepository) {
        this.bookRepository = bookRepository;
        this.authorRepository = authorRepository;
    }

    @QueryMapping
    List<Object> search(@Argument String text) {
        log.debug("Searching for '" + text + "'");
        List<Object> results = new ArrayList<>();
        results.addAll(authorRepository.findAllByNameContainsIgnoreCase(text));
        results.addAll(bookRepository.findAllByTitleContainsIgnoreCase(text));
        return results;
    }
}
```

## Performance Improvements

* Performance considerations
* Solving the N+1 query problem
  * Database optimization techniques
  * Making too many controller invocations (n+1)
* Implementing @BatchMapping for efficient data loading
* All of this is happening sequentially on the same tomcat thread (sequentially)
  * Leveraging Project Loom for scalability
  * Configuring virtual thread executors
  * This will enable a VirtualThreadExecutor


## Observability

Observability is the ability to observe the internal state of a running system from the outside. It consists of the three pillars logging, metrics and traces.

Spring projects now have their own, built-in instrumentation for metrics and traces based on the new Observation API from Micrometer

GraphQL is a good use case for Observability in general, as the GraphQL engine can fan out data fetching across REST APIs, data stores, caches, and more.

* Metrics collection using Micrometer
* Trace propagation across services
* GraphQL-specific instrumentation
* Performance monitoring and debugging

## Data Integration

When building APIs, one of the most common challenges developers face is implementing flexible search functionality. 
You often need to support filtering based on multiple optional criteria, leading to complex query logic and verbose repository methods. 
Spring Boot 3.2 introduces a powerful combination: GraphQL with Query by Example (QBE) support, offering an elegant solution to this challenge.

Traditional approaches to implementing dynamic queries often involve writing multiple repository methods or building complex predicates. 
Consider a book management system where users need to search by title, author, or publication year in any combination. 
Your repository might end up looking like this:

```java
public interface BookRepository extends JpaRepository<Book, Long> {
    List<Book> findByTitle(String title);
    List<Book> findByAuthor(String author);
    List<Book> findByPublishedYear(Integer year);
    List<Book> findByTitleAndAuthor(String title, String author);
    List<Book> findByTitleAndPublishedYear(String title, Integer year);
    List<Book> findByAuthorAndPublishedYear(String author, Integer year);
    List<Book> findByTitleAndAuthorAndPublishedYear(String title, String author, Integer year);
}
```

### @GraphQLRepository 

The magic happens with the `@GraphQLRepository` annotation, which automatically creates data fetchers for your GraphQL 
queries based on the repository methods. Combined with QueryByExampleExecutor, it enables dynamic querying without additional code.

```graphql
type Query {
  review(id: Int!): Review
  reviews(filter: ReviewFilter): [Review]!
}

type Review {
  id: ID!
  rating: Int!
  comment: String
  createdAt: String!
  reviewerName: String!
  verified: Boolean!
  book: Book!
}

input ReviewFilter {
  rating: Int
  verified: Boolean
  reviewerName: String
}
```

Example queries demonstrating the review filtering system:

```graphql

# Find verified reviews only
{
  reviews(filter: { verified: true }) {
    reviewerName
    rating
    comment
  }
}

# Find reviews by Sarah Chen
{
  reviews(filter: { reviewerName: "Sarah Chen" }) {
    book {
      title
    }
    reviewerName
    rating
    comment
  }
}
```

**Best Practices and Considerations**

When implementing GraphQL with Query by Example, keep these points in mind:

* Use nullable fields in your input types to make them optional for searching
* Consider adding match modes (exact, contains, starts with) for string fields
* Implement pagination for large result sets
* Add proper validation and error handling

https://www.danvega.dev/blog/spring-boot-graphql-query-by-example


## Client App

* GraphQL Client Implementation
* Type-safe query generation
* Error handling and response parsing
* Integration with Spring's WebClient

## Netflix DGS Integration

The Netflix Domain Graph Service (DGS) Framework is a GraphQL server framework for Java, built on top of Spring Boot.
In March 2024, the DGS Framework introduced an integration with Spring for GraphQL, aiming to unify the GraphQL Java community
and leverage the strengths of both frameworks.

https://docs.spring.io/spring-graphql/reference/codegen.html

## Federation

GraphQL Federation is an architectural pattern and specification that allows you to combine multiple GraphQL services into 
a single unified API. It's particularly useful for large organizations with multiple teams working on different 
parts of the API.

https://github.com/apollographql/federation-jvm-spring-example

## Resources

[Spring for GraphQL - Reference Documentation](https://docs.spring.io/spring-graphql/reference)
[Spring for GraphQL - GitHub](https://github.com/spring-projects/spring-graphql)
[Spring for GraphQL - GitHub Examples](https://github.com/spring-projects/spring-graphql-examples)
[GraphQL Playlist on YouTube](https://www.youtube.com/playlist?list=PLZV0a2jwt22slmUC9iwGGWfRQRIhs1ELa)
[Spring for GraphQL Java Book](https://leanpub.com/graphql-java/)

## Conclusion

This project demonstrates how GraphQL can provide a more efficient and flexible API compared to traditional REST approaches. 
Through features like precise data selection, batch loading, and strong typing, we've shown how to build a scalable and 
maintainable API that better serves both frontend and backend developers.


## Notes

- Agenda Timing
  - Why GrapQL & Getting Started - 15 min
  - Schema First Approach & Schema Inspection - 21 min
  - Data Fetchers - 35 min
  - Unions - 40 min
  - Performance & Observability - 55 min
  - Data Integration - 60min
