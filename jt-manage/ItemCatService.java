package com.jt.manage.service;

import java.util.List;

import com.jt.manage.vo.EasyUITree;

public interface ItemCatService {

	/**
	 * 数据库搜索
	 * @param parentId
	 * @return
	 */
	List<EasyUITree> findItemCatList(Long parentId);

	/**
	 * 通过缓存操作
	 * @param parentId
	 * @return
	 */
	List<EasyUITree> findItemCatCache(Long parentId);




}
