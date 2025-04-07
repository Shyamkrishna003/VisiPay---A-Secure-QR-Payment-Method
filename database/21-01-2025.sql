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
  `ifsc` varchar(100) DEFAULT NULL,
  `balance` varchar(100) DEFAULT NULL,
  `date_time` varchar(100) DEFAULT NULL,
  `status` varchar(100) DEFAULT NULL,
  `qr` varchar(1000) DEFAULT NULL,
  PRIMARY KEY (`account_id`)
) ENGINE=MyISAM AUTO_INCREMENT=3 DEFAULT CHARSET=latin1;

/*Data for the table `account` */

insert  into `account`(`account_id`,`customer_id`,`bank_id`,`account_number`,`ifsc`,`balance`,`date_time`,`status`,`qr`) values 
(1,3,NULL,'164995111111','SBIL0462726','2936','2024-12-02 14:18:55',NULL,NULL),
(2,2,NULL,'12345678912','SBIL0462721','5780','2025-01-21 12:04:11',NULL,NULL);

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
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

/*Data for the table `bank` */

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
  `user_id` int(11) DEFAULT NULL,
  `customer_id` int(11) DEFAULT NULL,
  `complaint` varchar(100) DEFAULT NULL,
  `replay` varchar(100) DEFAULT NULL,
  `date` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`complaint_id`)
) ENGINE=MyISAM AUTO_INCREMENT=7 DEFAULT CHARSET=latin1;

/*Data for the table `complaint` */

insert  into `complaint`(`complaint_id`,`user_id`,`customer_id`,`complaint`,`replay`,`date`) values 
(1,NULL,3,'hai','hello','2024-12-02'),
(2,NULL,2,'here some issues','ok','2024-12-02'),
(3,NULL,2,'dgv','pending','2024-12-14'),
(4,NULL,2,'fgh','hhj','2024-12-19'),
(5,NULL,2,'this is wrong','pending','2025-01-15'),
(6,NULL,2,'ebeh','pending','2025-01-21');

/*Table structure for table `customer` */

DROP TABLE IF EXISTS `customer`;

CREATE TABLE `customer` (
  `customer_id` int(11) NOT NULL AUTO_INCREMENT,
  `login_id` int(11) DEFAULT NULL,
  `f_name` varchar(100) DEFAULT NULL,
  `l_name` varchar(100) DEFAULT NULL,
  `house_name` varchar(100) DEFAULT NULL,
  `place` varchar(100) DEFAULT NULL,
  `pincode` varchar(100) DEFAULT NULL,
  `phone` varchar(100) DEFAULT NULL,
  `email` varchar(100) DEFAULT NULL,
  `gender` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`customer_id`)
) ENGINE=MyISAM AUTO_INCREMENT=3 DEFAULT CHARSET=latin1;

/*Data for the table `customer` */

insert  into `customer`(`customer_id`,`login_id`,`f_name`,`l_name`,`house_name`,`place`,`pincode`,`phone`,`email`,`gender`) values 
(1,3,'dhsh','hdhdh','dhshsh','ajsjsj','656565','4646464946','athulsoman015@gmail.com','Male'),
(2,5,'snsn','zhzhn','sjsjs','djdjsn','464646','4646464656','akash.edva@gmail.com','Male');

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
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

/*Data for the table `employee` */

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
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

/*Data for the table `loan` */

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
) ENGINE=MyISAM AUTO_INCREMENT=7 DEFAULT CHARSET=latin1;

/*Data for the table `login` */

insert  into `login`(`login_id`,`username`,`password`,`usertype`) values 
(1,'admin','admin','admin'),
(2,'mer','mer','merchant'),
(3,'cus2','cus2','customer'),
(4,'mer2','mer2','merchant'),
(5,'cus','cus','customer'),
(6,'shshsh','2Asg@hssh','pending');

/*Table structure for table `merchant` */

DROP TABLE IF EXISTS `merchant`;

CREATE TABLE `merchant` (
  `merchant_id` int(11) NOT NULL AUTO_INCREMENT,
  `login_id` int(11) DEFAULT NULL,
  `merchantname` varchar(100) DEFAULT NULL,
  `place` varchar(100) DEFAULT NULL,
  `phone` varchar(100) DEFAULT NULL,
  `email` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`merchant_id`)
) ENGINE=MyISAM AUTO_INCREMENT=4 DEFAULT CHARSET=latin1;

