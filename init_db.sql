drop table if exists movie;

create table movie (
	id serial primary key, 
	title varchar(100) not null, 
	director varchar(50) not null,
	genre varchar(300) not null,
       	release timestamp not null
);

