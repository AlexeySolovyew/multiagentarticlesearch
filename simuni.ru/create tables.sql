--
-- Database: `simuni`
--

-- --------------------------------------------------------

--
-- Table structure for table `Test`
--

CREATE TABLE IF NOT EXISTS `Test` (
  `TestID` int(11) NOT NULL AUTO_INCREMENT,
  `Expression` varchar(100) DEFAULT NULL,
  `Result` varchar(255) DEFAULT NULL,
  `TaskNum` int(11) DEFAULT NULL,
  PRIMARY KEY (`TestID`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 AUTO_INCREMENT=1 ;

--
-- Table structure for table `Task`
--

CREATE TABLE IF NOT EXISTS `Task` (
  `TaskID` int(11) NOT NULL AUTO_INCREMENT,
  `TaskLessonNumber` int(11) NOT NULL,
  PRIMARY KEY (`TaskID`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 AUTO_INCREMENT=1 ;

---Hometask---

CREATE TABLE IF NOT EXISTS `Hometask` (
  `HometaskID` int(11) NOT NULL AUTO_INCREMENT,
  `Topic` varchar(30) NOT NULL,
   `Date` DATE NOT NULL,
  PRIMARY KEY (`HometaskID`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 AUTO_INCREMENT=1 ;

---User---

CREATE TABLE  `simuni`.`User` (
`UserID` INT NOT NULL ,
`Name` VARCHAR( 30 ) NOT NULL ,
`Surname` VARCHAR( 30 ) NOT NULL ,
`RoleID` INT NOT NULL ,
`GroupNumber` INT NULL
) ENGINE = MYISAM ;

CREATE TABLE  `simuni`.`Solution` (
`SolutionID` INT NOT NULL ,
`TaskID` INT NOT NULL ,
`UserID` INT NOT NULL ,
`LoadTimestamp` INT NOT NULL ,
`Result` INT NOT NULL
) ENGINE = MYISAM ;

CREATE TABLE  `simuni`.`Role` (
`RoleID` INT NOT NULL AUTO_INCREMENT PRIMARY KEY ,
`RoleName` VARCHAR( 30 ) NOT NULL
) ENGINE = MYISAM ;

CREATE TABLE  `simuni`.`GeneralInfo` (
`GeneralInfoID` INT NOT NULL AUTO_INCREMENT PRIMARY KEY ,
`Name` VARCHAR( 30 ) NOT NULL ,
`Value` VARCHAR( 30 ) NOT NULL
) ENGINE = MYISAM ;

CREATE TABLE  `simuni`.`Result` (
`ResultID` INT NOT NULL AUTO_INCREMENT PRIMARY KEY ,
`Text` VARCHAR( 30 ) NOT NULL
) ENGINE = MYISAM ;


