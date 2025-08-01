--liquibase formatted sql
--changeset ryan:1_Initial_Create

-- Comics.`Characters` definition
CREATE TABLE IF NOT EXISTS `Characters` (
  `CharacterID` int(11) NOT NULL,
  `Characters` varchar(150) NOT NULL,
  `CharacterPic` varchar(80) DEFAULT NULL,
  `RecordCreationDate` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `LastUpdated` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`CharacterID`),
  UNIQUE KEY `Characters` (`Characters`),
  KEY `CharacterPic` (`CharacterPic`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;


-- Comics.`Condition` definition
CREATE TABLE IF NOT EXISTS `Condition` (
  `ConditionID` int(11) NOT NULL,
  `Condition` varchar(10) NOT NULL,
  PRIMARY KEY (`ConditionID`),
  KEY `Condition` (`Condition`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

INSERT INTO `Condition`
(ConditionID, `Condition`)
VALUES (1,'MT 10.0'),
	 (2,'MT 9.9'),
	 (3,'NM/M 9.8'),
	 (4,'NM+ 9.6'),
	 (5,'NM 9.4'),
	 (6,'NM- 9.2'),
	 (7,'VF/NM 9.0'),
	 (8,'VF+ 8.5'),
	 (9,'VF 8.0'),
	 (10,'VF- 7.5'),
	 (11,'FN/VF 7.0'),
	 (12,'FN+ 6.5'),
	 (13,'FN 6.0'),
	 (14,'FN- 5.5'),
	 (15,'VG/FN 5.0'),
	 (16,'VG+ 4.5'),
	 (17,'VG 4.0'),
	 (18,'VG- 3.5'),
	 (19,'GD/VG 3.0'),
	 (20,'GD+ 2.5'),
	 (21,'GD 2.0'),
	 (22,'GD- 1.8'),
	 (23,'F/GD 1.5'),
	 (24,'F 1.0'),
	 (25,'PR 0.5') ON DUPLICATE KEY UPDATE `Condition`=VALUES(`Condition`);

-- Comics.Creators definition
CREATE TABLE IF NOT EXISTS `Creators` (
  `CreatorID` int(11) NOT NULL,
  `Creator` varchar(65) NOT NULL,
  `creatorPic` varchar(80) DEFAULT NULL,
  `RecordCreationDate` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `LastUpdated` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`CreatorID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;


-- Comics.DeletedEvent definition
CREATE TABLE IF NOT EXISTS `DeletedEvent` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `TableName` varchar(255) NOT NULL,
  `RecordID1` int(11) DEFAULT NULL,
  `RecordID2` int(11) DEFAULT NULL,
  `String1` varchar(255) DEFAULT NULL,
  `String2` varchar(255) DEFAULT NULL,
  `Processed` bit(1) NOT NULL,
  `RecordCreationDate` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=149 DEFAULT CHARSET=latin1;

-- Comics.Publisher definition
CREATE TABLE IF NOT EXISTS `Publisher` (
  `PublisherID` int(10) unsigned NOT NULL,
  `Publisher` varchar(30) NOT NULL,
  `RecordCreationDate` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`PublisherID`),
  KEY `Publisher` (`Publisher`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- Comics.CharacterAliases definition
CREATE TABLE IF NOT EXISTS `CharacterAliases` (
  `CharacterID` int(11) NOT NULL,
  `Alias` varchar(60) NOT NULL,
  `RecordCreationDate` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`CharacterID`,`Alias`),
  UNIQUE KEY `Alias` (`Alias`),
  CONSTRAINT `CharacterAliases_ibfk_1` FOREIGN KEY (`CharacterID`) REFERENCES `Characters` (`CharacterID`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- Comics.ComicCharacters definition
CREATE TABLE IF NOT EXISTS `ComicCharacters` (
  `ComicID` bigint(20) NOT NULL,
  `CharacterID` int(11) NOT NULL,
  `AppearsAs` varchar(65) NOT NULL,
  `RecordCreationDate` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`ComicID`,`CharacterID`,`AppearsAs`),
  KEY `ComicID` (`ComicID`),
  KEY `CharacterID` (`CharacterID`),
  KEY `ComicID_2` (`ComicID`),
  CONSTRAINT `ComicCharacters_ibfk_1` FOREIGN KEY (`CharacterID`) REFERENCES `Characters` (`CharacterID`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- Comics.Comics definition
CREATE TABLE IF NOT EXISTS `Comics` (
  `ComicID` bigint(20) unsigned NOT NULL,
  `Title` varchar(60) NOT NULL,
  `Volume` int(10) unsigned DEFAULT '1',
  `Issue` double(10,1) DEFAULT '0.0',
  `publicationDate` date DEFAULT NULL,
  `StoryTitle` mediumtext,
  `Publisher` varchar(30) DEFAULT NULL,
  `PricePaid` float(10,2) DEFAULT '0.00',
  `Value` float(10,2) DEFAULT '0.00',
  `Condition` varchar(10) DEFAULT NULL,
  `Picture` varchar(55) DEFAULT NULL,
  `RecordCreationDate` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `LastUpdated` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`ComicID`),
  KEY `Title` (`Title`,`Volume`,`Issue`),
  KEY `Publisher` (`Publisher`),
  KEY `Condition` (`Condition`),
  CONSTRAINT `Comics_ibfk_1` FOREIGN KEY (`Publisher`) REFERENCES `Publisher` (`Publisher`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `Comics_ibfk_2` FOREIGN KEY (`Condition`) REFERENCES `Condition` (`Condition`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- Comics.CreatorAlias definition
CREATE TABLE IF NOT EXISTS `CreatorAlias` (
  `CreatorID` int(11) NOT NULL,
  `Alias` varchar(65) NOT NULL,
  `RecordCreationDate` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`CreatorID`,`Alias`),
  UNIQUE KEY `Alias` (`Alias`),
  CONSTRAINT `CreatorAlias_ibfk_1` FOREIGN KEY (`CreatorID`) REFERENCES `Creators` (`CreatorID`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- Comics.Notes definition
CREATE TABLE IF NOT EXISTS `Notes` (
  `NoteID` int(11) NOT NULL AUTO_INCREMENT,
  `ComicID` bigint(20) unsigned NOT NULL,
  `Notes` varchar(60) NOT NULL,
  `RecordCreationDate` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `LastUpdated` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`NoteID`),
  KEY `ComicID` (`ComicID`),
  CONSTRAINT `comicID_fk` FOREIGN KEY (`ComicID`) REFERENCES `Comics` (`ComicID`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=14994 DEFAULT CHARSET=latin1;

-- Comics.ComicAlias definition
CREATE TABLE IF NOT EXISTS `ComicAlias` (
  `ComicID` bigint(20) unsigned NOT NULL,
  `Title` varchar(60) NOT NULL,
  `Issue` int(10) NOT NULL,
  `Volume` int(10) NOT NULL,
  `RecordCreationDate` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `LastUpdated` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`ComicID`),
  KEY `ComicID` (`ComicID`),
  CONSTRAINT `ComicID` FOREIGN KEY (`ComicID`) REFERENCES `Comics` (`ComicID`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- Comics.ComicArtists definition
CREATE TABLE IF NOT EXISTS `ComicArtists` (
  `ComicID` bigint(20) unsigned NOT NULL,
  `ArtistID` int(11) NOT NULL,
  `RecordCreationDate` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`ComicID`,`ArtistID`),
  KEY `ArtistID` (`ArtistID`),
  CONSTRAINT `ComicArtists_ibfk_1` FOREIGN KEY (`ComicID`) REFERENCES `Comics` (`ComicID`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `ComicArtists_ibfk_2` FOREIGN KEY (`ArtistID`) REFERENCES `Creators` (`CreatorID`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;


-- Comics.ComicWriters definition
CREATE TABLE IF NOT EXISTS `ComicWriters` (
  `ComicID` bigint(20) unsigned NOT NULL,
  `WriterID` int(11) NOT NULL,
  `RecordCreationDate` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`ComicID`,`WriterID`),
  KEY `WriterID` (`WriterID`),
  CONSTRAINT `ComicWriters_ibfk_2` FOREIGN KEY (`ComicID`) REFERENCES `Comics` (`ComicID`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `ComicWriters_ibfk_3` FOREIGN KEY (`WriterID`) REFERENCES `Creators` (`CreatorID`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- Comics.`User` definition
CREATE TABLE IF NOT EXISTS `User` (
  `UserID` varchar(255) NOT NULL,
  `UserName` varchar(255) NOT NULL,
  `Password` varchar(255) NOT NULL,
  `EmailAddress` varchar(255) NOT NULL,
  `RecordCreationDate` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `LastUpdatedDate` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`UserID`),
  UNIQUE KEY `UserName` (`UserName`),
  UNIQUE KEY `EmailAddress` (`EmailAddress`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- Comics.UserLog definition
CREATE TABLE IF NOT EXISTS `UserLog` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `UserName` varchar(20) NOT NULL,
  `Event` longtext NOT NULL,
  `TimeStamp` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=23203 DEFAULT CHARSET=latin1;

-- Comics.UserSettings definition
CREATE TABLE IF NOT EXISTS `UserSettings` (
  `UserID` varchar(255) NOT NULL,
  `numRecentIssues` int(11) NOT NULL,
  `RecordCreationDate` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `LastUpdatedDate` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  KEY `UserSettings_FK` (`UserID`),
  CONSTRAINT `UserSettings_FK` FOREIGN KEY (`UserID`) REFERENCES `User` (`UserID`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--rollback DROP TABLE `Characters`, `Condition`, `Creators`, `DeletedEvent`, `Publisher`, `CharacterAliases`, `ComicCharacters`, `Comics`, `CreatorAlias`, `Notes`, `ComicAlias`, `ComicArtists`, `ComicWriters`, `User`, `UserLog`, `UserSettings`