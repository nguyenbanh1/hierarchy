CREATE TABLE IF NOT EXISTS `employee` (
    `id`                INTEGER NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `name`              VARCHAR(250) NOT NULL UNIQUE,
    `supervisor`        INTEGER,
    `version`           INTEGER NOT NULL,
    `created_date`      TIMESTAMP(3) DEFAULT CURRENT_TIMESTAMP(3),
    `modified_date`     TIMESTAMP(3) DEFAULT CURRENT_TIMESTAMP(3),
    FOREIGN KEY(supervisor) REFERENCES employee(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE = utf8mb4_unicode_ci;
