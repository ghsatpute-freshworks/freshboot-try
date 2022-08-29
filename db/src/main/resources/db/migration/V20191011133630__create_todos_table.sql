DELIMITER $$

DROP PROCEDURE IF EXISTS upgrade_database_20191011133630 $$

CREATE PROCEDURE upgrade_database_20191011133630()
BEGIN

    IF NOT EXISTS((SELECT *
                   FROM information_schema.COLUMNS
                   WHERE TABLE_SCHEMA = DATABASE()
                     AND TABLE_NAME = 'todos')) THEN
        create table todos
        (
            id        bigint       not null auto_increment,
            title     varchar(191) not null,
            completed tinyint,
            primary key (id)
        )
            ENGINE = InnoDB
            default CHARSET = utf8mb4;

    END IF;

END $$

CALL upgrade_database_20191011133630() $$

DELIMITER ;