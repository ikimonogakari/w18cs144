create table Actors(name VARCHAR(40), movie VARCHAR(80), year INTEGER, role VARCHAR(40));
load data local infile 'actors.csv' into table Actors
fields terminated by ','
optionally enclosed by '"';
select name from Actors where movie='Die Another Day';
drop table Actors;
