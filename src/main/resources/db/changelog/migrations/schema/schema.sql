--liquibase formatted sql

--changeset tomas:2021112801
DROP TABLE IF EXISTS order_product;
DROP TABLE IF EXISTS cart_product ;
DROP TABLE IF EXISTS cart;
DROP TABLE IF EXISTS product_order;
DROP TABLE IF EXISTS category;
DROP TABLE IF EXISTS digital_product;
DROP TABLE IF EXISTS physical_product;

--changeset tomas:2021112802
CREATE TABLE digital_product ("id" BIGINT PRIMARY KEY, "label" varchar NOT NULL, "type" varchar NOT NULL, "url" varchar NOT NULL);

--changeset tomas:2021112803
CREATE TABLE physical_product ("id" bigint PRIMARY KEY, "label" varchar NOT NULL, "type" varchar NOT NULL, "weight" integer NOT NULL);

--changeset tomas:2021112804
CREATE TABLE product_order ("id" bigint PRIMARY KEY);

--changeset tomas:2021112805
CREATE TABLE cart ("id" bigint PRIMARY KEY, "status" varchar NOT NULL);

--changeset tomas:2021112806
CREATE TABLE cart_product ("id_product" bigint, "id_cart" bigint, PRIMARY KEY ("id_product", "id_cart"));

--changeset tomas:2021112807
CREATE TABLE order_product ("id_product" bigint, "id_cart" bigint, PRIMARY KEY ("id_product", "id_cart"));

--changeset tomas:2021112808
CREATE TABLE category ("id" bigint PRIMARY KEY, "label" varchar NOT NULL, "parent_id" bigint);

--changeset tomas:2021112809
ALTER TABLE category ADD CONSTRAINT category_parent_id_fk FOREIGN KEY ("parent_id") REFERENCES "category" ("id");

--changeset tomas:2021112810
ALTER TABLE cart_product ADD CONSTRAINT cart_product_id_cart_fk FOREIGN KEY ("id_cart") REFERENCES "cart" ("id");

--changeset tomas:2021112811
ALTER TABLE order_product ADD CONSTRAINT order_product_id_cart_fk FOREIGN KEY ("id_cart") REFERENCES "product_order" ("id");