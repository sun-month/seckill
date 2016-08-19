package org.seckill.dao;

import org.apache.ibatis.annotations.Param;
import org.seckill.entity.SuccessKilled;

public interface SuccessSeckilledDao {

	/**
	 * 插入购买明细，可过滤重复
	 * @param seckillId
	 * @param phone
	 * @return@return如果影响行数>1，表示插入的结果集
	 */
	int insertSuccessSeckilled(@Param("seckillId")long seckillId, @Param("userPhone")long userPhone);
	
	/**
	 * 根据id查询SuccessKilled并携带秒杀产品实体对象
	 * @param seckillId
	 * @return
	 */
	SuccessKilled queryByIdWithSeckill( @Param("seckillId")long seckillId,  @Param("userPhone")long userPhone);
}
