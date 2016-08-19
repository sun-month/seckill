package org.seckill.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.MapUtils;
import org.seckill.dao.SeckillDao;
import org.seckill.dao.SuccessSeckilledDao;
import org.seckill.dao.redis.RedisDao;
import org.seckill.dto.Exposer;
import org.seckill.dto.SeckillExecution;
import org.seckill.entity.Seckill;
import org.seckill.entity.SuccessKilled;
import org.seckill.enums.SeckillStateEnum;
import org.seckill.exception.RepeatKillException;
import org.seckill.exception.SeckillCloseException;
import org.seckill.exception.SeckillException;
import org.seckill.service.SeckillService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;

@Service
public class SeckillServiceImpl implements SeckillService {

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private RedisDao redisDao;

	@Autowired
	private SeckillDao seckillDao;

	@Autowired
	private SuccessSeckilledDao successSeckilledDao;

	// md5混淆盐值
	private String slat = "ksjhd#&$^*#/-*-*123.,sdfqweoihSJSDFHIU2342348979*(&(%$^$^9*)(*)*&";

	@Override
	public List<Seckill> getSeckillList() {
		return seckillDao.queryAll(0, 5);
	}

	@Override
	public Seckill getSeckillById(long seckillId) {
		return seckillDao.queryById(seckillId);
	}

	@Override
	public Exposer exportSeckillURL(long seckillId) {

		Seckill seckill = redisDao.getSeckill(seckillId);
		if (seckill == null) {
			seckill = seckillDao.queryById(seckillId);

			if (seckill == null)
				return new Exposer(false, seckillId);

			redisDao.putSeckill(seckill);

		}

		Date startTime = seckill.getStartTime();
		Date endTime = seckill.getEndTime();
		Date nowTime = new Date();
		if (nowTime.getTime() < startTime.getTime() || nowTime.getTime() > endTime.getTime())
			return new Exposer(false, seckillId, nowTime.getTime(), startTime.getTime(), endTime.getTime());

		String md5 = getMD5(seckillId);
		return new Exposer(true, md5, seckillId);
	}

	private String getMD5(long seckillId) {
		return DigestUtils.md5DigestAsHex((seckillId + "/" + slat).getBytes());
	}

	@Override
	@Transactional
	public SeckillExecution executeSeckill(long seckillId, long userPhone, String md5)
			throws SeckillException, RepeatKillException, SeckillCloseException {
		if (md5 == null || !md5.equals(getMD5(seckillId)))// 系统异常
			throw new SeckillException("seckill data rewrite");
		// 执行秒杀逻辑：减库存+记录购买行为
		Date killTime = new Date();
		try {
			// 1.先插入购买明细
			int insertCount = successSeckilledDao.insertSuccessSeckilled(seckillId, userPhone);
			if (insertCount <= 0) {// 防止单个用户多次秒杀
				throw new RepeatKillException("seckill repeat");
			} else {
				// 2.再执行减库存操作
				int updateCount = seckillDao.reduceNumber(seckillId, killTime);
				if (updateCount <= 0) {// 秒杀已经结束
					throw new SeckillCloseException("seckill is closed");
				} else {
					SuccessKilled successKilled = successSeckilledDao.queryByIdWithSeckill(seckillId, userPhone);
					return new SeckillExecution(seckillId, SeckillStateEnum.SUCCESS, successKilled);
				}
			}
		} catch (SeckillCloseException e1) {
			throw e1;
		} catch (RepeatKillException e2) {
			throw e2;
		} catch (Exception e) {
			logger.error(e.getMessage());
			// 系统所有编译器异常都转化成运行时异常
			throw new SeckillException("seckill inner error : " + e.getMessage());
		}
	}

	@Override
	public SeckillExecution executeSeckillProcedure(long seckillId, long userPhone, String md5) {
		if (md5 == null && !getMD5(seckillId).equals(md5)) {
			return new SeckillExecution(seckillId, SeckillStateEnum.DATA_REWRITE);
		}
		Date killTime = new Date();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("seckillId", seckillId);
		map.put("userPhone", userPhone);
		map.put("killTime", killTime);
		map.put("result", null);
		try {
			seckillDao.killByProcudure(map);
			int result = MapUtils.getIntValue(map, "result", -2);
			if (result == 1) {
				SuccessKilled successKilled = successSeckilledDao.queryByIdWithSeckill(seckillId, userPhone);
				return new SeckillExecution(seckillId, SeckillStateEnum.SUCCESS, successKilled);
			} else {
				return new SeckillExecution(seckillId, SeckillStateEnum.stateOf(result));
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
			return new SeckillExecution(seckillId, SeckillStateEnum.INNER_ERROR);
		}
	}

}
