# BatchDemo
A simple application to illustrate some of the functionality in Spring Batch.

Made in relation to a presentation on Spring Batch to ITVerket.

## Technologies
- Spring Boot 3
- Spring Batch 5
- Postgresql

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