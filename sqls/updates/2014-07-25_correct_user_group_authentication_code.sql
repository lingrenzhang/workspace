BEGIN;

ALTER TABLE UserGroup CHANGE AuthnicationCode AuthenticationCode varchar(20);

COMMIT;
