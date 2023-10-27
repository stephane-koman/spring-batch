CREATE TABLE employee (
    id int primary key,
    name varchar(100),
    salary int
);

CREATE SEQUENCE hibernate_sequence START 1;

INSERT INTO employee VALUES (1,'name01',1000);
INSERT INTO employee VALUES (2,'name02',2000);
INSERT INTO employee VALUES (3,'name03',3000);
