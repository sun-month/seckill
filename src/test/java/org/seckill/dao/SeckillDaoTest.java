package org.seckill.dao;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.seckill.entity.Seckill;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:spring/spring-dao.xml"})
public class SeckillDaoTest {

	@Resource
	private SeckillDao seckillDao;
	
	@Test
	public void testQueryById() {
		int sekillId = 1000;
		Seckill seckill = seckillDao.queryById(sekillId);
		System.out.println(seckill);
	}

	@Test
	public void testQueryAll(){
		List<Seckill> seckills = seckillDao.queryAll(0, 100);
		for (Seckill seckill : seckills) {
			System.out.println(seckill);
		}
	}
	
	@Test
	public void testReduceNumber(){
		Date killTime = new Date();
		seckillDao.reduceNumber(1000L, killTime);
	}
	
}