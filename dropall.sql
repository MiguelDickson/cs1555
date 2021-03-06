DROP TABLE MUTUALFUND CASCADE CONSTRAINTS;
DROP TABLE CLOSINGPRICE CASCADE CONSTRAINTS;
DROP TABLE CUSTOMER CASCADE CONSTRAINTS;
DROP TABLE ADMINISTRATOR CASCADE CONSTRAINTS;
DROP TABLE ALLOCATION CASCADE CONSTRAINTS;
DROP TABLE PREFERS CASCADE CONSTRAINTS;
DROP TABLE TRXLOG CASCADE CONSTRAINTS;
DROP TABLE OWNS CASCADE CONSTRAINTS;
DROP TABLE MUTUALDATE CASCADE CONSTRAINTS;
DROP FUNCTION new_balance;
DROP FUNCTION sale_proceeds;
DROP FUNCTION share_prices;
DROP FUNCTION get_last_allocation;
DROP FUNCTION get_number_preferences;
DROP FUNCTION get_last_closing_price;
DROP FUNCTION get_n_preference;
DROP TRIGGER ON_SALE;
DROP TRIGGER ON_BUY;
DROP TRIGGER ON_DEPOSIT;
DROP FUNCTION get_n_prefsymbol;
DROP FUNCTION has_shares;
DROP VIEW LATESTPRICE;
DROP PROCEDURE FUNDS_IN_RANGE;
commit;
purge recyclebin;
