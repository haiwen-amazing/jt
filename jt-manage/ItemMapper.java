package com.jt.manage.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.jt.common.mapper.SysMapper;
import com.jt.manage.pojo.Item;

public interface ItemMapper extends SysMapper<Item> {

	/**
	 * 查询记录数
	 * @return
	 */
	int findCount();

	/**
	 * 分页查询
	 * @param start		起始位置
	 * @param rows		显示行数
	 * @return
	 * 说明：Mybatis中不允许多值传参，可以将多值封装为单值
	 * 封装策略：1将数据封装为pojo
	 * 			2 将数据封装为Map集合map<key,value>	
	 * 				-->下面写注解主要是这个，底层实现封装为map集合
	 * 			3 将数据封装为Array数组/List集合
	 */			
	List<Item> findItemByPage(
			@Param("start") int start,
			@Param("rows") int rows
			);

	/** 查询商品分类的名字*/
	@Select("select name from tb_item_cat where id=#{itemId}")
	String findItemCatNameById(Long itemId);

	void updateStatus(@Param("ids")Long[] ids,@Param("status")int status);

}
