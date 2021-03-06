----------------------------------------------------------------------------------------------------
--- Name: John Lee, Miguel Dickson (Group 10)
--- Pitt ID: JOL59, LMD90
---
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
DROP VIEW LATESTPRICE CASCADE CONSTRAINTS;
PURGE RECYCLEBIN;


--DROP ALL FUNCTIONS/PROCEDURES/TRIGGERS AS ABOVE
--DROP FUNCTION new_balance;
--DROP FUNCTION sale_proceeds;
--DROP TRIGGER ON_SALE;


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
INSERT INTO MUTUALDATE values('28-MAR-14');
INSERT INTO MUTUALDATE values('29-MAR-14');
INSERT INTO MUTUALDATE values('30-MAR-14');
INSERT INTO MUTUALDATE values('31-MAR-14');
INSERT INTO MUTUALDATE values('01-APR-14');
INSERT INTO MUTUALDATE values('02-APR-14');
INSERT INTO MUTUALDATE values('03-APR-14');


INSERT INTO ADMINISTRATOR values('admin', 'Administrator', 'admin@gmail.com', 'somewhere street', 'root');

INSERT INTO CUSTOMER values('mike', 'Mike', 'mike@betterfuture.com', '1st street', 'pwd', 0);
INSERT INTO CUSTOMER values('mary', 'Mary', 'mary@betterfuture.com', '2nd street', 'pwd', 0);

INSERT INTO MUTUALFUND values('MM', 'money-market', 'money-market,conservative', 'fixed', '06-JAN-14');
INSERT INTO MUTUALFUND values('RE', 'real-estate',  'real estate', 'fixed', '09-JAN-14');
INSERT INTO MUTUALFUND values('STB', 'short-term-bonds', 'short term bonds', 'bonds', '06-JAN-14');
INSERT INTO MUTUALFUND values('GS', 'general-stocks',  'general stocks', 'stocks', '09-JAN-14');

INSERT INTO CLOSINGPRICE values('RE', 10000, '03-APR-14');
INSERT INTO CLOSINGPRICE values('MM', 1000, '03-APR-14');
INSERT INTO CLOSINGPRICE values('STB', 100, '03-APR-14');
INSERT INTO CLOSINGPRICE values('GS', 10, '03-APR-14');


INSERT INTO OWNS values('mike','RE', 50);

INSERT INTO ALLOCATION values('0', 'mike', '28-MAR-14');
INSERT INTO ALLOCATION values('1', 'mary', '28-MAR-14');
INSERT INTO ALLOCATION values('2', 'mike', '03-APR-14');

INSERT INTO PREFERS values('2', 'MM', .1);
INSERT INTO PREFERS values('2', 'RE', .4);
INSERT INTO PREFERS values('2', 'STB', .3);
INSERT INTO PREFERS values('2', 'GS', .2);

--INSERT INTO CLOSINGPRICE values('MM', 10, '28-MAR-14');
--INSERT INTO CLOSINGPRICE values('MM', 11, '29-MAR-14');
--INSERT INTO CLOSINGPRICE values('MM', 12, '30-MAR-14');
INSERT INTO CLOSINGPRICE values('MM', 15, '31-MAR-14');
INSERT INTO CLOSINGPRICE values('MM', 14, '01-APR-14');
INSERT INTO CLOSINGPRICE values('MM', 15, '02-APR-14');

--INSERT INTO CLOSINGPRICE values('RE', 10, '28-MAR-14'); 
--INSERT INTO CLOSINGPRICE values('RE', 11, '29-MAR-14');
--INSERT INTO CLOSINGPRICE values('RE', 12, '30-MAR-14');
INSERT INTO CLOSINGPRICE values('RE', 15, '31-MAR-14');
INSERT INTO CLOSINGPRICE values('RE', 14, '01-APR-14');
INSERT INTO CLOSINGPRICE values('RE', 15, '02-APR-14');

--INSERT INTO CLOSINGPRICE values('STB', 10, '28-MAR-14'); 
--INSERT INTO CLOSINGPRICE values('STB', 11, '29-MAR-14');
--INSERT INTO CLOSINGPRICE values('STB', 12, '30-MAR-14');
INSERT INTO CLOSINGPRICE values('STB', 15, '31-MAR-14');
INSERT INTO CLOSINGPRICE values('STB', 14, '01-APR-14');
INSERT INTO CLOSINGPRICE values('STB', 15, '02-APR-14');

