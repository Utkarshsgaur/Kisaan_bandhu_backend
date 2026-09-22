-- MySQL dump 10.13  Distrib 8.4.9, for Win64 (x86_64)
--
-- Host: localhost    Database: agri_procurement_system
-- ------------------------------------------------------
-- Server version	8.4.9

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `bookings`
--

DROP TABLE IF EXISTS `bookings`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `bookings` (
  `booking_id` bigint unsigned NOT NULL AUTO_INCREMENT,
  `farmer_id` bigint unsigned NOT NULL,
  `schedule_id` bigint unsigned NOT NULL,
  `quantity_quintal` decimal(10,2) NOT NULL,
  `booking_status` enum('PENDING','CONFIRMED','CANCELLED','NO_SHOW','COMPLETED') NOT NULL DEFAULT 'PENDING',
  `booked_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`booking_id`),
  KEY `idx_booking_farmer` (`farmer_id`),
  KEY `idx_booking_schedule` (`schedule_id`),
  KEY `idx_booking_status` (`booking_status`),
  CONSTRAINT `fk_booking_farmer` FOREIGN KEY (`farmer_id`) REFERENCES `farmers` (`farmer_id`) ON DELETE RESTRICT ON UPDATE CASCADE,
  CONSTRAINT `fk_booking_schedule` FOREIGN KEY (`schedule_id`) REFERENCES `procurement_schedules` (`schedule_id`) ON DELETE RESTRICT ON UPDATE CASCADE,
  CONSTRAINT `chk_booking_quantity` CHECK ((`quantity_quintal` > 0))
) ENGINE=InnoDB AUTO_INCREMENT=29 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = cp850 */ ;
/*!50003 SET character_set_results = cp850 */ ;
/*!50003 SET collation_connection  = cp850_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50017 DEFINER=`root`@`localhost`*/ /*!50003 TRIGGER `trg_prevent_booking_overcapacity` BEFORE INSERT ON `bookings` FOR EACH ROW BEGIN
    DECLARE current_booked DECIMAL(10,2);
    DECLARE schedule_capacity DECIMAL(10,2);

    IF NEW.booking_status IN ('PENDING', 'CONFIRMED') THEN

        SELECT
            COALESCE(SUM(quantity_quintal), 0),
            max_capacity_quintal
        INTO
            current_booked,
            schedule_capacity
        FROM bookings
        JOIN procurement_schedules
            ON bookings.schedule_id = procurement_schedules.schedule_id
        WHERE bookings.schedule_id = NEW.schedule_id
          AND bookings.booking_status IN ('PENDING', 'CONFIRMED')
        GROUP BY max_capacity_quintal;

        IF current_booked + NEW.quantity_quintal > schedule_capacity THEN
            SIGNAL SQLSTATE '45000'
            SET MESSAGE_TEXT = 'Booking rejected: schedule capacity exceeded';
        END IF;

    END IF;
END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = cp850 */ ;
/*!50003 SET character_set_results = cp850 */ ;
/*!50003 SET collation_connection  = cp850_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50017 DEFINER=`root`@`localhost`*/ /*!50003 TRIGGER `trg_update_booked_capacity_after_insert` AFTER INSERT ON `bookings` FOR EACH ROW BEGIN
    IF NEW.booking_status IN ('PENDING', 'CONFIRMED') THEN

        UPDATE procurement_schedules
        SET booked_capacity_quintal =
            booked_capacity_quintal + NEW.quantity_quintal
        WHERE schedule_id = NEW.schedule_id;

    END IF;
END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = cp850 */ ;
/*!50003 SET character_set_results = cp850 */ ;
/*!50003 SET collation_connection  = cp850_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50017 DEFINER=`root`@`localhost`*/ /*!50003 TRIGGER `trg_sync_capacity_after_booking_update` AFTER UPDATE ON `bookings` FOR EACH ROW BEGIN

    IF OLD.schedule_id <> NEW.schedule_id THEN

        UPDATE procurement_schedules
        SET booked_capacity_quintal = (
            SELECT COALESCE(SUM(quantity_quintal), 0)
            FROM bookings
            WHERE schedule_id = OLD.schedule_id
              AND booking_status IN ('PENDING', 'CONFIRMED')
        )
        WHERE schedule_id = OLD.schedule_id;

        UPDATE procurement_schedules
        SET booked_capacity_quintal = (
            SELECT COALESCE(SUM(quantity_quintal), 0)
            FROM bookings
            WHERE schedule_id = NEW.schedule_id
              AND booking_status IN ('PENDING', 'CONFIRMED')
        )
        WHERE schedule_id = NEW.schedule_id;

    ELSE

        UPDATE procurement_schedules
        SET booked_capacity_quintal = (
            SELECT COALESCE(SUM(quantity_quintal), 0)
            FROM bookings
            WHERE schedule_id = NEW.schedule_id
              AND booking_status IN ('PENDING', 'CONFIRMED')
        )
        WHERE schedule_id = NEW.schedule_id;

    END IF;

END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = cp850 */ ;
/*!50003 SET character_set_results = cp850 */ ;
/*!50003 SET collation_connection  = cp850_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50017 DEFINER=`root`@`localhost`*/ /*!50003 TRIGGER `trg_sync_capacity_after_booking_delete` AFTER DELETE ON `bookings` FOR EACH ROW BEGIN
    IF OLD.booking_status IN ('PENDING', 'CONFIRMED') THEN
        UPDATE procurement_schedules
        SET booked_capacity_quintal =
            booked_capacity_quintal - OLD.quantity_quintal
        WHERE schedule_id = OLD.schedule_id;
    END IF;
END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;

--
-- Table structure for table `crops`
--

DROP TABLE IF EXISTS `crops`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `crops` (
  `crop_id` bigint unsigned NOT NULL AUTO_INCREMENT,
  `crop_name` varchar(100) NOT NULL,
  `crop_code` varchar(20) NOT NULL,
  `is_active` tinyint(1) NOT NULL DEFAULT '1',
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`crop_id`),
  UNIQUE KEY `crop_name` (`crop_name`),
  UNIQUE KEY `crop_code` (`crop_code`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `farmers`
--

DROP TABLE IF EXISTS `farmers`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `farmers` (
  `farmer_id` bigint unsigned NOT NULL AUTO_INCREMENT,
  `user_id` bigint unsigned NOT NULL,
  `full_name` varchar(100) NOT NULL,
  `village` varchar(100) NOT NULL,
  `district` varchar(100) NOT NULL,
  `state` varchar(100) NOT NULL,
  `pincode` char(6) NOT NULL,
  `alternate_phone` varchar(15) DEFAULT NULL,
  `aadhaar_hash` char(64) DEFAULT NULL,
  `is_active` tinyint(1) NOT NULL DEFAULT '1',
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`farmer_id`),
  UNIQUE KEY `user_id` (`user_id`),
  UNIQUE KEY `aadhaar_hash` (`aadhaar_hash`),
  CONSTRAINT `fk_farmer_user` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`) ON DELETE RESTRICT ON UPDATE CASCADE,
  CONSTRAINT `chk_farmer_pincode` CHECK (regexp_like(`pincode`,_utf8mb4'^[0-9]{6}$'))
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `notifications`
--

