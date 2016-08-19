package org.seckill.dao.redis;

import org.seckill.entity.Seckill;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dyuproject.protostuff.LinkedBuffer;
import com.dyuproject.protostuff.ProtostuffIOUtil;
import com.dyuproject.protostuff.runtime.RuntimeSchema;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

public class RedisDao {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	private RuntimeSchema<Seckill> schema = RuntimeSchema.createFrom(Seckill.class);

	private JedisPool jedisPool;

	public RedisDao(String host, int port) {
		jedisPool = new JedisPool(host, port);
	}

	public Seckill getSeckill(long seckillId) {
		// get-->byte[]-->反序列化-->Object(Seckill)
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			String key = "seckill:" + seckillId;
			byte[] bytes = jedis.get(key.getBytes());
			if (bytes != null) {
				Seckill seckill = schema.newMessage();
				ProtostuffIOUtil.mergeFrom(bytes, seckill, schema);
				return seckill;
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
		} finally {
			jedis.close();
		}
		return null;
	}

	public String putSeckill(Seckill seckill) {
		// Object(Seckill)-->序列化-->byte[]-->put
		Jedis jedis = null;
		String ok = null;
		try {
			jedis = jedisPool.getResource();
			String key = "seckill:" + seckill.getSeckillId();
			byte[] bytes = ProtostuffIOUtil.toByteArray(seckill, schema, LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE));
			int timeout = 60 * 60;
			ok = jedis.setex(key.getBytes(), timeout, bytes);
			
		} catch (Exception e) {
			logger.error(e.getMessage());
		} finally {
			jedis.close();
		}

		return ok;
	}
}