--INSERT INTO CLOSINGPRICE values('GS', 10, '28-MAR-14'); 
--INSERT INTO CLOSINGPRICE values('GS', 11, '29-MAR-14');
--INSERT INTO CLOSINGPRICE values('GS', 12, '30-MAR-14');
INSERT INTO CLOSINGPRICE values('GS', 15, '31-MAR-14');
INSERT INTO CLOSINGPRICE values('GS', 14, '01-APR-14');
INSERT INTO CLOSINGPRICE values('GS', 15, '02-APR-14');

SET SERVEROUTPUT ON;

---------------------------------------------------------------------------------------------------------------

  /* --------- */
 /* --VIEWS-- */
/* --------- */

-- LASTUPDATE is a view that displays all funds' latest dates.
CREATE OR REPLACE VIEW LASTUPDATE
AS
	SELECT symbol, max(p_date) AS l_date
	FROM CLOSINGPRICE
	GROUP BY symbol;
   
-- LATESTPRICE is a view that displays all funds' latest prices.
CREATE OR REPLACE VIEW LATESTPRICE
AS
	SELECT b.symbol, b.price, b.p_date
	FROM LASTUPDATE a JOIN CLOSINGPRICE b
	ON b.symbol = a.symbol AND b.p_date = a.l_date;

  /* ------------ */
 /* --TRIGGERS-- */
/* ------------ */

-- ON_DEPOSIT, upon a decrement in balance, increases the user's stocks according to their buying preferences.
CREATE OR REPLACE TRIGGER ON_DEPOSIT
AFTER 
UPDATE on CUSTOMER
FOR EACH ROW
WHEN (new.balance < old.balance)
DECLARE 
    recent_alloc number;
    money_alloc number;
    shares_bought number;
    numb_prefs number;
    price_shares number;
    owned_shares number;
BEGIN
    recent_alloc:= get_last_allocation(:new.login);
    numb_prefs:= get_number_preferences(recent_alloc);
    
    for i in 1..numb_prefs LOOP
		owned_shares := has_shares(:new.login, get_n_prefsymbol(i,recent_alloc));
		-- dbms_output.put_line(i || '. owned_shares: ' || owned_shares);
		money_alloc := get_n_preference(i, recent_alloc) * :old.balance;
		-- dbms_output.put_line(i || '. money_alloc: ' || money_alloc);
		-- dbms_output.put_line(i || '. get_n_preference(i, recent_alloc): ' || get_n_preference(i, recent_alloc));
		-- dbms_output.put_line(i || '. :old.balance: ' || :old.balance);
		shares_bought := FLOOR(money_alloc / get_last_closing_price(get_n_prefsymbol(i, recent_alloc)));
		IF owned_shares = 1
		THEN
			UPDATE OWNS set shares = shares + shares_bought WHERE login = :new.login AND symbol = get_n_prefsymbol(i,recent_alloc);
			ELSE
			INSERT into OWNS values(:new.login, get_n_prefsymbol(i,recent_alloc), shares_bought);
		END IF;
		dbms_output.put_line('Bought ' || shares_bought || ' shares of ' || get_n_prefsymbol(i,recent_alloc) || '.');
		add_trx(:new.login, get_n_prefsymbol(i,recent_alloc), 'buy', shares_bought, get_last_closing_price(get_n_prefsymbol(i, recent_alloc)), shares_bought * get_last_closing_price(get_n_prefsymbol(i, recent_alloc)));
	end loop;
END;
/

-- ON_SALE, upon a decrease in shares, increases the seller's balance by the value of the sold shares.
CREATE OR REPLACE TRIGGER ON_SALE
AFTER 
UPDATE on OWNS
FOR EACH ROW
WHEN (new.shares < old.shares)
DECLARE
    newdate date;
BEGIN    
    SELECT l_date INTO newdate
    FROM LASTUPDATE
    WHERE symbol = :new.symbol;
    
    UPDATE CUSTOMER set balance = balance + calc_value(:new.symbol, (:old.shares - :new.shares), newdate) WHERE login = :new.login;
END;
/
	
  /* ------------- */
 /* --FUNCTIONS-- */
/* ------------- */
	