DROP TABLE IF EXISTS `notifications`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `notifications` (
  `notification_id` bigint unsigned NOT NULL AUTO_INCREMENT,
  `farmer_id` bigint unsigned NOT NULL,
  `booking_id` bigint unsigned DEFAULT NULL,
  `notification_type` enum('BOOKING_CREATED','BOOKING_CONFIRMED','BOOKING_CANCELLED','SLOT_OFFERED','SCHEDULE_REMINDER','PROCUREMENT_STATUS') NOT NULL,
  `channel` enum('SMS','CALL','APP') NOT NULL,
  `message` text NOT NULL,
  `status` enum('PENDING','SENT','FAILED') NOT NULL DEFAULT 'PENDING',
  `sent_at` timestamp NULL DEFAULT NULL,
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`notification_id`),
  KEY `idx_notification_farmer` (`farmer_id`),
  KEY `idx_notification_booking` (`booking_id`),
  KEY `idx_notification_status` (`status`),
  CONSTRAINT `fk_notification_booking` FOREIGN KEY (`booking_id`) REFERENCES `bookings` (`booking_id`) ON DELETE RESTRICT ON UPDATE CASCADE,
  CONSTRAINT `fk_notification_farmer` FOREIGN KEY (`farmer_id`) REFERENCES `farmers` (`farmer_id`) ON DELETE RESTRICT ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `procurement_centres`
