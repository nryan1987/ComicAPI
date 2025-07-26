--liquibase formatted sql
--changeset ryan:2_Increase_Title_Length
ALTER TABLE Comics.Comics MODIFY COLUMN Title varchar(65) CHARACTER SET latin1 COLLATE latin1_swedish_ci NOT NULL;


--rollback ALTER TABLE Comics.Comics MODIFY COLUMN Title varchar(60) CHARACTER SET latin1 COLLATE latin1_swedish_ci NOT NULL;
