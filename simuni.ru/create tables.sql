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