-- new_balance returns the sum of a balance 'x' and an incremental number 'incr_val'.
CREATE OR REPLACE FUNCTION new_balance(x in number, incr_val in number)
	RETURN number IS final_val number;
BEGIN
	final_val := x + incr_val;
	RETURN(final_val);
END;
/

-- calc_value returns the value of a number of shares 'shares' for a fund 'symbol' on a date 'sell_date'.
CREATE OR REPLACE FUNCTION calc_value(sym in varchar, shares in number, c_date date)
    RETURN number IS proceeds number;
c_price number;
BEGIN
    SELECT price INTO c_price
    FROM CLOSINGPRICE
    WHERE p_date = c_date AND symbol = sym;
    
    proceeds := c_price * shares;
    --dbms_output.put_line(proceeds);
    RETURN(proceeds);
END;
/

-- has_shares returns a 1 if user 'log_name' owns shares in 'symb'.
CREATE OR REPLACE FUNCTION has_shares(log_name in varchar, symb in varchar)
    RETURN number IS bool_shares number;
num_results number;
BEGIN
    SELECT COUNT(*) into num_results
    FROM OWNS
    WHERE login = log_name AND symbol = symb;
    IF num_results = 0 
	THEN
        --dbms_output.put_line(log_name || 'owns no shares of ' || symb);
        RETURN(0);
    ELSE
        --dbms_output.put_line(log_name || 'owns some shares of ' || symb);  
        RETURN(1);
    END IF;       
END;
/

-- get_last_allocation returns the last allocation of user 'log_name'.
CREATE OR REPLACE FUNCTION get_last_allocation(log_name in varchar)
    RETURN number IS last_alloc number;
BEGIN   
    select * into last_alloc
    from (select allocation_no from allocation where login = log_name order by p_date DESC) S
    where rownum <=1
    ORDER by rownum;    
    --dbms_output.put_line(last_alloc);
    RETURN(last_alloc);
END;
/

-- get_number_preferences returns the current number preferences of an allocation 'alloc_no'.
CREATE OR REPLACE FUNCTION get_number_preferences(alloc_no in number)
    RETURN number is num_pref number;
BEGIN   
    select count(symbol) into num_pref
    from prefers
    where allocation_no = alloc_no;
    --dbms_output.put_line(num_pref);
    RETURN(num_pref);
END;
/

-- get_last_closing_price returns the last closing price of a fund 'share_name'.
CREATE OR REPLACE FUNCTION get_last_closing_price(share_name in varchar)
    RETURN number IS close_price number;
BEGIN   
    select * into close_price
    from (select price from CLOSINGPRICE where symbol = share_name order by p_date DESC) S
    where rownum <=1
    ORDER by rownum;    
    --dbms_output.put_line(close_price);
    RETURN(close_price);
END;
/

-- get_n_preference returns the 'nth' preference of an allocation 'alloc'.
CREATE OR REPLACE FUNCTION get_n_preference(nth in number, alloc in number)
    RETURN float IS npref float;
BEGIN
           SELECT percentage into npref
           from
               (SELECT percentage, rownum as snum
                FROM
                    (select * FROM PREFERS where alloc = allocation_no ORDER BY percentage DESC)
                )
            WHERE snum = nth;
      --dbms_output.put_line(npref);
      RETURN(npref);
END;
/

-- get_n_prefsymbol returns the 'nth' preference symbol of an allocation 'alloc'.
CREATE OR REPLACE FUNCTION get_n_prefsymbol(nth in number, alloc in number)
    RETURN varchar IS symb varchar(20);
BEGIN
           SELECT symbol into symb
           from
               (SELECT symbol, percentage, rownum as snum
                FROM
                    (select * FROM PREFERS where alloc = allocation_no ORDER BY percentage DESC)
                )
            WHERE snum = nth;
      --dbms_output.put_line(symb);
      RETURN(symb);
END;
/

  /* -------------- */
 /* --PROCEDURES-- */
/* -------------- */

-- deposit adds an amount of money to a user's balance, calculates if purchases based on the user's allocation preferences can be made, and decrements the balance by that amount if possible.
CREATE OR REPLACE PROCEDURE deposit(logi IN varchar, amoun IN float, success OUT number)
AS
    old_balance number;
	shares_not_purchased number;
	recent_alloc number;
	money_alloc number;
    shares_bought number;
    numb_prefs number;
    before_buy number;
    after_buy number;
    price_shares number;
    owned_shares number;
