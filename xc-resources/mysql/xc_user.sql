/*
SQLyog v10.2 
MySQL - 5.7.21-log : Database - xc_user
*********************************************************************
*/

/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
CREATE DATABASE /*!32312 IF NOT EXISTS*/`xc_user` /*!40100 DEFAULT CHARACTER SET utf8 */;

USE `xc_user`;

/*Table structure for table `oauth_access_token` */

DROP TABLE IF EXISTS `oauth_access_token`;

CREATE TABLE `oauth_access_token` (
  `token_id` varchar(256) DEFAULT NULL,
  `token` blob,
  `authentication_id` varchar(48) NOT NULL,
  `user_name` varchar(256) DEFAULT NULL,
  `client_id` varchar(256) DEFAULT NULL,
  `authentication` blob,
  `refresh_token` varchar(256) DEFAULT NULL,
  PRIMARY KEY (`authentication_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `oauth_access_token` */

/*Table structure for table `oauth_approvals` */

DROP TABLE IF EXISTS `oauth_approvals`;

CREATE TABLE `oauth_approvals` (
  `userId` varchar(256) DEFAULT NULL,
  `clientId` varchar(256) DEFAULT NULL,
  `scope` varchar(256) DEFAULT NULL,
  `status` varchar(10) DEFAULT NULL,
  `expiresAt` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `lastModifiedAt` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00'
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `oauth_approvals` */

/*Table structure for table `oauth_client_details` */

DROP TABLE IF EXISTS `oauth_client_details`;

CREATE TABLE `oauth_client_details` (
  `client_id` varchar(48) NOT NULL,
  `resource_ids` varchar(256) DEFAULT NULL,
  `client_secret` varchar(256) DEFAULT NULL,
  `scope` varchar(256) DEFAULT NULL,
  `authorized_grant_types` varchar(256) DEFAULT NULL,
  `web_server_redirect_uri` varchar(256) DEFAULT NULL,
  `authorities` varchar(256) DEFAULT NULL,
  `access_token_validity` int(11) DEFAULT NULL,
  `refresh_token_validity` int(11) DEFAULT NULL,
  `additional_information` varchar(4096) DEFAULT NULL,
  `autoapprove` varchar(256) DEFAULT NULL,
  PRIMARY KEY (`client_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `oauth_client_details` */

insert  into `oauth_client_details`(`client_id`,`resource_ids`,`client_secret`,`scope`,`authorized_grant_types`,`web_server_redirect_uri`,`authorities`,`access_token_validity`,`refresh_token_validity`,`additional_information`,`autoapprove`) values ('app',NULL,'app','app','password,refresh_token',NULL,NULL,NULL,NULL,NULL,NULL),('XcWebApp',NULL,'$2a$10$9bEpZ/hWRQxyr5hn5wHUj.jxFpIrnOmBcWlE/g/0Zp3uNxt9QTh/S','app','authorization_code,password,refresh_token,client_credentials',NULL,NULL,43200,43200,NULL,NULL);

/*Table structure for table `oauth_client_token` */

DROP TABLE IF EXISTS `oauth_client_token`;

CREATE TABLE `oauth_client_token` (
  `token_id` varchar(256) DEFAULT NULL,
  `token` blob,
  `authentication_id` varchar(48) NOT NULL,
  `user_name` varchar(256) DEFAULT NULL,
  `client_id` varchar(256) DEFAULT NULL,
  PRIMARY KEY (`authentication_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `oauth_client_token` */

/*Table structure for table `oauth_code` */

DROP TABLE IF EXISTS `oauth_code`;

CREATE TABLE `oauth_code` (
  `code` varchar(256) DEFAULT NULL,
  `authentication` blob
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `oauth_code` */

/*Table structure for table `oauth_refresh_token` */

DROP TABLE IF EXISTS `oauth_refresh_token`;

CREATE TABLE `oauth_refresh_token` (
  `token_id` varchar(256) DEFAULT NULL,
  `token` blob,
  `authentication` blob
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `oauth_refresh_token` */

insert  into `oauth_refresh_token`(`token_id`,`token`,`authentication`) values ('b96e057b4e1c4920428e833db48d4c15','��\0sr\0Lorg.springframework.security.oauth2.common.DefaultExpiringOAuth2RefreshToken/�Gc��ɷ\0L\0\nexpirationt\0Ljava/util/Date;xr\0Dorg.springframework.security.oauth2.common.DefaultOAuth2RefreshTokens�\ncT�^\0L\0valuet\0Ljava/lang/String;xpt\0$b012d487-a908-43af-a8ef-53c5399c8bd6sr\0java.util.Datehj�KYt\0\0xpw\0\0a;tɎx','��\0sr\0Aorg.springframework.security.oauth2.provider.OAuth2Authentication�@bR\0L\0\rstoredRequestt\0<Lorg/springframework/security/oauth2/provider/OAuth2Request;L\0userAuthenticationt\02Lorg/springframework/security/core/Authentication;xr\0Gorg.springframework.security.authentication.AbstractAuthenticationTokenӪ(~nGd\0Z\0\rauthenticatedL\0authoritiest\0Ljava/util/Collection;L\0detailst\0Ljava/lang/Object;xp\0sr\0&java.util.Collections$UnmodifiableList�%1��\0L\0listt\0Ljava/util/List;xr\0,java.util.Collections$UnmodifiableCollectionB\0��^�\0L\0cq\0~\0xpsr\0java.util.ArrayListx����a�\0I\0sizexp\0\0\0	w\0\0\0	sr\0Borg.springframework.security.core.authority.SimpleGrantedAuthority\0\0\0\0\0\0�\0L\0rolet\0Ljava/lang/String;xpt\0\nROLE_adminsq\0~\0\rt\0apidocsq\0~\0\rt\0database/logsq\0~\0\rt\0systemsq\0~\0\rt\0user/addsq\0~\0\rt\0user/deletesq\0~\0\rt\0	user/editsq\0~\0\rt\0	user/viewsq\0~\0\rt\0userListxq\0~\0psr\0:org.springframework.security.oauth2.provider.OAuth2Request\0\0\0\0\0\0\0\0Z\0approvedL\0authoritiesq\0~\0L\0\nextensionst\0Ljava/util/Map;L\0redirectUriq\0~\0L\0refresht\0;Lorg/springframework/security/oauth2/provider/TokenRequest;L\0resourceIdst\0Ljava/util/Set;L\0\rresponseTypesq\0~\0$xr\08org.springframework.security.oauth2.provider.BaseRequest6(z>�qi�\0L\0clientIdq\0~\0L\0requestParametersq\0~\0\"L\0scopeq\0~\0$xpt\0webAppsr\0%java.util.Collections$UnmodifiableMap��t�B\0L\0mq\0~\0\"xpsr\0java.util.HashMap���`�\0F\0\nloadFactorI\0	thresholdxp?@\0\0\0\0\0w\0\0\0\0\0\0t\0\ngrant_typet\0passwordt\0usernamet\0adminxsr\0%java.util.Collections$UnmodifiableSet��я��U\0\0xq\0~\0	sr\0java.util.LinkedHashSet�l�Z��*\0\0xr\0java.util.HashSet�D�����4\0\0xpw\0\0\0?@\0\0\0\0\0t\0appxsq\0~\03w\0\0\0?@\0\0\0\0\0\0xsq\0~\0*?@\0\0\0\0\0\0w\0\0\0\0\0\0\0xppsq\0~\03w\0\0\0?@\0\0\0\0\0\0xsq\0~\03w\0\0\0?@\0\0\0\0\0\0xsr\0Oorg.springframework.security.authentication.UsernamePasswordAuthenticationToken\0\0\0\0\0\0�\0L\0credentialsq\0~\0L\0	principalq\0~\0xq\0~\0sq\0~\0sq\0~\0\0\0\0	w\0\0\0	q\0~\0q\0~\0q\0~\0q\0~\0q\0~\0q\0~\0q\0~\0q\0~\0q\0~\0xq\0~\0=sr\0java.util.LinkedHashMap4�N\\l��\0Z\0accessOrderxq\0~\0*?@\0\0\0\0\0w\0\0\0\0\0\0q\0~\0,q\0~\0-q\0~\0.q\0~\0/x\0psr\02org.springframework.security.core.userdetails.User\0\0\0\0\0\0�\0Z\0accountNonExpiredZ\0accountNonLockedZ\0credentialsNonExpiredZ\0enabledL\0authoritiesq\0~\0$L\0passwordq\0~\0L\0usernameq\0~\0xpsq\0~\00sr\0java.util.TreeSetݘP���[\0\0xpsr\0Forg.springframework.security.core.userdetails.User$AuthorityComparator\0\0\0\0\0\0�\0\0xpw\0\0\0	q\0~\0q\0~\0q\0~\0q\0~\0q\0~\0q\0~\0q\0~\0q\0~\0q\0~\0xpt\0admin');

/*Table structure for table `xc_company` */

DROP TABLE IF EXISTS `xc_company`;

CREATE TABLE `xc_company` (
  `id` varchar(32) NOT NULL,
  `linkname` varchar(64) DEFAULT NULL COMMENT '联系人名称',
  `name` varchar(128) NOT NULL COMMENT '名称',
  `mobile` varchar(11) NOT NULL,
  `email` varchar(128) NOT NULL,
  `intro` varchar(512) DEFAULT NULL COMMENT '简介',
  `logo` varchar(128) DEFAULT NULL COMMENT 'logo',
  `identitypic` varchar(128) DEFAULT NULL COMMENT '身份证照片',
  `worktype` varchar(32) DEFAULT NULL COMMENT '工具性质',
  `businesspic` varchar(128) DEFAULT NULL COMMENT '营业执照',
  `status` varchar(32) DEFAULT NULL COMMENT '企业状态',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `xc_company` */

insert  into `xc_company`(`id`,`linkname`,`name`,`mobile`,`email`,`intro`,`logo`,`identitypic`,`worktype`,`businesspic`,`status`) values ('1','张老师','传智播客','13333334444','abc@126.com','2006年创建！',NULL,NULL,NULL,NULL,NULL),('2','李老师','博学谷','','',NULL,NULL,NULL,NULL,NULL,NULL);

/*Table structure for table `xc_company_user` */

DROP TABLE IF EXISTS `xc_company_user`;

CREATE TABLE `xc_company_user` (
  `id` varchar(32) NOT NULL,
  `company_id` varchar(32) NOT NULL,
  `user_id` varchar(32) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `xc_company_user_unique` (`company_id`,`user_id`),
  KEY `FK_xc_company_user_user_id` (`user_id`),
  CONSTRAINT `FK_xc_company_user_company_id` FOREIGN KEY (`company_id`) REFERENCES `xc_company` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `FK_xc_company_user_user_id` FOREIGN KEY (`user_id`) REFERENCES `xc_user` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `xc_company_user` */

insert  into `xc_company_user`(`id`,`company_id`,`user_id`) values ('1','1','49'),('2','2','52');

/*Table structure for table `xc_menu` */

DROP TABLE IF EXISTS `xc_menu`;

CREATE TABLE `xc_menu` (
  `id` varchar(64) NOT NULL,
  `code` varchar(255) DEFAULT NULL COMMENT '菜单编码',
  `p_id` varchar(255) DEFAULT NULL COMMENT '父菜单ID',
  `menu_name` varchar(255) DEFAULT NULL COMMENT '名称',
  `url` varchar(255) DEFAULT NULL COMMENT '请求地址',
  `is_menu` char(1) DEFAULT NULL COMMENT '是否是菜单',
  `level` int(11) DEFAULT NULL COMMENT '菜单层级',
  `sort` int(11) DEFAULT NULL COMMENT '菜单排序',
  `status` char(1) DEFAULT NULL,
  `icon` varchar(255) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `FK_CODE` (`code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `xc_menu` */

insert  into `xc_menu`(`id`,`code`,`p_id`,`menu_name`,`url`,`is_menu`,`level`,`sort`,`status`,`icon`,`create_time`,`update_time`) values ('','xc_teachmanager_course_pic',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),('000000000000000000','root','0','系统根目录',NULL,'0',0,1,'1',NULL,'2017-08-03 18:31:54',NULL),('111111111111111111','xc_sysmanager','000000000000000000','系统管理',NULL,'1',1,10,'1','','2017-08-04 09:47:06',NULL),('222222222222222222','xc_teachmanager','000000000000000000','教学管理',NULL,'1',1,2,'1',NULL,NULL,NULL),('893288715881807872','xc_sysmanager_user','111111111111111111','用户管理',NULL,'1',2,1,'1','','2017-08-04 09:53:21','2017-08-07 18:18:39'),('893304960282787840','xc_sysmanager_user_add','893288715881807872','添加用户',NULL,'1',3,1,'1','','2017-08-04 10:57:54','2017-08-08 11:02:55'),('894396523532517376','xc_sysmanager_user_edit','893288715881807872','用户修改',NULL,'0',3,1,'1','','2017-08-07 11:15:23','2017-08-07 16:57:52'),('894473486712438784','xc_sysmanager_user_view','893288715881807872','用户列表',NULL,'1',3,2,'1','','2017-08-07 16:21:12',NULL),('894473651837992960','xc_sysmanager_user_delete','893288715881807872','用户删除',NULL,'0',3,4,'1','','2017-08-07 16:21:52',NULL),('894475142061621248','xc_sysmanager_role','111111111111111111','角色管理',NULL,'1',2,2,'1','','2017-08-07 16:27:47','2017-08-08 10:34:56'),('894475827880656896','xc_sysmanager_role_add','894475142061621248','角色添加',NULL,'0',3,1,'1','','2017-08-07 16:30:31',NULL),('894475985452269568','xc_sysmanager_role_edit','894475142061621248','角色编辑',NULL,'0',3,2,'1','','2017-08-07 16:31:08',NULL),('894476118730473472','xc_sysmanager_role_delete','894475142061621248','角色删除',NULL,'0',3,2,'1','','2017-08-07 16:31:40','2017-08-07 16:37:24'),('894476276402749440','xc_sysmanager_role_permission','894475142061621248','角色配权',NULL,'0',3,3,'1','','2017-08-07 16:32:18',NULL),('894476950951690240','xc_sysmanager_menu','111111111111111111','菜单管理',NULL,'1',2,2,'1','','2017-08-07 16:34:58',NULL),('894477107919323136','xc_sysmanager_menu_add','894476950951690240','菜单添加',NULL,'0',3,1,'1','','2017-08-07 16:35:36',NULL),('894477244926263296','xc_sysmanager_menu_edit','894476950951690240','菜单编辑',NULL,'0',3,2,'1','','2017-08-07 16:36:08',NULL),('894477420512411648','xc_sysmanager_menu_delete','894476950951690240','菜单删除',NULL,'0',3,2,'1','','2017-08-07 16:36:50',NULL),('894477851082883072','xc_sysmanager_doc','111111111111111111','文档查询',NULL,'1',2,9,'1','','2017-08-07 16:38:33','2017-09-13 11:20:26'),('894477995903811584','xc_sysmanager_log','111111111111111111','add',NULL,'1',2,10,'1','','2017-08-07 16:39:07','2017-08-08 09:56:29'),('894752734459199488','xc_sysmanager_company','111111111111111111','机构管理',NULL,'1',1,1,'1','','2017-08-08 10:50:50',NULL),('903459378655395840','xc_sysmanager_user_resetpwd','893288715881807872','密码重置',NULL,'1',3,2,'1','','2017-09-01 11:27:56',NULL),('903459378655395841','xc_teachmanager_course','222222222222222222','课程管理',NULL,'1',2,1,'1',NULL,NULL,NULL),('903459378655395842','xc_teachmanager_course_add','903459378655395841','添加课程',NULL,'1',3,1,'1',NULL,NULL,NULL),('903459378655395843','xc_teachmanager_course_del','903459378655395841','删除课程',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),('903459378655395845','xc_teachmanager_course_market','903459378655395841','编辑课程营销信息',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),('903459378655395846','xc_teachmanager_course_base','903459378655395841','编辑课程基础信息',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),('903459378655395847','xc_teachmanager_course_plan','903459378655395841','编辑课程计划',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),('903459378655395848','xc_teachmanager_course_publish','903459378655395841','发布课程',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),('903459378655395849','xc_teachmanager_course_list','903459378655395841','我的课程',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),('903459378655395850','course_find_list','903459378655395841','查询课程列表',NULL,NULL,NULL,NULL,'1',NULL,NULL,NULL);

/*Table structure for table `xc_permission` */

DROP TABLE IF EXISTS `xc_permission`;

CREATE TABLE `xc_permission` (
  `id` varchar(32) NOT NULL,
  `role_id` varchar(32) NOT NULL,
  `menu_id` varchar(255) NOT NULL,
  `create_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `xu_permission_unique` (`role_id`,`menu_id`),
  KEY `fk_xc_permission_menu_id` (`menu_id`),
  CONSTRAINT `fk_xc_permission_menu_id` FOREIGN KEY (`menu_id`) REFERENCES `xc_menu` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_xc_permission_role_id` FOREIGN KEY (`role_id`) REFERENCES `xc_role` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `xc_permission` */

insert  into `xc_permission`(`id`,`role_id`,`menu_id`,`create_time`) values ('11','20','222222222222222222',NULL),('11101','20','903459378655395850',NULL),('12','20','903459378655395841',NULL),('13','20','903459378655395842',NULL),('14','20','903459378655395843',NULL),('15','20','903459378655395845',NULL),('16','20','903459378655395846',NULL),('17','20','903459378655395847',NULL),('18','20','903459378655395848',NULL),('19','20','903459378655395849',NULL),('89328714465778073617','17','111111111111111111','2017-09-14 18:40:48'),('8932871446577807366','6','111111111111111111','2017-08-08 11:31:39'),('8932871446577807367','6','903459378655395846',NULL),('8932871446577807368','8','111111111111111111','2017-08-08 11:56:44'),('8932887158818078726','6','893288715881807872','2017-08-08 11:31:39'),('8932887158818078728','8','893288715881807872','2017-08-08 11:56:44'),('8933049602827878406','6','893304960282787840','2017-08-08 11:31:39'),('8933049602827878408','8','893304960282787840','2017-08-08 11:56:44'),('8943965235325173766','6','894396523532517376','2017-08-08 11:31:39'),('8943965235325173768','8','894396523532517376','2017-08-08 11:56:44'),('8944734867124387846','6','894473486712438784','2017-08-08 11:31:39'),('8944734867124387848','8','894473486712438784','2017-08-08 11:56:44'),('8944736518379929606','6','894473651837992960','2017-08-08 11:31:39'),('8944736518379929608','8','894473651837992960','2017-08-08 11:56:44'),('8944751420616212488','8','894475142061621248','2017-08-08 11:56:44'),('8944758278806568968','8','894475827880656896','2017-08-08 11:56:44'),('8944759854522695688','8','894475985452269568','2017-08-08 11:56:44'),('8944761187304734728','8','894476118730473472','2017-08-08 11:56:45'),('8944762764027494408','8','894476276402749440','2017-08-08 11:56:45'),('8944769509516902408','8','894476950951690240','2017-08-08 11:56:45'),('8944771079193231368','8','894477107919323136','2017-08-08 11:56:45'),('8944772449262632968','8','894477244926263296','2017-08-08 11:56:45'),('8944774205124116488','8','894477420512411648','2017-08-08 11:56:45'),('89447785108288307217','17','894477851082883072','2017-09-14 18:40:51'),('8944778510828830726','6','894477851082883072','2017-08-08 11:31:39'),('8944778510828830728','8','894477851082883072','2017-08-08 11:56:45'),('89447799590381158417','17','894477995903811584','2017-09-14 18:40:53'),('8944779959038115846','6','894477995903811584','2017-08-08 11:31:39'),('8944779959038115848','8','894477995903811584','2017-08-08 11:56:45'),('89475273445919948817','17','894752734459199488','2017-09-14 18:40:54'),('8947527344591994888','8','894752734459199488','2017-08-08 11:56:45'),('8947692177635409926','6','903459378655395842','2017-08-08 11:56:45'),('8947692177635409930','6','903459378655395841',NULL);

/*Table structure for table `xc_role` */

DROP TABLE IF EXISTS `xc_role`;

CREATE TABLE `xc_role` (
  `id` varchar(32) NOT NULL,
  `role_name` varchar(255) DEFAULT NULL,
  `role_code` varchar(255) DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  `status` char(1) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `unique_role_name` (`role_name`),
  UNIQUE KEY `unique_role_value` (`role_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `xc_role` */

insert  into `xc_role`(`id`,`role_name`,`role_code`,`description`,`create_time`,`update_time`,`status`) values ('17','学生','student',NULL,'2018-03-19 15:07:13','2018-03-19 15:07:13','1'),('18','老师','teacher',NULL,'2018-03-19 15:07:13','2018-03-19 15:07:13','1'),('20','教学管理员','teachmanager',NULL,'2018-03-19 15:07:13','2018-03-19 15:07:13','1'),('6','管理员','admin',NULL,'2018-03-19 15:07:13','2018-03-19 15:07:13','1'),('8','超级管理员','super',NULL,'2018-03-19 15:07:13','2018-03-19 15:07:13','1');

/*Table structure for table `xc_teacher` */

DROP TABLE IF EXISTS `xc_teacher`;

CREATE TABLE `xc_teacher` (
  `id` varchar(32) NOT NULL,
  `user_id` varchar(32) NOT NULL COMMENT '用户id',
  `name` varchar(64) NOT NULL COMMENT '称呼',
  `intro` varchar(512) DEFAULT NULL COMMENT '个人简介',
  `resume` varchar(1024) DEFAULT NULL COMMENT '个人简历',
  `pic` varchar(128) DEFAULT NULL COMMENT '老师照片',
  PRIMARY KEY (`id`),
  UNIQUE KEY `xu_teacher_user_id` (`user_id`),
  CONSTRAINT `fk_xc_teacher_user_id` FOREIGN KEY (`user_id`) REFERENCES `xc_user` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `xc_teacher` */

/*Table structure for table `xc_user` */

DROP TABLE IF EXISTS `xc_user`;

CREATE TABLE `xc_user` (
  `id` varchar(32) NOT NULL,
  `username` varchar(45) NOT NULL,
  `password` varchar(96) NOT NULL,
  `salt` varchar(45) DEFAULT NULL,
  `name` varchar(45) NOT NULL,
  `userpic` varchar(255) DEFAULT NULL COMMENT '头像',
  `utype` varchar(32) NOT NULL,
  `birthday` datetime DEFAULT NULL,
  `sex` char(1) DEFAULT NULL,
  `email` varchar(45) DEFAULT NULL,
  `phone` varchar(45) DEFAULT NULL,
  `qq` varchar(32) DEFAULT NULL,
  `status` varchar(32) NOT NULL COMMENT '用户状态',
  `create_time` datetime NOT NULL,
  `update_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `unique_user_username` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `xc_user` */

insert  into `xc_user`(`id`,`username`,`password`,`salt`,`name`,`userpic`,`utype`,`birthday`,`sex`,`email`,`phone`,`qq`,`status`,`create_time`,`update_time`) values ('46','super','$2a$10$TJ4TmCdK.X4wv/tCqHW14.w70U3CC33CeVncD3SLmyMXMknstqKRe',NULL,'超级管理员',NULL,'101003',NULL,'1',NULL,NULL,NULL,'1','2018-03-07 16:27:47',NULL),('48','admin','$2a$10$TJ4TmCdK.X4wv/tCqHW14.w70U3CC33CeVncD3SLmyMXMknstqKRe',NULL,'系统管理员',NULL,'101003',NULL,'1',NULL,NULL,NULL,'1','2018-03-07 16:27:47',NULL),('49','itcast','$2a$10$TJ4TmCdK.X4wv/tCqHW14.w70U3CC33CeVncD3SLmyMXMknstqKRe',NULL,'test02',NULL,'101002',NULL,'1','','12345',NULL,'1','2018-03-07 16:27:47',NULL),('50','stu1','$2a$10$pLtt2KDAFpwTWLjNsmTEi.oU1yOZyIn9XkziK/y/spH5rftCpUMZa',NULL,'学生1',NULL,'101001',NULL,'1',NULL,NULL,NULL,'1','2018-03-07 16:27:47',NULL),('51','stu2','$2a$10$nxPKkYSez7uz2YQYUnwhR.z57km3yqKn3Hr/p1FR6ZKgc18u.Tvqm',NULL,'学生2',NULL,'101001',NULL,'1',NULL,NULL,NULL,'1','2018-03-07 16:27:47',NULL),('52','t1','$2a$10$TJ4TmCdK.X4wv/tCqHW14.w70U3CC33CeVncD3SLmyMXMknstqKRe',NULL,'老师1',NULL,'101002',NULL,'1',NULL,NULL,NULL,'','2018-03-07 16:27:47',NULL);

/*Table structure for table `xc_user_role` */

DROP TABLE IF EXISTS `xc_user_role`;

CREATE TABLE `xc_user_role` (
  `id` varchar(32) NOT NULL,
  `user_id` varchar(32) DEFAULT NULL,
  `role_id` varchar(32) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `creator` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_xc_user_role_user_id` (`user_id`),
  KEY `fk_xc_user_role_role_id` (`role_id`),
  CONSTRAINT `fk_xc_user_role_role_id` FOREIGN KEY (`role_id`) REFERENCES `xc_role` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_xc_user_role_user_id` FOREIGN KEY (`user_id`) REFERENCES `xc_user` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `xc_user_role` */

insert  into `xc_user_role`(`id`,`user_id`,`role_id`,`create_time`,`creator`) values ('1','46','8','2017-09-11 13:02:45','超级管理员'),('19','50','6','2017-09-12 14:20:20','超级管理员'),('2','48','6','2017-09-11 13:02:56','超级管理员'),('20','50','17','2017-09-12 14:20:20','超级管理员'),('21','52','20',NULL,NULL),('3','49','20','2017-09-11 13:03:12',NULL);

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
