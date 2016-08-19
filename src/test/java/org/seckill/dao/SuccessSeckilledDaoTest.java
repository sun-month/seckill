package org.seckill.dao;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.seckill.entity.SuccessKilled;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:spring/spring-dao.xml"})
public class SuccessSeckilledDaoTest {

	@Resource
	private SuccessSeckilledDao dao;
	
	@Test
	public void testinsertSuccessSeckilled(){
		int count = dao.insertSuccessSeckilled(1001L, 13425497461L);
		System.out.println("count : " + count);
	}
	
	@Test
	public void testqueryByIdWithSeckill(){
		SuccessKilled successKilled = dao.queryByIdWithSeckill(1000L, 13425497461L);
		System.out.println(successKilled);
		System.out.println(successKilled.getSeckill());
	}
	
	
	
}
