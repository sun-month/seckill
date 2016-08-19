package org.seckill.service;

import java.util.List;

import org.seckill.dto.Exposer;
import org.seckill.dto.SeckillExecution;
import org.seckill.entity.Seckill;
import org.seckill.exception.RepeatKillException;
import org.seckill.exception.SeckillCloseException;
import org.seckill.exception.SeckillException;

/**
 * 站在使用者的角度设计接口 三个方面：方法名，参数，返回类型/异常
 * 
 * @author Administrator
 *
 */
public interface SeckillService {

	/**
	 * 返回所有的秒杀商品列表
	 * 
	 * @return
	 */
	List<Seckill> getSeckillList();

	/**
	 * 根据Id返回单个秒杀商品信息
	 * 
	 * @param seckillId
	 * @return
	 */
	Seckill getSeckillById(long seckillId);

	/**
	 * 秒杀开启时输出秒杀接口地址 否则输出系统时间和秒杀时间
	 * 
	 * @param seckillId
	 * @return
	 */
	Exposer exportSeckillURL(long seckillId);

	/**
	 * 执行秒杀操作
	 * 
	 * @param seckillId
	 * @param userPhone
	 * @param md5
	 * @return
	 */
	SeckillExecution executeSeckill(long seckillId, long userPhone, String md5)
			throws SeckillException, RepeatKillException, SeckillCloseException;
	
	/**
	 * 执行秒杀操作Procedure
	 * 
	 * @param seckillId
	 * @param userPhone
	 * @param md5
	 * @return
	 */
	SeckillExecution executeSeckillProcedure(long seckillId, long userPhone, String md5)
			throws SeckillException, RepeatKillException, SeckillCloseException;

}
