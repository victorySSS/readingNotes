-- MySQL Script generated by MySQL Workbench
-- Fri Dec 15 23:57:47 2017
-- Model: New Model    Version: 1.0
-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='TRADITIONAL,ALLOW_INVALID_DATES';

-- -----------------------------------------------------
-- Schema Notes
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `Notes` DEFAULT CHARACTER SET utf8 ;
USE `Notes` ;

-- -----------------------------------------------------
-- Table `Notes`.`NoteList`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `Notes`.`NoteList` (
  `userID` INT UNSIGNED NOT NULL,
  `noteID` INT UNSIGNED NOT NULL,
  PRIMARY KEY (`noteID`));


-- -----------------------------------------------------
-- Table `Notes`.`Note`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `Notes`.`Note` ( 
  `userID` INT UNSIGNED NOT NULL, 
  `noteID` INT UNSIGNED NOT NULL AUTO_INCREMENT, 
  `noteAddress` VARCHAR(255) NOT NULL, 
  `textAddress` VARCHAR(255) NULL,
  `bookName` VARCHAR(100) NULL, 
  `createTime` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP, 
  `totalLikes` INT UNSIGNED NOT NULL DEFAULT 0, 
  `totalComments` INT UNSIGNED NOT NULL DEFAULT 0, 
  `User_userID` INT UNSIGNED NOT NULL, 
  PRIMARY KEY (`noteID`), 
  INDEX `fk_Note_User1_idx` (`User_userID` ASC), 
  CONSTRAINT `fk_Note_User1` 
    FOREIGN KEY (`User_userID`) 
    REFERENCES `Notes`.`User` (`userID`) 
    ON DELETE NO ACTION 
    ON UPDATE NO ACTION); 

-- -----------------------------------------------------
-- Table `Notes`.`User`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `Notes`.`User` (
  `userID` INT UNSIGNED NOT NULL AUTO_INCREMENT,
  `userName` VARCHAR(16) NOT NULL UNIQUE,
  `userPassword` VARCHAR(32) NOT NULL,
  `userNickname` VARCHAR(16) NULL,
  `userPortrait` VARCHAR(255) NULL,
  `create_time` TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`userID`));

-- -----------------------------------------------------
-- Table `Notes`.`Comment`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `Notes`.`Comment` (
  `noteID` INT UNSIGNED NOT NULL,
  `commentID` INT UNSIGNED NOT NULL,
  `comFromID` INT UNSIGNED NOT NULL,
  `comToID` INT UNSIGNED NOT NULL,
  `commentText` VARCHAR(255) NOT NULL,
  `Note_noteID` INT UNSIGNED NOT NULL,
  PRIMARY KEY (`noteID`),
  INDEX `fk_Comment_Note1_idx` (`Note_noteID` ASC),
  CONSTRAINT `fk_Comment_Note1`
    FOREIGN KEY (`Note_noteID`)
    REFERENCES `Notes`.`Note` (`noteID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION);


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
