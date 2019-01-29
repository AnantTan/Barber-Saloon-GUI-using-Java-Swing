-- phpMyAdmin SQL Dump
-- version 4.8.3
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Jan 29, 2019 at 04:38 PM
-- Server version: 10.1.35-MariaDB
-- PHP Version: 7.2.9

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET AUTOCOMMIT = 0;
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `barbershop`
--

-- --------------------------------------------------------

--
-- Table structure for table `barber`
--

CREATE TABLE `barber` (
  `first_name` text NOT NULL,
  `last_name` text NOT NULL,
  `phone_number` int(12) NOT NULL,
  `email_address` varchar(50) NOT NULL,
  `location` varchar(50) NOT NULL,
  `verified` text NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `booking`
--

CREATE TABLE `booking` (
  `person_email` varchar(50) NOT NULL,
  `booked_with` varchar(50) DEFAULT NULL,
  `shop_address` varchar(50) NOT NULL,
  `booking_confirmation` text NOT NULL,
  `date` date NOT NULL,
  `slot` varchar(20) NOT NULL,
  `taken` text NOT NULL,
  `status` text NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `complaint`
--

CREATE TABLE `complaint` (
  `by_email` varchar(50) NOT NULL,
  `about` text NOT NULL,
  `description` varchar(300) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `credentials`
--

CREATE TABLE `credentials` (
  `role` text NOT NULL,
  `email_address` varchar(50) NOT NULL,
  `password` varchar(12) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `credentials`
--

INSERT INTO `credentials` (`role`, `email_address`, `password`) VALUES
('administrator', 'admin@admin.admin', 'Pass123!');

-- --------------------------------------------------------

--
-- Table structure for table `customer`
--

CREATE TABLE `customer` (
  `first_name` text NOT NULL,
  `last_name` text NOT NULL,
  `phone_number` int(20) NOT NULL,
  `email_address` varchar(50) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Indexes for dumped tables
--

--
-- Indexes for table `barber`
--
ALTER TABLE `barber`
  ADD KEY `FK_email_address` (`email_address`);

--
-- Indexes for table `booking`
--
ALTER TABLE `booking`
  ADD UNIQUE KEY `FK_person_email` (`person_email`),
  ADD KEY `FK_booked_with` (`booked_with`);

--
-- Indexes for table `credentials`
--
ALTER TABLE `credentials`
  ADD PRIMARY KEY (`email_address`);

--
-- Indexes for table `customer`
--
ALTER TABLE `customer`
  ADD KEY `FK_email_address` (`email_address`);

--
-- Constraints for dumped tables
--

--
-- Constraints for table `barber`
--
ALTER TABLE `barber`
  ADD CONSTRAINT `barber_ibfk_1` FOREIGN KEY (`email_address`) REFERENCES `credentials` (`email_address`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `booking`
--
ALTER TABLE `booking`
  ADD CONSTRAINT `FK_booked_with` FOREIGN KEY (`booked_with`) REFERENCES `credentials` (`email_address`),
  ADD CONSTRAINT `booking_ibfk_1` FOREIGN KEY (`person_email`) REFERENCES `credentials` (`email_address`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `customer`
--
ALTER TABLE `customer`
  ADD CONSTRAINT `customer_ibfk_1` FOREIGN KEY (`email_address`) REFERENCES `credentials` (`email_address`) ON DELETE CASCADE ON UPDATE CASCADE;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
