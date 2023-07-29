# Spring Batch demo
A simple application to illustrate some of the functionality in Spring Batch 5.

The application reads from a csv file, processes it, and stores the data to a postgres db. 

Uses chunk-based processing, as opposed to Tasklets. 

## Technologies
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