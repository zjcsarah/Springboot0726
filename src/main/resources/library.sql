ALTER TABLE user ADD COLUMN phone varchar(20) DEFAULT NULL;

DROP TABLE IF EXISTS book;
CREATE TABLE book (
  id int(10) NOT NULL AUTO_INCREMENT,
  title varchar(100) NOT NULL,
  author varchar(50) NOT NULL,
  isbn varchar(20) DEFAULT NULL,
  category varchar(50) DEFAULT NULL,
  publisher varchar(100) DEFAULT NULL,
  publish_date date DEFAULT NULL,
  description text,
  total_quantity int(10) NOT NULL DEFAULT 1,
  available_quantity int(10) NOT NULL DEFAULT 1,
  create_time datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS borrow_record;
CREATE TABLE borrow_record (
  id int(10) NOT NULL AUTO_INCREMENT,
  user_id int(10) NOT NULL,
  book_id int(10) NOT NULL,
  borrow_date datetime DEFAULT CURRENT_TIMESTAMP,
  due_date datetime DEFAULT NULL,
  return_date datetime DEFAULT NULL,
  status varchar(20) NOT NULL DEFAULT 'BORROWED',
  PRIMARY KEY (id),
  INDEX idx_user_id (user_id),
  INDEX idx_book_id (book_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

INSERT INTO book (id, title, author, isbn, category, publisher, publish_date, description, total_quantity, available_quantity, create_time) VALUES
(1, 'Java Programming Thoughts', 'Bruce Eckel', '9787111213826', 'Computer', 'China Machine Press', '2007-06-01', 'Java classic book', 5, 5, NOW()),
(2, 'Spring in Action', 'Craig Walls', '9787115417305', 'Computer', 'Posts & Telecom Press', '2016-04-01', 'Spring framework guide', 3, 3, NOW()),
(3, 'Three Body', 'Liu Cixin', '9787536692930', 'Sci-Fi', 'Chongqing Press', '2008-01-01', 'Hugo Award winner', 10, 10, NOW()),
(4, 'To Live', 'Yu Hua', '9787532125982', 'Literature', 'Writer Press', '1993-06-01', 'A story of an ordinary life', 4, 4, NOW()),
(5, 'MySQL Crash Course', 'Ben Forta', '9787115191120', 'Computer', 'Posts & Telecom Press', '2009-01-01', 'MySQL intro classic', 6, 6, NOW());