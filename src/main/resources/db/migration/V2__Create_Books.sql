CREATE TABLE `books` (
  `id` INT(11) NOT NULL PRIMARY KEY AUTO_INCREMENT,
  `title` VARCHAR(50) NOT NULL,
  `publisher_name` VARCHAR(50) NOT NULL,
  `publish_date` DATE NOT NULL,
  `language` ENUM('ENGLISH', 'PORTUGUESE', 'ITALIAN', 'RUSSIAN') NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `author_book` (
  `author_id` INT(11) NOT NULL,
  `book_id` INT(11) NOT NULL,
  PRIMARY KEY (`author_id`,`book_id`),
  KEY `book_id` (`book_id`),
  CONSTRAINT `author_book_has_author`
    FOREIGN KEY (`author_id`) REFERENCES `authors` (`id`),
  CONSTRAINT `author_book_has_book`
    FOREIGN KEY (`book_id`) REFERENCES `books` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