--

DROP TABLE IF EXISTS `procurement_centres`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `procurement_centres` (
  `centre_id` bigint unsigned NOT NULL AUTO_INCREMENT,
  `centre_code` varchar(20) NOT NULL,
  `centre_name` varchar(150) NOT NULL,
  `district` varchar(100) NOT NULL,
  `state` varchar(100) NOT NULL,
  `address` varchar(255) NOT NULL,
  `pincode` char(6) NOT NULL,
  `contact_phone` varchar(15) DEFAULT NULL,
  `is_active` tinyint(1) NOT NULL DEFAULT '1',
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`centre_id`),
  UNIQUE KEY `uk_centre_code` (`centre_code`),
  CONSTRAINT `chk_centre_pincode` CHECK (regexp_like(`pincode`,_cp850'^[0-9]{6}$'))
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `procurement_records`
--

DROP TABLE IF EXISTS `procurement_records`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `procurement_records` (
  `record_id` bigint unsigned NOT NULL AUTO_INCREMENT,
  `booking_id` bigint unsigned NOT NULL,
  `procured_quantity_quintal` decimal(10,2) DEFAULT NULL,
  `procurement_status` enum('PENDING','IN_PROGRESS','COMPLETED','REJECTED') NOT NULL DEFAULT 'PENDING',
  `rejection_reason` varchar(255) DEFAULT NULL,
  `procured_at` timestamp NULL DEFAULT NULL,
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`record_id`),
  UNIQUE KEY `uk_procurement_booking` (`booking_id`),
  KEY `idx_procurement_status` (`procurement_status`),
  CONSTRAINT `fk_procurement_booking` FOREIGN KEY (`booking_id`) REFERENCES `bookings` (`booking_id`) ON DELETE RESTRICT ON UPDATE CASCADE,
  CONSTRAINT `chk_procured_quantity` CHECK (((`procured_quantity_quintal` is null) or (`procured_quantity_quintal` > 0)))
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `procurement_schedules`
--

DROP TABLE IF EXISTS `procurement_schedules`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `procurement_schedules` (
  `schedule_id` bigint unsigned NOT NULL AUTO_INCREMENT,
  `centre_id` bigint unsigned NOT NULL,
  `season_id` bigint unsigned NOT NULL,
  `crop_id` bigint unsigned NOT NULL,
  `schedule_date` date NOT NULL,
  `start_time` time NOT NULL,
  `end_time` time NOT NULL,
  `max_capacity_quintal` decimal(10,2) NOT NULL,
  `booked_capacity_quintal` decimal(10,2) NOT NULL DEFAULT '0.00',
  `status` enum('OPEN','CLOSED','CANCELLED') NOT NULL DEFAULT 'OPEN',
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`schedule_id`),
  KEY `idx_schedule_centre` (`centre_id`),
  KEY `idx_schedule_season` (`season_id`),
  KEY `idx_schedule_crop` (`crop_id`),
  KEY `idx_schedule_date` (`schedule_date`),
  CONSTRAINT `fk_schedule_centre` FOREIGN KEY (`centre_id`) REFERENCES `procurement_centres` (`centre_id`) ON DELETE RESTRICT ON UPDATE CASCADE,
  CONSTRAINT `fk_schedule_crop` FOREIGN KEY (`crop_id`) REFERENCES `crops` (`crop_id`) ON DELETE RESTRICT ON UPDATE CASCADE,
  CONSTRAINT `fk_schedule_season` FOREIGN KEY (`season_id`) REFERENCES `procurement_seasons` (`season_id`) ON DELETE RESTRICT ON UPDATE CASCADE,
  CONSTRAINT `chk_booked_capacity` CHECK (((`booked_capacity_quintal` >= 0) and (`booked_capacity_quintal` <= `max_capacity_quintal`))),
  CONSTRAINT `chk_schedule_capacity` CHECK ((`max_capacity_quintal` > 0)),
  CONSTRAINT `chk_schedule_time` CHECK ((`end_time` > `start_time`))
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `procurement_seasons`
--

