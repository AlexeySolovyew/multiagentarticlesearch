ALTER TABLE  `Test` ADD  `Smart` INT NOT NULL DEFAULT  '0'

ALTER TABLE  `Test` ADD  `TestForTaskID` INT NOT NULL

ALTER TABLE  `Task` ADD  `HometaskID` INT NOT NULL

ALTER TABLE  `Task` ADD  `Condition` MEDIUMTEXT NOT NULL AFTER  `HometaskID`

ALTER TABLE  `simuni`.`Task` ADD UNIQUE (
`TaskForHometask` ,
`HometaskID`
)

ALTER TABLE  `simuni`.`Test` ADD UNIQUE (
`TestForTaskID` ,
`TaskID`
)

ALTER TABLE  `Solution` CHANGE  `Result`  `ResultID` INT( 11 ) NOT NULL

ALTER TABLE  `Solution` ADD  `Code` MEDIUMTEXT NOT NULL AFTER  `ResultID`

ALTER TABLE  `Solution` ADD  `TestResult` VARCHAR( 40 ) NOT NULL AFTER  `Code`

ALTER TABLE  `User` CHANGE  `UserID`  `UserID` VARCHAR( 32 ) NOT NULL

ALTER TABLE  `User` ADD  `Password` VARCHAR( 32 ) NOT NULL AFTER  `UserID`

ALTER TABLE  `User` CHANGE  `RoleID`  `RoleID` INT( 11 ) NOT NULL DEFAULT  '1'

ALTER TABLE  `Hometask` ADD  `Deadline` DATE NOT NULL

mysql_real_escape_string($user)

ALTER TABLE  `Solution` CHANGE  `UserID`  `UserID` VARCHAR( 30 ) NOT NULL

ALTER TABLE  `Task` ADD  `Reward` INT NOT NULL AFTER  `Condition`

//дедлайны
//сортировка
//отделить решения от самих задач

ALTER TABLE  `Task` ADD  `Price` INT NOT NULL

ALTER TABLE  `Hometask` CHANGE  `Topic`  `Topic` VARCHAR( 200 ) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL