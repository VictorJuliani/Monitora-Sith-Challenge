CREATE TABLE authors (
  id int NOT NULL PRIMARY KEY AUTO_INCREMENT,
  first_name varchar(50) NOT NULL,
  last_name varchar(50) NOT NULL,
  birthdate date NOT NULL,
  distinguished boolean NOT NULL DEFAULT false
) ENGINE=InnoDB DEFAULT CHARSET=utf8;