CREATE DATABASE order_help;
CREATE  USER admin PASSWORD 'admin_pwd';
GRANT ALL PRIVILEGES ON DATABASE order_help TO admin;

\c order;

CREATE USER order_user PASSWORD 'order_user_pwd';
GRANT CONNECT ON DATABASE order_help TO order_user;

CREATE SCHEMA product;
ALTER SCHEMA product OWNER CONNECT TO admin;
GRANT ALL PRIVILEGES ON SCHEMA product TO order_user;
ALTER DEFAULT PRIVILEGES
      FOR USER admin
      IN SCHEMA product
      GRANT ALL PRIVILEGES ON TABLES TO order_user;
ALTER DEFAULT PRIVILEGES
      FOR USER admin
      IN SCHEMA product
      GRANT ALL PRIVILEGES ON SEQUENCES TO order_user;