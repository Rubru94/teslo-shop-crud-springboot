# Teslo API

## Run dev environment 💻

1. Clone repository

2. Rename file `.env.template` to `.env`.

3. Fill in environment variables in `.env` file.

4. Run database

   ```bash
   $ docker compose up -d
   ```

5. Run clean & install

   ```bash
   $ mvn clean install
   ```

6. Run app

   ```bash
   $ mvn spring-boot:run
   ```

## Stack 🚧

- PostgreSQL
- Springboot

## Tools 🔧

- JDK: `OpenJDK Runtime Environment Corretto-21.0.5.11.1`
- Maven: `Apache Maven 3.9.9`

## Bibliography 📚

- [Amazon Corretto](https://aws.amazon.com/es/corretto/?filtered-posts.sort-by=item.additionalFields.createdDate&filtered-posts.sort-order=desc)
- [How to install Amazon Corretto 21 in Ubuntu 22.04 LTS with JAVA_HOME Environment Variable](https://www.youtube.com/watch?app=desktop&v=sY_9CwGSCJ0)
- [Maven on Linux (Ubuntu)](https://www.digitalocean.com/community/tutorials/install-maven-linux-ubuntu#installing-maven-on-linux-ubuntu)
- [Using Postgres Effectively in Spring Boot Applications](https://hackernoon.com/using-postgres-effectively-in-spring-boot-applications)
- [Error Handling in Spring Boot with @ControllerAdvice](https://freedium.cfd/https://erkanyasun.medium.com/advanced-error-handling-in-spring-boot-with-controlleradvice-2526803890f9)
- [Java Records: A Detailed Guide](https://freedium.cfd/https://erkanyasun.medium.com/understanding-java-records-a-detailed-guide-510ce4a1bc42)
- [Spring Boot - Profiles](./docs/spring-boot-profiles.md)
- [Implement JWT authentication in a Spring Boot application](https://medium.com/@tericcabrel/implement-jwt-authentication-in-a-spring-boot-3-application-5839e4fd8fac)
- [Spring Security with Database for User account and Roles](https://medium.com/@prasanta-paul/spring-security-with-database-for-user-account-and-roles-ec3c7540db6e)
- [VsCode - Java formatting and linting](https://code.visualstudio.com/docs/java/java-linting)

---

<br/>

# HELP.md 🔷

## Read Me

The following was discovered as part of building this project:

- The original package name 'com.teslo.teslo-shop' is invalid and this project uses 'com.teslo.teslo_shop' instead.

## Getting Started

### Reference Documentation

For further reference, please consider the following sections:

- [Official Apache Maven documentation](https://maven.apache.org/guides/index.html)
- [Spring Boot Maven Plugin Reference Guide](https://docs.spring.io/spring-boot/3.4.1/maven-plugin)
- [Create an OCI image](https://docs.spring.io/spring-boot/3.4.1/maven-plugin/build-image.html)
- [Spring Data JPA](https://docs.spring.io/spring-boot/3.4.1/reference/data/sql.html#data.sql.jpa-and-spring-data)
- [Spring Web](https://docs.spring.io/spring-boot/3.4.1/reference/web/servlet.html)

### Guides

The following guides illustrate how to use some features concretely:

- [Accessing Data with JPA](https://spring.io/guides/gs/accessing-data-jpa/)
- [Building a RESTful Web Service](https://spring.io/guides/gs/rest-service/)
- [Serving Web Content with Spring MVC](https://spring.io/guides/gs/serving-web-content/)
- [Building REST services with Spring](https://spring.io/guides/tutorials/rest/)
- [Using the Criteria API to Create Queries](https://jakarta.ee/learn/docs/jakartaee-tutorial/current/persist/persistence-criteria/persistence-criteria.html)

### Maven Parent overrides

Due to Maven's design, elements are inherited from the parent POM to the project POM.
While most of the inheritance is fine, it also inherits unwanted elements like `<license>` and `<developers>` from the parent.
To prevent this, the project POM contains empty overrides for these elements.
If you manually switch to a different parent and actually want the inheritance, you need to remove those overrides.
