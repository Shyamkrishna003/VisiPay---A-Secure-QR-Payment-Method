/*
SQLyog Community v13.1.6 (64 bit)
MySQL - 5.7.9 : Database - bank_transaction
*********************************************************************
*/

/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
CREATE DATABASE /*!32312 IF NOT EXISTS*/`bank_transaction` /*!40100 DEFAULT CHARACTER SET latin1 */;

USE `bank_transaction`;

/*Table structure for table `account` */

DROP TABLE IF EXISTS `account`;

CREATE TABLE `account` (
  `account_id` int(11) NOT NULL AUTO_INCREMENT,
  `customer_id` int(11) DEFAULT NULL,
  `bank_id` int(11) DEFAULT NULL,
  `account_number` varchar(100) DEFAULT NULL,
  `balance` int(100) DEFAULT NULL,
  `date_time` varchar(100) DEFAULT NULL,
  `status` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`account_id`)
) ENGINE=MyISAM AUTO_INCREMENT=3 DEFAULT CHARSET=latin1;

/*Data for the table `account` */

insert  into `account`(`account_id`,`customer_id`,`bank_id`,`account_number`,`balance`,`date_time`,`status`) values 
(1,2,2,'12345678910',1500,'2023-01-14 12:18:13','pending'),
(2,4,1,'78251986335',1000,'2023-01-14 14:45:46','pending');

/*Table structure for table `bank` */

DROP TABLE IF EXISTS `bank`;

CREATE TABLE `bank` (
  `bank_id` int(11) NOT NULL AUTO_INCREMENT,
  `login_id` int(11) DEFAULT NULL,
  `bank_name` varchar(100) DEFAULT NULL,
  `place` varchar(100) DEFAULT NULL,
  `phone` varchar(100) DEFAULT NULL,
  `email` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`bank_id`)
) ENGINE=MyISAM AUTO_INCREMENT=3 DEFAULT CHARSET=latin1;

/*Data for the table `bank` */

insert  into `bank`(`bank_id`,`login_id`,`bank_name`,`place`,`phone`,`email`) values 
(1,2,'axis','ernakulam','9947270209','axis@gmail.com'),
(2,3,'sbi','ktm','9847050556','sbi@gmail.com');

/*Table structure for table `chat` */

DROP TABLE IF EXISTS `chat`;

CREATE TABLE `chat` (
  `chat_id` int(11) NOT NULL AUTO_INCREMENT,
  `sender_id` int(11) DEFAULT NULL,
  `receiver_id` int(11) DEFAULT NULL,
  `message` varchar(100) DEFAULT NULL,
  `date` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`chat_id`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

/*Data for the table `chat` */

/*Table structure for table `complaint` */

DROP TABLE IF EXISTS `complaint`;

CREATE TABLE `complaint` (
  `complaint_id` int(11) NOT NULL AUTO_INCREMENT,
  `customer_id` int(11) DEFAULT NULL,
  `complaint` varchar(100) DEFAULT NULL,
  `replay` varchar(100) DEFAULT NULL,
  `date` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`complaint_id`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

/*Data for the table `complaint` */

/*Table structure for table `customer` */

DROP TABLE IF EXISTS `customer`;

CREATE TABLE `customer` (
  `customer_id` int(11) NOT NULL AUTO_INCREMENT,
  `login_id` int(11) DEFAULT NULL,
  `security_key` varchar(100) DEFAULT NULL,
  `f_name` varchar(100) DEFAULT NULL,
  `l_name` varchar(100) DEFAULT NULL,
  `house_name` varchar(100) DEFAULT NULL,
  `place` varchar(100) DEFAULT NULL,
  `pincode` varchar(100) DEFAULT NULL,
  `phone` varchar(100) DEFAULT NULL,
  `email` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`customer_id`)
) ENGINE=MyISAM AUTO_INCREMENT=5 DEFAULT CHARSET=latin1;

/*Data for the table `customer` */

insert  into `customer`(`customer_id`,`login_id`,`security_key`,`f_name`,`l_name`,`house_name`,`place`,`pincode`,`phone`,`email`) values 
(1,6,'123','ammu','mol','thoppil','kannur','789456','9999999999','ammu@gmail.com'),
(2,6,'123','ammu','mol','thoppil','kannur','789456','9999999999','ammu@gmail.com'),
(3,7,'333333','RESHMA','SANIL','thoppil','ktm','789456','9999999999','ammu@gmail.com'),
(4,7,'333333','RESHMA','SANIL','thoppil','ktm','789456','9999999999','ammu@gmail.com');

/*Table structure for table `employee` */

DROP TABLE IF EXISTS `employee`;

CREATE TABLE `employee` (
  `employee_id` int(11) NOT NULL AUTO_INCREMENT,
  `login_id` int(11) DEFAULT NULL,
  `bank_id` int(11) DEFAULT NULL,
  `first_name` varchar(100) DEFAULT NULL,
  `last_name` varchar(100) DEFAULT NULL,
  `house_name` varchar(100) DEFAULT NULL,
  `place` varchar(100) DEFAULT NULL,
  `gender` varchar(100) DEFAULT NULL,
  `phone` varchar(100) DEFAULT NULL,
  `email` varchar(100) DEFAULT NULL,
  `qualification` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`employee_id`)
) ENGINE=MyISAM AUTO_INCREMENT=3 DEFAULT CHARSET=latin1;