/*Data for the table `merchant` */

insert  into `merchant`(`merchant_id`,`login_id`,`merchantname`,`place`,`phone`,`email`) values 
(1,2,'dfgc','cthv','8288888856','ctg@fgvc'),
(2,4,'dhshsh','dhshsh','6464694464','hshshs@bdhsh'),
(3,6,'shsha','ahsh','4949441364','shshs@gmail.com');

/*Table structure for table `notification` */

DROP TABLE IF EXISTS `notification`;

CREATE TABLE `notification` (
  `notification_id` int(11) NOT NULL AUTO_INCREMENT,
  `bank_id` int(11) DEFAULT NULL,
  `notification` varchar(100) DEFAULT NULL,
  `date` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`notification_id`)
) ENGINE=MyISAM AUTO_INCREMENT=9 DEFAULT CHARSET=latin1;

/*Data for the table `notification` */

insert  into `notification`(`notification_id`,`bank_id`,`notification`,`date`) values 
(1,0,'helloo','2024-12-02'),
(2,0,'fgf','2024-12-02'),
(3,0,'werwer','2024-12-02'),
(4,0,'hi','2024-12-02'),
(5,0,'hi','2024-12-02'),
(6,0,'zzzzzzz','2024-12-02'),
(7,0,'lklkk','2024-12-02'),
(8,0,'asdad','2024-12-02');

/*Table structure for table `payment` */

DROP TABLE IF EXISTS `payment`;

CREATE TABLE `payment` (
  `payment_id` int(11) NOT NULL AUTO_INCREMENT,
  `transfer_id` int(11) DEFAULT NULL,
  `amount` varchar(100) DEFAULT NULL,
  `date` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`payment_id`)
) ENGINE=MyISAM AUTO_INCREMENT=25 DEFAULT CHARSET=latin1;

/*Data for the table `payment` */

insert  into `payment`(`payment_id`,`transfer_id`,`amount`,`date`) values 
(1,1,'10','2024-12-02'),
(2,2,'100','2024-12-02'),
(3,3,'500','2024-12-02'),
(4,4,'12','2024-12-02'),
(5,5,'50','2024-12-02'),
(6,6,'13.0','2024-12-02'),
(7,7,'100.0','2024-12-02'),
(8,8,'100.0','2024-12-02'),
(9,9,'20.0','2024-12-02'),
(10,10,'100.0','2024-12-02'),
(11,11,'100.0','2024-12-02'),
(12,12,'10.0','2024-12-02'),
(13,14,'50.0','2024-12-03'),
(14,13,'100.0','2024-12-03'),
(15,15,'100.0','2024-12-03'),
(16,16,'8.0','2024-12-03'),
(17,17,'100.0','2024-12-14'),
(18,19,'100.0','2024-12-14'),
(19,20,'100.0','2024-12-14'),
(20,21,'500.0','2024-12-31'),
(21,22,'100.0','2024-12-31'),
(22,23,'100.0','2024-12-31'),
(23,24,'100.0','2024-12-31'),
(24,18,'95.0','2025-01-21');

/*Table structure for table `transaction` */

DROP TABLE IF EXISTS `transaction`;

CREATE TABLE `transaction` (
  `transaction_id` int(11) NOT NULL AUTO_INCREMENT,
  `from_ac` varchar(100) DEFAULT NULL,
  `from_ifsc` varchar(100) DEFAULT NULL,
  `to_ac` varchar(100) DEFAULT NULL,
  `to_ifsc` varchar(100) DEFAULT NULL,
  `amount` varchar(100) DEFAULT NULL,
  `date_time` varchar(100) DEFAULT NULL,
  `customer_id` int(11) DEFAULT NULL,
  `type` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`transaction_id`)
) ENGINE=MyISAM AUTO_INCREMENT=19 DEFAULT CHARSET=latin1;

/*Data for the table `transaction` */

