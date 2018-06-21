CREATE DATABASE IF NOT EXISTS opn default charset utf8 COLLATE utf8_general_ci;

CREATE TABLE `opn`.`user_login` (
  `id`  INT NOT NULL AUTO_INCREMENT,
	`fpid` varchar(32) NOT NULL,
	`ip` VARCHAR(20) NULL,
	`city` VARCHAR(20) NULL,
	`create_time` TIMESTAMP NOT NULL default CURRENT_TIMESTAMP,
	 PRIMARY KEY (`id`),
    KEY `fpid` (`fpid`)
 )
 ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `opn`.`post` (
  `id`  INT NOT NULL AUTO_INCREMENT,
	`topic_id` INT NOT NULL,
	`source_id` INT NOT NULL,
	`media_type` INT NOT NULL,
	`title` VARCHAR(100) NULL,
	`date` VARCHAR(20) NULL,
	`author` VARCHAR(20) NULL,
	`content` VARCHAR(5000) NULL,
	`orgin_url` VARCHAR(200) NOT NULL,
	`tts_urls` VARCHAR(2000)  NULL,
	`create_time` TIMESTAMP NOT NULL default CURRENT_TIMESTAMP,
	 PRIMARY KEY (`id`),
    KEY `topicId` (`topic_id`),
    KEY `date` (`date`)
 )
 ENGINE=InnoDB DEFAULT CHARSET=utf8;


CREATE TABLE `opn`.`dispatch_log` (
  `id`  INT NOT NULL AUTO_INCREMENT,
	`fpid` varchar(32) NOT NULL,
	`post_id`  INT NOT NULL,
	`create_time` TIMESTAMP NOT NULL default CURRENT_TIMESTAMP,
	 PRIMARY KEY (`id`),
    KEY `fpid` (`fpid`)
 )
 ENGINE=InnoDB DEFAULT CHARSET=utf8;


CREATE TABLE `opn`.`user_topic` (
  `id`  INT NOT NULL AUTO_INCREMENT,
	`fpid` varchar(32) NOT NULL,
	`topic_id`  INT NOT NULL,
	`create_time` TIMESTAMP NOT NULL default CURRENT_TIMESTAMP,
	 PRIMARY KEY (`id`),
    KEY `fpid` (`fpid`)
 )
 ENGINE=InnoDB DEFAULT CHARSET=utf8;