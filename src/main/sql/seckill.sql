--秒杀商品列表
CREATE TABLE `t_seckill` (
  `seckill_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '库存商品id',
  `name` varchar(120) NOT NULL COMMENT '商品名称',
  `number` int(11) NOT NULL COMMENT '库存数量',
  `start_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '秒杀开始时间',
  `end_time` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00' COMMENT '秒杀结束时间',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`seckill_id`),
  KEY `idx_start_time` (`start_time`),
  KEY `idx_end_time` (`end_time`),
  KEY `idx_create_time` (`create_time`)
) ENGINE=InnoDB AUTO_INCREMENT=1005 DEFAULT CHARSET=utf8 COMMENT='秒杀库存表'
--插入数据到秒杀商品列表
insert  into 
		`t_seckill`(`seckill_id`,`name`,`number`,`start_time`,`end_time`,`create_time`) 
	values 
		(1000,'1000元秒杀iPhone7',96,'2016-08-18 09:45:11','2016-10-12 00:00:00','2016-08-10 16:56:04'),
		(1001,'500元秒杀ipod2',198,'2016-08-18 17:28:11','2016-10-12 00:00:00','2016-08-10 16:56:04'),
		(1002,'2000元秒杀mac',300,'2016-08-18 17:28:11','2016-10-12 00:00:00','2016-08-10 16:56:04'),
		(1003,'400元秒杀小米5',400,'2016-08-18 17:28:11','2016-10-12 00:00:00','2016-08-10 16:56:04'),
		(1004,'100元秒杀红米note',500,'2016-08-18 17:28:11','2016-10-12 00:00:00','2016-08-10 16:56:04');


--秒杀成功列表
CREATE TABLE `t_success_killed` (
  `seckill_id` bigint(20) NOT NULL COMMENT '秒杀商品id',
  `user_phone` bigint(20) NOT NULL COMMENT '用户手机号',
  `state` tinyint(4) NOT NULL DEFAULT '-1' COMMENT '状态显示：-1：无效，0：成功，1：已付款，2：已发货',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`seckill_id`,`user_phone`),
  KEY `idx_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='秒杀库存表'
