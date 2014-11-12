----------------------------------------------------------------------------------------------------
--- Name: John Lee
--- Pitt ID: JOL59
--- HW5 Queries
--- Works with "hw5-sample-data.sql"
----------------------------------------------------------------------------------------------------

----------------------------------------------------------------------------------------------------
--- 1. Edit the sample solution script of Assignment #4 in such a way that all constraints are
--- INITIALLY IMMEDIATE DEFERRABLE and then use it to create the database and populate the database
--- with the sample data.
----------------------------------------------------------------------------------------------------
--- DROP ALL TABLES TO MAKE SURE THE SCHEMA IS CLEAR

DROP TABLE MUTUALFUND CASCADE CONSTRAINTS;
DROP TABLE CLOSINGPRICE CASCADE CONSTRAINTS;
DROP TABLE CUSTOMER CASCADE CONSTRAINTS;
DROP TABLE ALLOCATION CASCADE CONSTRAINTS;
DROP TABLE PREFERS CASCADE CONSTRAINTS;
DROP TABLE TRXLOG CASCADE CONSTRAINTS;
DROP TABLE OWNS CASCADE CONSTRAINTS;
DROP TABLE MUTUALDATE CASCADE CONSTRAINTS;
PURGE RECYCLEBIN;

--- Relational schemas: The following schemas should be used.
CREATE TABLE MUTUALFUND(
	CONSTRAINT MUTUALFUND_PK PRIMARY KEY(symbol) INITIALLY IMMEDIATE DEFERRABLE,
	symbol		varchar(20),
	name		varchar(30),
	description	varchar(100),
	category: 	varchar(10),
	c_date		date
);

CREATE TABLE CLOSINGPRICE(
	CONSTRAINT CLOSINGPRICE_PK PRIMARY KEY(symbol, p_date) INITIALLY IMMEDIATE DEFERRABLE,
	CONSTRAINT CLOSINGPRICE_FK FOREIGN KEY(symbol) REFERENCES MUTUALFUND(symbol) INITIALLY IMMEDIATE DEFERRABLE,
	symbol		varchar(20),
	price		float,
	p_date		date
); 

CREATE TABLE CUSTOMER(
	CONSTRAINT CUSTOMER_PK PRIMARY KEY(login) INITIALLY IMMEDIATE DEFERRABLE,
	login 		varchar(10),
	name 		varchar(20),
	email 		varchar(20),
	address		varchar(30),
	password 	varchar(10),
	balance 	float
); 

CREATE TABLE ADMINISTRATOR(
	CONSTRAINT ADMINISTRATOR_PK PRIMARY KEY(login) INITIALLY IMMEDIATE DEFERRABLE,
	login 		varchar(10),
	name 		varchar(20),
	email 		varchar(20),
	address		varchar(30),
	password 	varchar(10)
); 

CREATE TABLE ALLOCATION(
	CONSTRAINT ALLOCATION_PK PRIMARY KEY(allocation_no) INITIALLY IMMEDIATE DEFERRABLE,
	CONSTRAINT ALLOCATION_FK FOREIGN KEY(login) REFERENCES CUSTOMER(login) INITIALLY IMMEDIATE DEFERRABLE,
	allocation_no	int,
	login			varchar(10),
	p_date			date
);
	
CREATE TABLE PREFERS(
	CONSTRAINT ALLOCATION_PK PRIMARY KEY(allocation_no, symbol) INITIALLY IMMEDIATE DEFERRABLE,
	CONSTRAINT ALLOCATION_FK1 FOREIGN KEY(allocation_no) REFERENCES ALLOCATION(allocation_no) INITIALLY IMMEDIATE DEFERRABLE,
	CONSTRAINT ALLOCATION_FK2 FOREIGN KEY(symbol) REFERENCES MUTUALFUND(symbol) INITIALLY IMMEDIATE DEFERRABLE,
	allocation_no	int,
	symbol			varchar(20),
	percentage		float
);

CREATE TABLE TRXLOG(
	CONSTRAINT TRXLOG_PK PRIMARY KEY(trans_id) INITIALLY IMMEDIATE DEFERRABLE,
	CONSTRAINT TRXLOG_FK1 FOREIGN KEY(login) REFERENCES CUSTOMER(login) INITIALLY IMMEDIATE DEFERRABLE,
	CONSTRAINT TRXLOG_FK2 FOREIGN KEY(symbol) REFERENCES MUTUALFUND(symbol) INITIALLY IMMEDIATE DEFERRABLE,
	trans_id	int,
	login		varchar(10),
	symbol		varchar(20),
	t_date		date,
	action		varchar(10),
	--	A deposit transaction should trigger a set of buy transactions showing that the money deposited into a customer's account will automatically be invested to mutual funds based on their preferences.
	num_shares	int,
	price		float,
	amount		float
); 

CREATE TABLE OWNS(
	CONSTRAINT OWNS_PK PRIMARY KEY(login, symbol) INITIALLY IMMEDIATE DEFERRABLE,
	CONSTRAINT OWNS_FK1 FOREIGN KEY(login) REFERENCES CUSTOMER(login) INITIALLY IMMEDIATE DEFERRABLE,
	CONSTRAINT OWNS_FK2 FOREIGN KEY(symbol) REFERENCES MUTUALFUND(symbol) INITIALLY IMMEDIATE DEFERRABLE,
	login	varchar(10),
	symbol	varchar(20),
	shares	int
);

CREATE TABLE OWNS(
	CONSTRAINT OWNS_PK PRIMARY KEY(login, symbol) INITIALLY IMMEDIATE DEFERRABLE,
	c_date	date
); 