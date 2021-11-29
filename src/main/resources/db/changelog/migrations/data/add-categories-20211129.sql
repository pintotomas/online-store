--liquibase formatted sql

--changeset tomas:2021112901
INSERT INTO category(id, label, parent_id) VALUES (1, 'Category 1', NULL);
INSERT INTO category(id, label, parent_id) VALUES (2, 'Category 2', NULL);

--changeset tomas:2021112902
INSERT INTO category(id, label, parent_id) VALUES (3, 'Category 3', 1);
INSERT INTO category(id, label, parent_id) VALUES (4, 'Category 4', 3);

--changeset tomas:2021112903
UPDATE digital_product SET id_category = 1 WHERE id = 1;
UPDATE digital_product SET id_category = 2 WHERE id = 2;
UPDATE digital_product SET id_category = 3 WHERE id = 3;
UPDATE digital_product SET id_category = 4 WHERE id = 4;
UPDATE digital_product SET id_category = 1 WHERE id = 5;
UPDATE digital_product SET id_category = 2 WHERE id = 6;
UPDATE digital_product SET id_category = 3 WHERE id = 7;
UPDATE digital_product SET id_category = 4 WHERE id = 8;
UPDATE digital_product SET id_category = 1 WHERE id = 9;
UPDATE digital_product SET id_category = 2 WHERE id = 10;

--changeset tomas:2021112904
UPDATE physical_product SET id_category = 1 WHERE id = 11;
UPDATE physical_product SET id_category = 2 WHERE id = 12;
UPDATE physical_product SET id_category = 3 WHERE id = 13;
UPDATE physical_product SET id_category = 4 WHERE id = 14;
UPDATE physical_product SET id_category = 1 WHERE id = 15;
UPDATE physical_product SET id_category = 2 WHERE id = 16;
UPDATE physical_product SET id_category = 3 WHERE id = 17;
UPDATE physical_product SET id_category = 4 WHERE id = 18;
UPDATE physical_product SET id_category = 1 WHERE id = 19;
UPDATE physical_product SET id_category = 2 WHERE id = 20;
