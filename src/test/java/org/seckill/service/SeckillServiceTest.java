package org.seckill.service;

import java.util.List;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.seckill.dto.Exposer;
import org.seckill.dto.SeckillExecution;
import org.seckill.entity.Seckill;
import org.seckill.exception.RepeatKillException;
import org.seckill.exception.SeckillCloseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({ "classpath:spring/spring-dao.xml", "classpath:spring/spring-service.xml" })
public class SeckillServiceTest {

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private SeckillService seckillService;

	@Test
	public void getSeckillList() {
		List<Seckill> seckillList = seckillService.getSeckillList();
		logger.debug("seckilllist = {}", seckillList);
	}

	@Test
	public void getSeckillById() {
		Seckill seckill = seckillService.getSeckillById(1000l);
		logger.debug("seckill = {}", seckill);
	}

	@Test
	public void exportSeckillURL() {
		Exposer exposer = seckillService.exportSeckillURL(1001l);
		if (exposer.isExposed()) {
			try {
				SeckillExecution excution = seckillService.executeSeckill(1001L, 13225564484L, exposer.getMd5());
				logger.debug("execution = {}", ReflectionToStringBuilder.toString(excution));
			} catch (SeckillCloseException e) {
				logger.error(e.getMessage());
			} catch (RepeatKillException e) {
				logger.error(e.getMessage());
			}
		} else {
			logger.info("exposer = {}", ReflectionToStringBuilder.toString(exposer));
		}
	}

	@Test
	public void testExecuteSeckillProcedure() {
		Exposer exposer = seckillService.exportSeckillURL(1001L);
		if (exposer.isExposed()) {
			SeckillExecution execution = seckillService.executeSeckillProcedure(1001L, 13213213211L, exposer.getMd5());
			logger.info("StateInfo --> " + execution.getStateInfo());
		}
	}

}
