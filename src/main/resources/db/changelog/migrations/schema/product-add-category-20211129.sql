--liquibase formatted sql

--changeset tomas:2021112901
ALTER TABLE digital_product
ADD COLUMN id_category BIGINT;

--changeset tomas:2021112902
ALTER TABLE physical_product
ADD COLUMN id_category BIGINT;

--changeset tomas:2021112903
ALTER TABLE digital_product ADD CONSTRAINT digital_product_id_category_fk FOREIGN KEY ("id_category") REFERENCES "category" ("id");

--changeset tomas:2021112904
ALTER TABLE physical_product ADD CONSTRAINT physical_product_id_category_fk FOREIGN KEY ("id_category") REFERENCES "category" ("id");