BEGIN
    IF amoun < 0
	THEN
        dbms_output.put_line('Cannot deposit negative money bro!!!');
		success := 0;
    ELSE
        SELECT balance INTO old_balance
        FROM CUSTOMER
        WHERE login = logi;
	
        before_buy := old_balance + amoun;
		UPDATE CUSTOMER set balance = before_buy WHERE login = logi; -- This should not trigger anything, as it is an increase in balance.
		
		after_buy := before_buy; -- Will be decremented by the total price of all shares.
		
		shares_not_purchased := 0; -- Flag for whether or not a share was too expensive to be obtained
		recent_alloc := get_last_allocation(logi);
		numb_prefs:= get_number_preferences(recent_alloc);
		
		for i in 1..numb_prefs LOOP
			money_alloc := get_n_preference(i, recent_alloc) * before_buy;
			--dbms_output.put_line('This amount of the current balance of the allocated amount is allocated:');
			--dbms_output.put_line(money_alloc);
			shares_bought := FLOOR(money_alloc / get_last_closing_price(get_n_prefsymbol(i, recent_alloc)));
			IF (shares_bought = 0)
			THEN
				shares_not_purchased:=1;
			END IF;        
			--dbms_output.put_line('This number of shares were purchased:');
			--dbms_output.put_line(shares_bought);
			after_buy := after_buy - (shares_bought * (get_last_closing_price(get_n_prefsymbol(i,recent_alloc))));
			--dbms_output.put_line('Balance is now:');
			--dbms_output.put_line(after_buy);    
		END LOOP; 
		
			--dbms_output.put_line('End balance is:');
			--dbms_output.put_line(after_buy);    
			
		IF (shares_not_purchased = 1)
		THEN
			dbms_output.put_line('Was unable to buy some shares and so placing the deposit into balance entirely.');
			success := 1;
		ELSE
			UPDATE CUSTOMER set balance = after_buy WHERE login = logi;
			add_trx(logi, NULL, 'deposit', NULL, NULL, before_buy - after_buy);
			success := 2;
		END IF;
		
    END IF;
END;
/

-- sale subtracts a number of shares of a fund, and adds its sale value to the user's balance.
CREATE OR REPLACE PROCEDURE sale(logi IN varchar, symb IN varchar, numshare IN number, proceeds OUT float, success OUT number)
AS
    has_shares number;
BEGIN
    execute immediate 'ALTER TRIGGER on_deposit DISABLE';
    IF numshare < 0
	THEN
        dbms_output.put_line('Cannot sell negative shares bro!!!');
		success := 0;
    ELSE
        SELECT shares INTO has_shares
        FROM OWNS
        WHERE login = logi AND symbol = symb; 

        IF numshare <= has_shares   
		THEN
            dbms_output.put_line('Selling shares!');
            UPDATE OWNS set shares = has_shares - numshare WHERE login = logi AND symbol = symb;
			proceeds := numshare * get_last_closing_price(symb);
			add_trx(logi, symb, 'sell', numshare, get_last_closing_price(symb), proceeds);
			success := 1;
		ELSE
            dbms_output.put_line('Cannot sell more shares than you have, bro!!');
			proceeds := 0;
			success := 0;
        END IF;
		
		IF has_shares - numshare = 0
		THEN
			DELETE FROM OWNS WHERE login = logi AND symbol = symb;
		END IF;
    END IF;
    execute immediate 'ALTER TRIGGER on_deposit ENABLE';
END;
/

-- purchase1 adds a number of shares of a fund, and subtracts its buy value from the user's balance.
CREATE OR REPLACE PROCEDURE purchase1(logi IN varchar, symb IN varchar, numshare IN number, price_shares OUT number, success OUT number)
AS
    curr_price number;
    curr_balance number;
