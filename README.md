# MS Acces + Spring Boot + Hibernate

A sample project which utilizes MS Access with Hibernate 5 and Spring Boot.

Please see [this Medium article](https://medium.com/@tobias.strg/accessing-ms-access-with-hibernate-5-and-spring-boot-1b58f26ebb7d) for explanation of _why_ anyone would do this.

This is only a bare-mininum implementation, and includes a sample database. Use `gradlew bootRun` to run the application.

**To run this example, please insert the _absolute_ paths (e.g. "C:/git/msaccess__hibernate_spring/test.accdb") to the included test.accdb in this file:**

`src\main\java\comp\project\backend\jpa\config\DataSourceConfig.java`

This project includes both examples of standard Spring queries (PartController) but also features examples of how (not) to do SQL queries (ParameterController).
