//Database related all required queries are given here
//Create SCHEMA
CREATE SCHEMA user_registration ;

//Create TABLE password_reset_token
CREATE TABLE `user_registration`.`password_reset_token` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `expiration_time` DATETIME(6) NULL,
  `token` VARCHAR(255) NULL,
  `user_id` BIGINT NULL,
  PRIMARY KEY (`id`));

//Create TABLE verification_token
CREATE TABLE `user_registration`.`verification_token` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `expiration_time` DATETIME(6) NULL,
  `token` VARCHAR(255) NULL,
  `user_id` BIGINT NULL,
  PRIMARY KEY (`id`));

//Create TABLE user
CREATE TABLE `user_registration`.`user` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `email` VARCHAR(255) NULL,
  `enabled` BIT(1) NULL,
  `first_name` VARCHAR(255) NULL,
  `last_name` VARCHAR(255) NULL,
  `password` VARCHAR(60) NULL,
  `role` VARCHAR(255) NULL,
  PRIMARY KEY (`id`));

//Insert Data into user table
INSERT INTO `user_registration`.`user` (`id`, `email`, `enabled`, `first_name`, `last_name`, `password`, `role`) VALUES ('1', 'admin', b'1', 'admin', 'google', '$2a$11$dZRMB5lcXcdSDuRYRZJmVu0ox29ZrnIS0eBBP/5CjNot4ZSLTsfMa', 'USER');
INSERT INTO `user_registration`.`user` (`id`, `email`, `enabled`, `first_name`, `last_name`, `password`, `role`) VALUES ('2', 'user', b'1', 'user', 'root', '$2a$11$NFvmSnxXs.yQVxEjtLyfwOpDxYv2LMWyF/dH/uuxVXuTE4rVZVSSu', 'USER');