BEGIN
    execute immediate 'ALTER TRIGGER on_deposit DISABLE';
    IF numshare <= 0
	THEN
        dbms_output.put_line('Must buy at least one share, bro!!!');
		success := 0;
    ELSE        
        SELECT balance INTO curr_balance
        FROM CUSTOMER
        WHERE login = logi;
        
        SELECT price INTO curr_price
        FROM LATESTPRICE
        WHERE symbol = symb;
        
        price_shares := curr_price * numshare;
        
        IF curr_balance < price_shares
		THEN
            dbms_output.put_line('Cannot buy more shares than you can afford, bro!!');
			success := 0;
        ELSE
			IF has_shares(logi, symb) = 1
			THEN
				UPDATE OWNS set shares = shares + numshare WHERE login = logi AND symbol = symb;
			ELSE
				INSERT INTO OWNS values(logi, symb, numshare);
			END IF;
			UPDATE CUSTOMER set balance = curr_balance - price_shares WHERE login = logi;
			add_trx(logi, symb, 'buy', numshare, curr_price, price_shares);
			success := 1;
        END IF;
    END IF;
    execute immediate 'ALTER TRIGGER on_deposit ENABLE';
END;
/

-- purchase2 adds as many shares of a fund as the given amount can afford, and subtracts the price from the user's balance.
CREATE OR REPLACE PROCEDURE purchase2(logi IN varchar, symb IN varchar, amount IN number, shares_bought OUT number, final_cost OUT float, success OUT number)
AS
    curr_price number;
    curr_balance number;
BEGIN
    execute immediate 'ALTER TRIGGER on_deposit DISABLE';
    IF amount <= 0
	THEN
        dbms_output.put_line('Must spend something, bro!!!');
		success := 0;
    ELSE        
        SELECT balance INTO curr_balance
        FROM CUSTOMER
        WHERE login = logi;
        
		-- First, check if amount given is greater than balance.
		IF curr_balance < amount
		THEN
			dbms_output.put_line('Cannot spend more than your current balance, bro!!');
			success := 0;
		ELSE
			SELECT price INTO curr_price
			FROM LATESTPRICE
			WHERE symbol = symb;
			
			-- Calculate how many shares you can purchase.
			shares_bought := FLOOR(amount / curr_price);
			
			-- Check if any were bought.
			IF shares_bought = 0
			THEN
				dbms_output.put_line('Cannot afford a single share, bro!!');
				success := 1;
			ELSE
				IF has_shares(logi, symb) = 1
				THEN
					UPDATE OWNS set shares = shares + shares_bought WHERE login = logi AND symbol = symb;
				ELSE
					INSERT INTO OWNS values(logi, symb, shares_bought);
				END IF;
				-- dbms_output.put_line(curr_balance);
				-- dbms_output.put_line(shares_bought);
				-- dbms_output.put_line(get_last_closing_price(symb));
				final_cost := shares_bought * curr_price;
				UPDATE CUSTOMER set balance = (curr_balance - final_cost) WHERE login = logi;
				add_trx(logi, symb, 'buy', shares_bought, curr_price, final_cost);
				success := 2;
			END IF;
		END IF;
    END IF;
    execute immediate 'ALTER TRIGGER on_deposit ENABLE';
END;
/

-- funds_in_range prints out all current fund prices within a given range.
CREATE OR REPLACE PROCEDURE funds_in_range(lo_limit IN float DEFAULT 0, hi_limit IN float DEFAULT 99999999)
AS
BEGIN
	DBMS_OUTPUT.put_line('Prices within the range $' || lo_limit || ' and $' || hi_limit || ':');
	FOR fund_rec IN (
        SELECT symbol, price 
		FROM LATESTPRICE
		WHERE (price >= lo_limit) AND (price <= hi_limit)
		ORDER BY price ASC )
	LOOP
		DBMS_OUTPUT.put_line (fund_rec.symbol || ':' || chr(9) || '$' || fund_rec.price);
	END LOOP;
END;
/

-- add_trx adds a new transaction record to TRXLOG, taking in as few inputs as possible.
CREATE OR REPLACE PROCEDURE add_trx(logi IN varchar, symb IN varchar, action IN varchar, num_shares IN number, curr_price IN number, total_amount IN number)
AS
	num_trx number;
	last_tid number;
	curr_date date;
BEGIN
	-- Check if TRXLOG has tuples.
	SELECT COUNT(*) INTO num_trx
	FROM TRXLOG;
	
	-- If not, start at trans_id of 0. Otherwise, increment from largest trans_id.
	IF num_trx = 0
	THEN
		last_tid := 0;
	ELSE
		SELECT MAX(trans_id) INTO last_tid
		FROM TRXLOG;
	END IF;
	
	SELECT MAX(c_date) INTO curr_date
	FROM MUTUALDATE;
	
	INSERT INTO TRXLOG values(last_tid + 1, logi, symb, curr_date, action, num_shares, curr_price, total_amount);
