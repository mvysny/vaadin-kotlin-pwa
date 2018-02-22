create table Task(
  id bigint auto_increment PRIMARY KEY,
  title varchar(1000) NOT NULL,
  completed BOOL not null,
  created TIMESTAMP not null
);
