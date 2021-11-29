--liquibase formatted sql

--changeset tomas:2021112901
ALTER TABLE digital_product
ALTER COLUMN id_category SET NOT NULL;

--changeset tomas:2021112902
ALTER TABLE physical_product
ALTER COLUMN id_category SET NOT NULL;
