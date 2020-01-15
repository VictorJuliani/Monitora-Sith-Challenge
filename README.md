Dev:
 - Review if disabling flyway is the best idea on tests (what if test db is not initialized?)

Tests:
 - Add book repository tests
 - Add book controller tests
 - Make use a separate db for tests
 - Clean up db on tests
 
Doubts:
 - What if a property name had changed in Author? Would I have to create one more extension layer? Or if a property
type had changed, such as name -> firstName + lastName?
 - How to use jOOQ with JPA entities?
 - How to use flyway on tests (to create & clean database)?
 - Why EntityManager is necessary to assert Hibernate exceptions on unit tests?
Why there's none in this example? https://github.com/devdojobr/springboot-essentials/blob/fab7270e52415179d7e329cc2acc5f7592465c66/src/test/java/br/com/devdojo/StudentRepositoryTest.java

Links offline:
 Module 3 - Spring Boot:
  - https://patrickgrimard.io/2014/08/14/how-to-build-a-spring-boot-application-using-intellij-idea/
  - https://blog.mwaysolutions.com/2014/06/05/10-best-practices-for-better-restful-api/


