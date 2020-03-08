/*
SQLyog v10.2 
MySQL - 5.7.21-log : Database - xc_learning
*********************************************************************
*/

/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
CREATE DATABASE /*!32312 IF NOT EXISTS*/`xc_learning` /*!40100 DEFAULT CHARACTER SET utf8 */;

USE `xc_learning`;

/*Table structure for table `xc_learning_course` */

DROP TABLE IF EXISTS `xc_learning_course`;

CREATE TABLE `xc_learning_course` (
  `id` varchar(32) NOT NULL,
  `course_id` varchar(32) NOT NULL COMMENT '课程id',
  `user_id` varchar(32) NOT NULL COMMENT '用户id',
  `valid` varchar(32) DEFAULT NULL COMMENT '有效性',
  `start_time` datetime DEFAULT NULL,
  `end_time` datetime DEFAULT NULL,
  `status` varchar(32) DEFAULT NULL COMMENT '选课状态',
  PRIMARY KEY (`id`),
  UNIQUE KEY `xc_learning_list_unique` (`course_id`,`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `xc_learning_course` */

insert  into `xc_learning_course`(`id`,`course_id`,`user_id`,`valid`,`start_time`,`end_time`,`status`) values ('402885816243d2dd016243f24c030002','402885816243d2dd016243f24c030002','49',NULL,NULL,NULL,'501001'),('8a7e82b564b5e53f0164b5ee61e50002','4028e581617f945f01617f9dabc40000','49',NULL,NULL,NULL,'501001'),('8a7e82b564b5e53f0164b5ee6b780003','4028e581617f945f01617f9dabc40001','49',NULL,NULL,NULL,'501001');

/*Table structure for table `xc_task_his` */

DROP TABLE IF EXISTS `xc_task_his`;

CREATE TABLE `xc_task_his` (
  `id` varchar(32) NOT NULL COMMENT '任务id',
  `create_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  `delete_time` datetime DEFAULT NULL,
  `task_type` varchar(32) DEFAULT NULL COMMENT '任务类型',
  `mq_exchange` varchar(64) DEFAULT NULL COMMENT '交换机名称',
  `mq_routingkey` varchar(64) DEFAULT NULL COMMENT 'routingkey',
  `request_body` varchar(512) DEFAULT NULL COMMENT '任务请求的内容',
  `version` int(10) DEFAULT '0' COMMENT '乐观锁版本号',
  `status` varchar(32) DEFAULT NULL COMMENT '任务状态',
  `errormsg` varchar(512) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `xc_task_his` */

insert  into `xc_task_his`(`id`,`create_time`,`update_time`,`delete_time`,`task_type`,`mq_exchange`,`mq_routingkey`,`request_body`,`version`,`status`,`errormsg`) values ('10','2018-04-04 22:58:20','2018-07-13 22:58:54','2018-07-16 12:24:36',NULL,'ex_learning_addchoosecourse','addchoosecourse','{\"userId\":\"49\",\"courseId\":\"4028e581617f945f01617f9dabc40000\"}',NULL,'10201',NULL),('11','2018-07-16 12:28:03','2018-07-15 12:28:04','2018-07-16 12:29:11',NULL,'ex_learning_addchoosecourse','addchoosecourse','{\"userId\":\"49\",\"courseId\":\"4028e581617f945f01617f9dabc40001\"}',NULL,NULL,NULL);

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
