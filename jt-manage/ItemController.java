package com.jt.manage.controller;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jt.common.vo.EasyUIResult;
import com.jt.common.vo.SysResult;
import com.jt.manage.pojo.Item;
import com.jt.manage.pojo.ItemDesc;
import com.jt.manage.service.ItemService;

@Controller
@RequestMapping("/item")
public class ItemController {

	private static final Logger logger=Logger.getLogger(ItemController.class);

	@Autowired
	private ItemService itemService;

	@RequestMapping("/query")
	@ResponseBody
	public EasyUIResult findItemByPage(int page,int rows){
		return itemService.findItemByPage(page,rows);
	}

	@RequestMapping(value="/cat/queryItemName",
			produces="text/html;charset=UTF-8"
			)
	//@ResponseBody处理实体对象一般不会乱码，如果处理String可能会乱码
	//因为使用@ResponseBody时，内部有多重的解析的机制，
	//当解析String类型时，默认以ISO-8859-1的格式进行解析
	//解析对象时，默认以UTF-8的格式进行解析
	@ResponseBody
	public String findItemCatNameById(Long itemId){
		return itemService.findItemCatNameById(itemId);
	}

	/** 添加商品 */
	@RequestMapping("/save")
	@ResponseBody
	public SysResult saveItem(Item item,String desc){
		//System.out.println(item);
		try {
			itemService.saveItem(item,desc);
			return SysResult.oK();
		} catch (Exception e) {
			e.printStackTrace();

		}
		return SysResult.build(201,"商品新增失败");
	}

	/** 商品修改 */
	@RequestMapping("/update")
	@ResponseBody
	public SysResult updateItem(Item item,String desc){
		try {
			itemService.updateItem(item,desc);
			//可以加日志打桩
			logger.info("{===============商品更新成功！！}");
			logger.debug("{================商品更新成功！！}");
			return SysResult.oK();
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage());
		}
		return SysResult.build(201,"修改商品失败");
	}

	@RequestMapping("/delete")
	@ResponseBody
	public SysResult deleteItems(Long... ids){
		try {
			itemService.deleteItems(ids);
			return SysResult.oK();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return SysResult.build(201,"修改商品失败");
	}

	/**商品上架*/
	@RequestMapping("/instock")
	@ResponseBody
	public SysResult instock(Long... ids){
		try {
			int status=2;	//表示商品下架
			itemService.updateStatus(ids,status);
			return SysResult.oK();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return SysResult.build(201,"商品下架失败");
	}

	@RequestMapping("/reshelf")
	@ResponseBody
	public SysResult reshelf(Long[] ids){
		try {
			int status=1;	//表示商品上架
			itemService.updateStatus(ids,status);
			return SysResult.oK();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return SysResult.build(201,"商品上架失败");
	}

	/**商品详情回想*/
	@RequestMapping("/query/item/desc/{itemId}")
	@ResponseBody
	public SysResult findItemDescById(@PathVariable("itemId") Long itemId){
		try {
			ItemDesc itemDesc = itemService.findItemDescById(itemId);
			return SysResult.oK(itemDesc);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return SysResult.build(201,"商品查询失败！");
	}
	
	
	
	



}
