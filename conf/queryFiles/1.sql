﻿create table users(
	id serial PRIMARY KEY,
	username varchar(50) not null,
	password varchar(100) not null,
	email varchar(50) not null,
	first_name varchar(20) not null,
	prefix varchar(20),
	last_name varchar(20) not null
);
create table cloud_service (
	id serial PRIMARY KEY,
	user_id Int NOT NULL REFERENCES users(id),
	api_key varchar(100) NOT NULL
)
