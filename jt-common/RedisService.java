package com.jt.common.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisSentinelPool;

@Service
public class RedisService {

	//注入分片的池对象
	//required=false使用时才注入对象
	//(因为它需要其他加载之后才能注入，如果是true，没有加载其他就加载这个会报错)
	//应用场景：有一个公用方法，需要注入某个元素，并且这个方法在公用模块里	
	//public ShardedJedisPool shardedJedisPool;
	@Autowired(required=false) 
	public JedisSentinelPool jedisSentinelPool;
	

	public void set(String key,String value){
		Jedis shardedJedis = jedisSentinelPool.getResource();
		shardedJedis.set(key, value);
		shardedJedis.close();
	}
	
	public String get(String key){
		Jedis shardedJedis = jedisSentinelPool.getResource();
		String result = shardedJedis.get(key);
		shardedJedis.close();
		return result;
	}
	/**设定超时时间*/
	public void set(String key,String value,int seconds){
		Jedis shardedJedis = jedisSentinelPool.getResource();
		shardedJedis.setex(key,seconds,value);
		shardedJedis.close();
	}
	
	
}