DROP TABLE IF EXISTS `procurement_seasons`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `procurement_seasons` (
  `season_id` bigint unsigned NOT NULL AUTO_INCREMENT,
  `season_name` varchar(100) NOT NULL,
  `start_date` date NOT NULL,
  `end_date` date NOT NULL,
  `is_active` tinyint(1) NOT NULL DEFAULT '1',
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`season_id`),
  UNIQUE KEY `season_name` (`season_name`),
  CONSTRAINT `chk_season_dates` CHECK ((`end_date` >= `start_date`))
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `season_crops`
--

DROP TABLE IF EXISTS `season_crops`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `season_crops` (
  `season_id` bigint unsigned NOT NULL,
  `crop_id` bigint unsigned NOT NULL,
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`season_id`,`crop_id`),
  KEY `fk_season_crops_crop` (`crop_id`),
  CONSTRAINT `fk_season_crops_crop` FOREIGN KEY (`crop_id`) REFERENCES `crops` (`crop_id`) ON DELETE RESTRICT ON UPDATE CASCADE,
  CONSTRAINT `fk_season_crops_season` FOREIGN KEY (`season_id`) REFERENCES `procurement_seasons` (`season_id`) ON DELETE RESTRICT ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `users`
--

DROP TABLE IF EXISTS `users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `users` (
  `user_id` bigint unsigned NOT NULL AUTO_INCREMENT,
  `phone_number` varchar(15) NOT NULL,
  `role` enum('FARMER','CENTRE_OPERATOR','ADMIN') NOT NULL,
  `is_active` tinyint(1) NOT NULL DEFAULT '1',
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`user_id`),
  UNIQUE KEY `phone_number` (`phone_number`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `waiting_list`
--

DROP TABLE IF EXISTS `waiting_list`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `waiting_list` (
  `waiting_id` bigint unsigned NOT NULL AUTO_INCREMENT,
  `farmer_id` bigint unsigned NOT NULL,
  `schedule_id` bigint unsigned NOT NULL,
  `quantity_quintal` decimal(10,2) NOT NULL,
  `queue_position` int unsigned NOT NULL,
  `status` enum('WAITING','OFFERED','ACCEPTED','EXPIRED','CANCELLED') NOT NULL DEFAULT 'WAITING',
  `joined_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`waiting_id`),
  KEY `idx_waiting_farmer` (`farmer_id`),
  KEY `idx_waiting_schedule` (`schedule_id`),
  KEY `idx_waiting_queue` (`schedule_id`,`queue_position`),
  KEY `idx_waiting_status` (`status`),
  CONSTRAINT `fk_waiting_farmer` FOREIGN KEY (`farmer_id`) REFERENCES `farmers` (`farmer_id`) ON DELETE RESTRICT ON UPDATE CASCADE,
  CONSTRAINT `fk_waiting_schedule` FOREIGN KEY (`schedule_id`) REFERENCES `procurement_schedules` (`schedule_id`) ON DELETE RESTRICT ON UPDATE CASCADE,
  CONSTRAINT `chk_queue_position` CHECK ((`queue_position` > 0)),
  CONSTRAINT `chk_waiting_quantity` CHECK ((`quantity_quintal` > 0))
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping events for database 'agri_procurement_system'
--

--
-- Dumping routines for database 'agri_procurement_system'
--
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2026-09-02 13:01:43