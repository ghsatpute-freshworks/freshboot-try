DELIMITER $$

DROP PROCEDURE IF EXISTS upgrade_database_20200421175630 $$

CREATE PROCEDURE upgrade_database_20200421175630()
BEGIN

    IF NOT EXISTS((SELECT *
                   FROM information_schema.COLUMNS
                   WHERE TABLE_SCHEMA = DATABASE()
                     AND COLUMN_NAME = 'account_id'
                     AND TABLE_NAME = 'todos')) THEN
        alter table todos
            add account_id varchar(191) default '1' not null;

    END IF;

END $$

CALL upgrade_database_20200421175630() $$

DELIMITER ;