END;
/

  /* ----------- */
 /* --TESTING-- */
/* ----------- */

/*
-- Testing deposit with lots of money
exec deposit('mike', 99999);
select * from owns;
select * from customer;
-- Testing deposit with too little money to fulfill the user's autobuy preferences
exec deposit('mike', 100);
select * from owns;
select * from customer;

-- Testing sale with the exact number of shares owned, checking for delete
exec sale('mike', 'RE', 50);
select * from owns;
select * from customer;

-- Testing purchase1, simple case
exec purchase1('mike', 'RE', 1);
select * from owns;
select * from customer;
-- Testing purchase2
	-- More than balance
	exec purchase2('mike', 'RE', 99999999);
	select * from owns;
	select * from customer;
	-- Less than stock price
	exec purchase2('mike', 'RE', 1);
	select * from owns;
	select * from customer;
	-- Just right
	exec purchase2('mike', 'RE', 10000);
	select * from owns;
	select * from customer;
	-- More than just right
	exec purchase2('mike', 'RE', 251234);
	select * from owns;
	select * from customer;


/*

--TESTING SALE TRANSACTION
PROMPT :::TESTING SALE TRANSACTION:::;
PROMPT SALE: CUSTOMER for Mike before;
select * from customer where login = 'mike';
PROMPT SALE: OWNS for Mike before;


INSERT INTO TRXLOG values('0', 'mike', 'RE', '03-APR-14', 'sell', 10, 15, 150);

PROMPT SALE: TRXLOG for Mike after;
select * from trxlog;
PROMPT SALE: CUSTOMER for Mike after;
select * from customer where login = 'mike';
PROMPT SALE: OWNS for Mike after;
select * from owns where login = 'mike';

--
--TESTING BUY TRANSACTION
PROMPT :::TESTING BUY TRANSACTION:::;
INSERT INTO TRXLOG values('1', 'mike', 'GS', '03-APR-14', 'buy', 10, 15, 150);

PROMPT BUY: TRXLOG for Mike after;
select * from trxlog where trans_id='1';
PROMPT BUY: CUSTOMER for Mike after;
select * from customer where login = 'mike';
PROMPT BUY: OWNS for Mike after;
select * from owns where login = 'mike';

--
--TESTING DEPOSIT TRANSACTION
PROMPT :::TESTING DEPOSIT TRANSACTION:::;
PROMPT DEPOSIT: CUSTOMER for Mike before;
select * from customer where login = 'mike';
PROMPT DEPOSIT: OWNS for Mike before;
select * from owns where login = 'mike';

INSERT INTO TRXLOG values('2', 'mike', 'RE', '03-APR-14', 'deposit', NULL, NULL, 1000000);

PROMPT DEPOSIT: CUSTOMER for Mike after;
select * from customer where login = 'mike';
PROMPT DEPOSIT: OWNS for Mike after;
select * from owns where login = 'mike';

--
--TESTING funds_in_range PROCEDURE
PROMPT :::TESTING funds_in_range PROCEDURE:::;
EXEC funds_in_range;
EXEC funds_in_range(100, 1000);


-- ON_BUY, upon a purchase, decrements the seller's shares by that amount and increases the seller's balance by the value of the sold shares.

CREATE OR REPLACE TRIGGER ON_BUY
AFTER 
INSERT on TRXLOG
FOR EACH ROW
WHEN (new.action = 'buy')
DECLARE 
    cur_balance number;
    price_shares number;
    check_shares number;
BEGIN   
    SELECT balance into cur_balance 
    from CUSTOMER
    WHERE (:new.login = login);    
    
    price_shares:= share_prices(:new.symbol, :new.num_shares, :new.t_date);
    IF (cur_balance >= :new.num_shares)
    THEN
    UPDATE CUSTOMER set balance = balance - price_shares WHERE login = :new.login;
       check_shares := has_shares(:new.login, :new.symbol);
       IF (check_shares =1)
       THEN
       UPDATE OWNS set shares = shares + :new.num_shares WHERE login = :new.login AND symbol = :new.symbol;    
       ELSE
       INSERT INTO OWNS values(:new.login, :new.symbol, :new.num_shares);
       END IF;
    END IF;
END;
/


*/