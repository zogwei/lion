CREATE TABLE `lion_tcc_business_activity` (
  `tx_id` VARCHAR(128) NOT NULL,
  `state` VARCHAR(20) NOT NULL,
  `gmt_create` DATETIME NOT NULL,
  `gmt_modified` DATETIME NOT NULL,
  `propagation` VARCHAR(20) NULL,
  PRIMARY KEY (`tx_id`));

CREATE TABLE `lionweb`.`lion_tcc_business_action` (
  `action_id` VARCHAR(128) NOT NULL,
  `tx_id` VARCHAR(128) NOT NULL,
  `name` VARCHAR(100) NOT NULL,
  `state` VARCHAR(20) NOT NULL,
  `gmt_create` DATETIME NOT NULL,
  `gmt_modifiled` DATETIME NOT NULL,
  `context` VARCHAR(4000) NULL,
  PRIMARY KEY (`action_id`));
