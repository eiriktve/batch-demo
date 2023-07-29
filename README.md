# Spring Batch demo
A simple application to illustrate some of the functionality in Spring Batch 5.

The application reads from a csv file, processes it, and stores the data to a postgres db. 

Uses chunk-based processing, as opposed to Tasklets. 

## Use case
The purpose of this repository is to be as a nice reference for setting up a spring batch application. The use
case is quite narrow, without much processing logic, but with all of the basic configuration in place to serve as a
starting point and lookup-tool when approaching a more complex application. 

This implementations shows how to solve some common use cases that are not provided out-of-the-box:
- **Automatic startup** when the application runs. Sometimes you want to just run your batch job as a simple jar rather than have it scheduled, for instance if you have a management system for your batch applications such as Tivoli Workload Scheduler.
- **Does not use the default spring batch tables** for storing job metadata.
- **Working with multiple datasources** while still using declarative configuration of the job. 

## Technologies
- Kotlin with jdk 17
- Spring Boot 3
- Spring Batch 5
- Postgresql
- Jetbrains Exposed

## Run
The application requires a postgres db, see the database section of the README for table structure.

**Run command:**
``java -jar batchdemo-{version}.jar --spring.profiles.active="dev"``

Alternatively, you can run it in IntelliJ.

## Database tables

```roomsql
CREATE TABLE person(
first_name VARCHAR(30) NOT NULL,
last_name VARCHAR(30) NOT NULL,
age INTEGER NOT NULL,
email VARCHAR(60) NOT NULL,
company VARCHAR(60) NOT NULL,
street VARCHAR(50) NOT NULL,
city VARCHAR(30) NOT NULL,
zip_code SMALLINT NOT NULL,
phone VARCHAR(20) NOT NULL,
creation_date TIMESTAMP NOT NULL,
last_updated TIMESTAMP NOT NULL,
id SERIAL PRIMARY KEY); -- auto increments every time a new person is added
```