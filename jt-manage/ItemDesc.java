package com.jt.manage.pojo;

import javax.persistence.Id;
import javax.persistence.Table;

import com.jt.common.po.BasePojo;

@Table(name="tb_item_desc")
public class ItemDesc extends BasePojo {

	@Id		//设置主键，不用自增，因为和item一对一的，相同
	private Long itemId;			//商品id
	private String itemDesc;	//商品详情

	public String getItemDesc() {
		return itemDesc;
	}
	public void setItemDesc(String itemDesc) {
		this.itemDesc = itemDesc;
	}
	public Long getItemId() {
		return itemId;
	}
	public void setItemId(Long itemId) {
		this.itemId = itemId;
	}
	 

}