insert  into `transaction`(`transaction_id`,`from_ac`,`from_ifsc`,`to_ac`,`to_ifsc`,`amount`,`date_time`,`customer_id`,`type`) values 
(1,'12395482391245','ABCD0123345','164995111111','SBIL0462726','100.0','2024-12-02',2,'online'),
(2,'164995111111','SBIL0462726','154646494949','AJSJ0171728','100.0','2024-12-02',3,'online'),
(3,'164995111111','SBIL0462726','12395482391245','ABCD0123345','149.0','2024-12-02',3,'online'),
(4,'164995111111','SBIL0462726','12395482391245','ABCD0123345','149.0','2024-12-02',3,'online'),
(5,'164995111111','SBIL0462726','12345678912','SBIL0462721','512.0','2024-12-02',3,'online'),
(6,'164995111111','SBIL0462726','12345678912','SBIL0462721','1674.0','2024-12-02',3,'online'),
(7,'12345678912','SBIL0462721','746464649499','AJAJ0282822','500.0','2024-12-02',2,'online'),
(8,'12345678912','SBIL0462721','746464649499638','AJAJ0282822','500.0','2024-12-02',2,'online'),
(9,'12345678912','SBIL0462721','164995111111','AJAJ0282822','500.0','2024-12-02',2,'online'),
(10,'164995111111','SBIL0462726','4949446464646494','SBIO0373831','1.0','2024-12-31',3,'online'),
(11,'12345678912','SBIL0462721','164995111111','SBSS0282282','100.0','2024-12-31',2,'online'),
(12,'12345678912','SBIL0462721','164995111111','SBSS0282282','100.0','2024-12-31',2,'online'),
(13,'12345678912','SBIL0462721','164995111111','SBIL0462726','100.0','2024-12-31',2,'online'),
(14,'164995111111','SBIL0462726','12345678912','SBIL0462721','200.0','2024-12-31',3,'online'),
(15,'12345678912','SBIL0462721','164995111111','SBIL0462726','10.0','2024-12-31',2,'online'),
(16,'164995111111','SBIL0462726','12345678912','SBIL0121212','10.0','2024-12-31',3,'online'),
(17,'164995111111','SBIL0462726','12345678912','SBIL0462721','10.0','2024-12-31',3,'online'),
(18,'164995111111','SBIL0462726','12345678912','SBIL0462721','9.0','2024-12-31',3,'online');

/*Table structure for table `transfer` */

DROP TABLE IF EXISTS `transfer`;

CREATE TABLE `transfer` (
  `transfer_id` int(11) NOT NULL AUTO_INCREMENT,
  `sender_id` int(11) DEFAULT NULL,
  `sendertype` varchar(100) DEFAULT NULL,
  `receiver_id` int(11) DEFAULT NULL,
  `receivertype` varchar(100) DEFAULT NULL,
  `amount` varchar(100) DEFAULT NULL,
  `date` varchar(100) DEFAULT NULL,
  `status` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`transfer_id`)
) ENGINE=MyISAM AUTO_INCREMENT=26 DEFAULT CHARSET=latin1;

/*Data for the table `transfer` */

insert  into `transfer`(`transfer_id`,`sender_id`,`sendertype`,`receiver_id`,`receivertype`,`amount`,`date`,`status`) values 
(1,3,'customer',2,'merchant','10','2024-12-02','paid'),
(2,3,'customer',2,'merchant','100','2024-12-02','paid'),
(3,3,'customer',4,'merchant','500','2024-12-02','paid'),
(4,3,'customer',4,'merchant','12','2024-12-02','paid'),
(5,3,'customer',2,'merchant','50','2024-12-02','paid'),
(6,3,'customer',2,'merchant','13','2024-12-02','paid'),
(7,3,'customer',2,'merchant','100','2024-12-02','paid'),
(8,3,'customer',2,'merchant','100','2024-12-02','paid'),
(9,3,'customer',2,'merchant','20','2024-12-02','paid'),
(10,3,'customer',5,'customer','100','2024-12-02','paid'),
(11,3,'customer',2,'merchant','100.0','2024-12-02','paid'),
(12,3,'customer',3,'customer','10.0','2024-12-02','paid'),
(13,5,'customer',3,'customer','100','2024-12-03','paid'),
(14,3,'customer',3,'customer','50','2024-12-03','paid'),
(15,5,'customer',3,'customer','100','2024-12-03','paid'),
(16,3,'customer',5,'customer','8','2024-12-03','paid'),
(17,3,'customer',2,'merchant','100','2024-12-14','paid'),
(18,5,'customer',2,'merchant','95','2024-12-14','paid'),
(19,3,'customer',2,'merchant','100.0','2024-12-14','paid'),
(20,3,'customer',2,'merchant','100.0','2024-12-14','paid'),
(21,3,'customer',2,'merchant','500.0','2024-12-31','paid'),
(22,3,'customer',2,'merchant','100.0','2024-12-31','paid'),
(23,3,'customer',2,'merchant','100.0','2024-12-31','paid'),
(24,3,'customer',2,'merchant','100.0','2024-12-31','paid'),
(25,3,'customer',2,'merchant','100','2025-01-21','pending');

