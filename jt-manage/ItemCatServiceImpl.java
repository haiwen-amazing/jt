package com.jt.manage.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jt.manage.mapper.ItemCatMapper;
import com.jt.manage.pojo.ItemCat;
import com.jt.manage.service.ItemCatService;
import com.jt.manage.vo.EasyUITree;

import redis.clients.jedis.JedisCluster;

@Service
public class ItemCatServiceImpl implements ItemCatService {

	@Autowired
	private ItemCatMapper itemCatMapper;
/*	@Autowired
	private Jedis jedis;*/
	@Autowired
	//private RedisService redisService;
	private JedisCluster jedisCluster;
	
	/** 工具类只需要一份 */
	private static final ObjectMapper objectMapper = new ObjectMapper();
	/**
	 * 实现思路：
	 * 1.利用通用的mapper查询商品分类信息
	 * 2.将List转化为EasyUITreeList;
	 */
	@Override
	public List<EasyUITree> findItemCatList(Long parentId) {
		ItemCat itemCat = new ItemCat();
		itemCat.setParentId(parentId);
		//Mapper插件执行查询
		List<ItemCat> itemCatList = itemCatMapper.select(itemCat);
		
		List<EasyUITree> treeList = new ArrayList<>();
		
		for (ItemCat itemCatTemp : itemCatList) {
			
			EasyUITree easyTree = new EasyUITree();
			easyTree.setId(itemCatTemp.getId());
			easyTree.setText(itemCatTemp.getName());
			
			String state = itemCatTemp.getIsParent()?"closed":"open";
			easyTree.setState(state);
			
			treeList.add(easyTree);
		}
		
		return treeList;
	}
	@Override
	public List<EasyUITree> findItemCatCache(Long parentId) {
		//1查询缓存（因为id自增，有可能和其他id存在相同，所以加前缀分开）
		String key = "ITEM_CAT_"+parentId;
		String result = jedisCluster.get(key);
		List<EasyUITree> treeList = null;
		try {
			//2判断是否有数据
			if(StringUtils.isEmpty(result)){
				//3 没有就查询数据库，之后将返回的结果转化为Json串，保存到redis
				treeList = findItemCatList(parentId);
				String easyUITreeJson =
						objectMapper.writeValueAsString(treeList);
				jedisCluster.set(key, easyUITreeJson);
				System.out.println("查询数据库的~~~~~~~~~");
			}else{
				//4 如果查询到结果，需要将json串转化成Java对象之后返回
				EasyUITree[] trees =
						objectMapper.readValue(result, EasyUITree[].class);
				treeList = Arrays.asList(trees);
				System.out.println("~~~~~~~~~~查询缓存的");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return treeList;
	}

	
	
	
	
	
	
	
	
	
}
