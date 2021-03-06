drop table if exists users;
drop table if exists transactions;

create table users (
  id serial unique,
  first_name text not null,
  last_name text not null,
  balance real not null default 0
);

create table transactions (
  id serial unique,
  user_from int,
  user_to int,
  transaction_amount real not null,
  transaction_date timestamp not null default now()
);

update users set balance  = amount where id = userId;

update users set balance = balance - 100 where id = 2;
update users set balance = balance + 100 where id = 1;