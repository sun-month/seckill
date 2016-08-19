--定义存储过程
DELIMITER $$ --把换行符 ; 转换成 $$
--秒杀事务逻辑
--参数： in:输入参数,out:输出参数
--返回状态：1.秒杀成功。0.秒杀结束。-1.重复秒杀。-2.系统异常。
CREATE PROCEDURE `seckill`.`execute_seckill`
	(IN v_seckill_id BIGINT, IN v_phone BIGINT,
	 IN v_kill_time TIMESTAMP, OUT r_result INT)
	 BEGIN
		 DECLARE insert_count INT DEFAULT 0;
		 START TRANSACTION;
		 INSERT IGNORE INTO `t_success_killed`(`seckill_id`, `user_phone`, `state`, `create_time`) VALUES(v_seckill_id, v_phone, 0, v_kill_time);
		 SELECT ROW_COUNT() INTO insert_count;
		 IF (insert_count = 0) THEN
		 	ROLLBACK;
		 	SET r_result = -1;
		 ELSEIF (insert_count < 0) THEN
		 	ROLLBACK;
		 	SET r_result = -2;
		 ELSE
		 	UPDATE `t_seckill` SET `number` = `number` - 1 WHERE `seckill_id` = v_seckill_id AND v_kill_time < `end_time` AND v_kill_time > `start_time` AND `number` > 0;
		 	SELECT ROW_COUNT() INTO insert_count;
		 	IF(insert_count = 0) THEN
		 		ROLLBACK;
		 		SET r_result = 0;
		 	ELSEIF(insert_count < 0) THEN
		 		ROLLBACK;
		 		SET r_result = -2;
		 	ELSE 
		 		SET r_result = 1;
		 		COMMIT;
		 	END IF;
		 END IF;
	 END ;
$$

DELIMITER ;
SET @r_result = -3;
CALL execute_seckill(1001,12313213112,now(),@r_result);

SELECT @r_result;