/*Data for the table `employee` */

insert  into `employee`(`employee_id`,`login_id`,`bank_id`,`first_name`,`last_name`,`house_name`,`place`,`gender`,`phone`,`email`,`qualification`) values 
(1,4,2,'achu','kk','kottkkal','ktm','male','7410896532','achu@gmail.com','b.com'),
(2,5,1,'anu','anumol','palathikkal','palakkad','female','9988776655','anna@gmail.com','b.com');

/*Table structure for table `loan` */

DROP TABLE IF EXISTS `loan`;

CREATE TABLE `loan` (
  `loan_id` int(11) NOT NULL AUTO_INCREMENT,
  `bank_id` int(11) DEFAULT NULL,
  `loan_name` varchar(100) DEFAULT NULL,
  `details` varchar(100) DEFAULT NULL,
  `interest_name` varchar(100) DEFAULT NULL,
  `max_amount` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`loan_id`)
) ENGINE=MyISAM AUTO_INCREMENT=3 DEFAULT CHARSET=latin1;

/*Data for the table `loan` */

insert  into `loan`(`loan_id`,`bank_id`,`loan_name`,`details`,`interest_name`,`max_amount`) values 
(1,1,'education ','helps to students ','education loan for students','10,00,000'),
(2,2,'farmers loan','helps to farmers ','farmer loan','20000');

/*Table structure for table `loanrequest` */

DROP TABLE IF EXISTS `loanrequest`;

CREATE TABLE `loanrequest` (
  `request_id` int(11) NOT NULL AUTO_INCREMENT,
  `customer_id` int(11) DEFAULT NULL,
  `loan_id` int(11) DEFAULT NULL,
  `date_time` varchar(100) DEFAULT NULL,
  `status` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`request_id`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

/*Data for the table `loanrequest` */

/*Table structure for table `login` */

DROP TABLE IF EXISTS `login`;

CREATE TABLE `login` (
  `login_id` int(11) NOT NULL AUTO_INCREMENT,
  `username` varchar(100) DEFAULT NULL,
  `password` varchar(100) DEFAULT NULL,
  `usertype` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`login_id`)
) ENGINE=MyISAM AUTO_INCREMENT=8 DEFAULT CHARSET=latin1;

/*Data for the table `login` */

insert  into `login`(`login_id`,`username`,`password`,`usertype`) values 
(1,'admin','admin','admin'),
(2,'axis','axis','bank'),
(3,'sbi','sbi','bank'),
(4,'achu','achu','employee'),
(5,'anu','anu','employee'),
(6,'ammu','ammu','customer'),
(7,'dsadas','vdsvd','customer');

/*Table structure for table `notification` */

DROP TABLE IF EXISTS `notification`;

CREATE TABLE `notification` (
  `notification_id` int(11) NOT NULL AUTO_INCREMENT,
  `bank_id` int(11) DEFAULT NULL,
  `notification` varchar(100) DEFAULT NULL,
  `date` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`notification_id`)
) ENGINE=MyISAM AUTO_INCREMENT=3 DEFAULT CHARSET=latin1;

/*Data for the table `notification` */

insert  into `notification`(`notification_id`,`bank_id`,`notification`,`date`) values 
(1,1,'alert','2023-01-14'),
(2,2,'warning','2023-01-14');

/*Table structure for table `transaction` */

DROP TABLE IF EXISTS `transaction`;

CREATE TABLE `transaction` (
  `transaction_id` int(11) NOT NULL AUTO_INCREMENT,
  `from_ac` int(11) DEFAULT NULL,
  `to_ac` int(11) DEFAULT NULL,
  `amount` varchar(100) DEFAULT NULL,
  `date_time` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`transaction_id`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

/*Data for the table `transaction` */

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