/*Table structure for table `upload_file` */

DROP TABLE IF EXISTS `upload_file`;

CREATE TABLE `upload_file` (
  `upload_file_id` int(11) NOT NULL AUTO_INCREMENT,
  `customer_id` int(11) DEFAULT NULL,
  `img1` varchar(1000) DEFAULT NULL,
  `img2` varchar(1000) DEFAULT NULL,
  PRIMARY KEY (`upload_file_id`)
) ENGINE=MyISAM AUTO_INCREMENT=8 DEFAULT CHARSET=latin1;

/*Data for the table `upload_file` */

insert  into `upload_file`(`upload_file_id`,`customer_id`,`img1`,`img2`) values 
(1,1,'static/qrcode/9be6d64d-b23d-424a-b22f-353dfae7775eabc.jpg','static/qrcode/756fa258-19af-43d3-af06-39261b78f101abc.jpg'),
(2,1,'static/qrcode/efce99ae-d9d5-49f4-b4e4-ddf1cad9f953abc.jpg','static/qrcode/756fa258-19af-43d3-af06-39261b78f101abc.jpg'),
(3,2,'static/qrcode/f032fda9-e06f-48e1-9b8b-23c30aef124fabc.jpg','static/qrcode/1fb88fec-0a77-45d3-911d-9bb27942f9bcabc.jpg'),
(4,2,'static/qrcode/99771277-a76f-4a1a-9893-1edecb19b621abc.jpg','static/qrcode/1fb88fec-0a77-45d3-911d-9bb27942f9bcabc.jpg'),
(5,2,'static/qrcode/06b0534b-a57a-4ff2-87ac-3537721e83ababc.jpg','static/qrcode/1fb88fec-0a77-45d3-911d-9bb27942f9bcabc.jpg'),
(6,2,'static/qrcode/f62fc7b6-f9bb-482b-9704-11c47d271742abc.jpg','static/qrcode/1fb88fec-0a77-45d3-911d-9bb27942f9bcabc.jpg'),
(7,1,'static/qrcode/49d37420-7198-4d33-ad3c-c631112b315dabc.jpg',NULL);

/*Table structure for table `user` */

DROP TABLE IF EXISTS `user`;

CREATE TABLE `user` (
  `user_id` int(11) NOT NULL AUTO_INCREMENT,
  `login_id` int(11) DEFAULT NULL,
  `fname` varchar(100) DEFAULT NULL,
  `lname` varchar(100) DEFAULT NULL,
  `house` varchar(100) DEFAULT NULL,
  `place` varchar(100) DEFAULT NULL,
  `phone` varchar(100) DEFAULT NULL,
  `email` varchar(100) DEFAULT NULL,
  `gender` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`user_id`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

/*Data for the table `user` */

/*Table structure for table `wallet` */

DROP TABLE IF EXISTS `wallet`;

CREATE TABLE `wallet` (
  `wallet_id` int(11) NOT NULL AUTO_INCREMENT,
  `customer_id` int(11) DEFAULT NULL,
  `amount` varchar(100) DEFAULT NULL,
  `customer_type` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`wallet_id`)
) ENGINE=MyISAM AUTO_INCREMENT=6 DEFAULT CHARSET=latin1;

/*Data for the table `wallet` */

insert  into `wallet`(`wallet_id`,`customer_id`,`amount`,`customer_type`) values 
(1,3,'160','customer'),
(2,2,'1279','merchant'),
(3,5,'313','customer'),
(4,2,'1179','merchant'),
(5,4,'0','merchant');

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
