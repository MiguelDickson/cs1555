----------------------------------------------------------------------------------------------------
--- Name: John Lee, Miguel Dickson (Group 10)
--- Pitt ID: JOL59, LMD90
----------------------------------------------------------------------------------------------------

----------------------------------------------------------------------------------------------------
--- DROP ALL TABLES TO MAKE SURE THE SCHEMA IS CLEAR

DROP TABLE MUTUALFUND CASCADE CONSTRAINTS;
DROP TABLE CLOSINGPRICE CASCADE CONSTRAINTS;
DROP TABLE CUSTOMER CASCADE CONSTRAINTS;
DROP TABLE ADMINISTRATOR CASCADE CONSTRAINTS;
DROP TABLE ALLOCATION CASCADE CONSTRAINTS;
DROP TABLE PREFERS CASCADE CONSTRAINTS;
DROP TABLE TRXLOG CASCADE CONSTRAINTS;
DROP TABLE OWNS CASCADE CONSTRAINTS;
DROP TABLE MUTUALDATE CASCADE CONSTRAINTS;
PURGE RECYCLEBIN;

--DROP ALL FUNCTIONS/PROCEDURES/TRIGGERS AS ABOVE
DROP TRIGGER ON_SALE;


--- Relational schemas: The following schemas should be used.

CREATE TABLE MUTUALDATE(
	CONSTRAINT MUTUALDATE_PK PRIMARY KEY(c_date) INITIALLY IMMEDIATE DEFERRABLE,
	c_date	date
); 

CREATE TABLE MUTUALFUND(
	CONSTRAINT MUTUALFUND_PK PRIMARY KEY(symbol) INITIALLY IMMEDIATE DEFERRABLE,
    CONSTRAINT MUTUALFUND_FK FOREIGN KEY(c_date) REFERENCES MUTUALDATE(c_date) INITIALLY IMMEDIATE DEFERRABLE,
	symbol		varchar(20),
	name		varchar(30),
	description	varchar(100),
	category 	varchar(10),
	c_date		date
);

CREATE TABLE CLOSINGPRICE(
	CONSTRAINT CLOSINGPRICE_PK PRIMARY KEY(symbol, p_date) INITIALLY IMMEDIATE DEFERRABLE,
	CONSTRAINT CLOSINGPRICE_FK FOREIGN KEY(symbol) REFERENCES MUTUALFUND(symbol) INITIALLY IMMEDIATE DEFERRABLE,
    CONSTRAINT CLOSINGPRICE_FK2 FOREIGN KEY(p_date) REFERENCES MUTUALDATE(c_date) INITIALLY IMMEDIATE DEFERRABLE,
	symbol		varchar(20),
	price		float,
	p_date		date
); 

CREATE TABLE CUSTOMER(
	CONSTRAINT CUSTOMER_PK PRIMARY KEY(login) INITIALLY IMMEDIATE DEFERRABLE,
	login 		varchar(10),
	name 		varchar(20),
	email 		varchar(40),
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
	CONSTRAINT ALLOCATION_FK1 FOREIGN KEY(login) REFERENCES CUSTOMER(login) INITIALLY IMMEDIATE DEFERRABLE,
    CONSTRAINT ALLOCATION_FK2 FOREIGN KEY(p_date) REFERENCES MUTUALDATE(c_date) INITIALLY IMMEDIATE DEFERRABLE,
	allocation_no	int,
	login			varchar(10),
	p_date			date
);
	
CREATE TABLE PREFERS(
	CONSTRAINT PREFERS_PK PRIMARY KEY(allocation_no, symbol) INITIALLY IMMEDIATE DEFERRABLE,
	CONSTRAINT PREFERS_FK1 FOREIGN KEY(allocation_no) REFERENCES ALLOCATION(allocation_no) INITIALLY IMMEDIATE DEFERRABLE,
	CONSTRAINT PREFERS_FK2 FOREIGN KEY(symbol) REFERENCES MUTUALFUND(symbol) INITIALLY IMMEDIATE DEFERRABLE,
	allocation_no	int,
	symbol			varchar(20),
	percentage		float
);

CREATE TABLE TRXLOG(
	CONSTRAINT TRXLOG_PK PRIMARY KEY(trans_id) INITIALLY IMMEDIATE DEFERRABLE,
	CONSTRAINT TRXLOG_FK1 FOREIGN KEY(login) REFERENCES CUSTOMER(login) INITIALLY IMMEDIATE DEFERRABLE,
	CONSTRAINT TRXLOG_FK2 FOREIGN KEY(symbol) REFERENCES MUTUALFUND(symbol) INITIALLY IMMEDIATE DEFERRABLE,
    CONSTRAINT TRXLOG_FK3 FOREIGN KEY(t_date) REFERENCES MUTUALDATE(c_date) INITIALLY IMMEDIATE DEFERRABLE,
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

---------------------------------------------------------------------------------------------------------------
--SAMPLE DATA SECTION: SKIP THIS IF READING--

INSERT INTO MUTUALDATE values('06-JAN-14');
INSERT INTO MUTUALDATE values('09-JAN-14');
INSERT INTO MUTUALDATE values('10-JAN-14');
INSERT INTO MUTUALDATE values('11-JAN-14');
INSERT INTO MUTUALDATE values('16-JAN-14');
INSERT INTO MUTUALDATE values('23-JAN-14');
INSERT INTO MUTUALDATE values('30-JAN-14');

INSERT INTO CUSTOMER values('mike', 'Mike', 'mike@betterfuture.com', '1st street', 'pwd', 750);
INSERT INTO CUSTOMER values('mary', 'Mary', 'mary@betterfuture.com', '2nd street', 'pwd', 0);

INSERT INTO MUTUALFUND values('MM', 'money-market', 'money-market,conservative', 'fixed', '06-JAN-14');
INSERT INTO MUTUALFUND values('RE', 'real-estate',  'real estate', 'fixed', '09-JAN-14');

INSERT INTO OWNS values('mike','RE', 50);

commit;

--CREATE OR REPLACE TRIGGER ON_PURCHASE 
--BEFORE UPDATE OF 

--CREATE OR REPLACE TRIGGER ON_SALE
--AFTER 
--DELETE ON OWNS

