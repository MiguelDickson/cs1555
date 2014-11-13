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

-- new_balance returns the sum of a balance 'x' and an incremental number 'incr_val'.
CREATE OR REPLACE FUNCTION new_balance(x in number, incr_val in number)
	RETURN number IS final_val number;
BEGIN
	final_val := x + incr_val;
	RETURN(final_val);
END;
/

-- sale_proceeds returns the proceeds of a number of shares 'shares' for a fund 'symbol' on a date 'sell_date'.
CREATE OR REPLACE FUNCTION sale_proceeds(sym in varchar, shares in number, sell_date date)
    RETURN number IS proceeds number;
sale_price number;
BEGIN
    SELECT price INTO sale_price
    FROM CLOSINGPRICE
    WHERE p_date = sell_date AND symbol = sym;
    
    proceeds := sale_price * shares;
    --dbms_output.put_line(proceeds);
    RETURN(proceeds);
END;
/

-- ON_SALE, upon a sale, decrements the seller's shares by that amount and increases the seller's balance by the value of the sold shares.
CREATE OR REPLACE TRIGGER ON_SALE
AFTER 
INSERT on TRXLOG
FOR EACH ROW
WHEN (new.action = 'sell') 
DECLARE 
    shares_held number;
BEGIN   
    SELECT shares into shares_held 
    from OWNS
    WHERE (:new.login = login);    
    
    IF (shares_held >= :new.num_shares)
    THEN
    UPDATE CUSTOMER set balance = balance + sale_proceeds(:new.symbol, :new.num_shares, :new.t_date) WHERE login = :new.login;
    UPDATE OWNS set shares = shares_held - :new.num_shares WHERE login = :new.login;    
    END IF;
END;
/
   
-- share_prices returns the cost of a number of shares 'shares' for a fund 'symbol' on a date 'sell_date'.
CREATE OR REPLACE FUNCTION share_prices(sym in varchar, shares in number, buy_date date)
    RETURN number IS cost number;
buy_price number;
BEGIN
    SELECT price INTO buy_price
    FROM CLOSINGPRICE
    WHERE p_date = buy_date AND symbol = sym;
    
    cost := buy_price * shares;
    --dbms_output.put_line(cost);
    RETURN(cost);
END;
/

-- ON_BUY, upon a purchase, decrements the seller's shares by that amount and increases the seller's balance by the value of the sold shares.
CREATE OR REPLACE TRIGGER ON_BUY
AFTER 
INSERT on TRXLOG
FOR EACH ROW
WHEN (new.action = 'buy')
DECLARE 
    cur_balance number;
    price_shares number;
BEGIN   
    SELECT balance into cur_balance 
    from CUSTOMER
    WHERE (:new.login = login);    
    
    price_shares:= share_prices(:new.symbol, :new.num_shares, :new.t_date);
    IF (cur_balance >= :new.num_shares)
    THEN
    UPDATE CUSTOMER set balance = balance - price_shares WHERE login = :new.login;
    UPDATE OWNS set shares = shares + :new.num_shares WHERE login = :new.login AND symbol = :new.symbol;    
    END IF;
END;
/

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
      dbms_output.put_line(npref);
      RETURN(npref);
END;
/

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
      dbms_output.put_line(symb);
      RETURN(symb);
END;
/

CREATE OR REPLACE TRIGGER ON_DEPOSIT 
AFTER 
INSERT on TRXLOG
FOR EACH ROW
WHEN (new.action = 'deposit')
DECLARE 
    recent_alloc number;
    money_alloc number;
    shares_bought number;
    numb_prefs number;
    cur_balance number;
    price_shares number;
    shares_not_purchased number;
BEGIN   
    --SELECT :new.amount into cur_balance 
    --from CUSTOMER
    --WHERE (:new.login = login); 
    cur_balance:= :new.amount;

    shares_not_purchased:= 0;
    recent_alloc:= get_last_allocation(:new.login);
    numb_prefs:= get_number_preferences(recent_alloc);
    
    for i in 1..numb_prefs LOOP
        money_alloc := get_n_preference(i, recent_alloc) * :new.amount;
        dbms_output.put_line('This amount of the current balance of the allocated amount is allocated:');
        dbms_output.put_line(money_alloc);
        shares_bought := FLOOR(money_alloc / get_last_closing_price(get_n_prefsymbol(i, recent_alloc)));
        IF (shares_bought = 0)
        THEN
        shares_not_purchased:=1;
        END IF;        
        dbms_output.put_line('This number of shares were purchased:');
        dbms_output.put_line(shares_bought);
        cur_balance:= cur_balance - (shares_bought * (get_last_closing_price(get_n_prefsymbol(i,recent_alloc))));
        dbms_output.put_line('Balance is now:');
        dbms_output.put_line(cur_balance);    
    END LOOP; 
    
        dbms_output.put_line('End balance is:');
        dbms_output.put_line(cur_balance);    
        
    IF (shares_not_purchased = 1)
        THEN
        dbms_output.put_line('Was unable to buy some shares and so placing the deposit into balance entirely.');
        UPDATE CUSTOMER set balance = balance + :new.amount;
    ELSE
        cur_balance:= :new.amount;
        
        for i in 1..numb_prefs LOOP
            money_alloc := get_n_preference(i, recent_alloc) * :new.amount;
            shares_bought := FLOOR(money_alloc / get_last_closing_price(get_n_prefsymbol(i, recent_alloc)));
            UPDATE OWNS set shares = shares + shares_bought WHERE login = :new.login AND symbol = get_n_prefsymbol(i,recent_alloc);  
            cur_balance:= cur_balance - (shares_bought * (get_last_closing_price(get_n_prefsymbol(i,recent_alloc))));
            
        end loop;
        
        UPDATE CUSTOMER set balance = balance + cur_balance;
        
     END IF;           
END;
/

--TESTING SALE TRANSACTION
PROMPT Testing SALE Transaction:;
PROMPT SALE: CUSTOMER for Mike;
select * from customer where login = 'mike';
PROMPT SALE: OWNS for Mike;
select * from owns where login = 'mike';

INSERT INTO TRXLOG values('0', 'mike', 'RE', '03-APR-14', 'sell', 10, 15, 150);

PROMPT SALE: TRXLOG for Mike;
select * from trxlog;
PROMPT SALE: CUSTOMER for Mike;
select * from customer where login = 'mike';
PROMPT SALE: OWNS for Mike;
select * from owns where login = 'mike';

--
--TESTING BUY TRANSACTION
PROMPT Testing BUY Transaction:;
INSERT INTO TRXLOG values('1', 'mike', 'RE', '03-APR-14', 'buy', 10, 15, 150);

PROMPT BUY: CUSTOMER for Mike;
select * from customer where login = 'mike';
PROMPT BUY: OWNS for Mike;
select * from owns where login = 'mike';

--
--TESTING DEPOSIT TRANSACTION
PROMPT Testing DEPOSIT Transaction:;
PROMPT DEPOSIT: CUSTOMER for Mike;
select * from customer where login = 'mike';
PROMPT DEPOSIT: OWNS for Mike;
select * from owns where login = 'mike';

INSERT INTO TRXLOG values('2', 'mike', 'RE', '03-APR-14', 'deposit', NULL, NULL, 1000000);

PROMPT DEPOSIT: CUSTOMER for Mike;
select * from customer where login = 'mike';
PROMPT DEPOSIT: OWNS for Mike;
select * from owns where login = 'mike';
--
--