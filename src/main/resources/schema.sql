DROP TABLE IF EXISTS order_product;
DROP TABLE IF EXISTS cart_product ;
DROP TABLE IF EXISTS cart;
DROP TABLE IF EXISTS product_order;
DROP TABLE IF EXISTS category;
DROP TABLE IF EXISTS digital_product;
DROP TABLE IF EXISTS physical_product;

CREATE TABLE digital_product ("id" BIGINT PRIMARY KEY, "label" varchar NOT NULL, "type" varchar NOT NULL, "url" varchar NOT NULL);

CREATE TABLE physical_product ("id" bigint PRIMARY KEY, "label" varchar NOT NULL, "type" varchar NOT NULL, "weight" integer NOT NULL);

CREATE TABLE product_order ("id" bigint PRIMARY KEY);

CREATE TABLE cart ("id" bigint PRIMARY KEY, "status" varchar NOT NULL);

CREATE TABLE cart_product ("id_product" bigint, "id_cart" bigint, PRIMARY KEY ("id_product", "id_cart"));

CREATE TABLE order_product ("id_product" bigint, "id_cart" bigint, PRIMARY KEY ("id_product", "id_cart"));

CREATE TABLE category ("id" bigint PRIMARY KEY, "label" varchar NOT NULL, "parent_id" bigint);

ALTER TABLE category ADD CONSTRAINT category_parent_id_fk FOREIGN KEY ("parent_id") REFERENCES "category" ("id");

ALTER TABLE cart_product ADD CONSTRAINT cart_product_id_cart_fk FOREIGN KEY ("id_cart") REFERENCES "cart" ("id");

ALTER TABLE order_product ADD CONSTRAINT order_product_id_cart_fk FOREIGN KEY ("id_cart") REFERENCES "product_order" ("id");
