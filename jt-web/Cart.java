package com.jt.web.pojo;

import com.jt.common.po.BasePojo;

	
public class Cart extends BasePojo {
	private static final long serialVersionUID = -4395492881257223157L;

	private Long id;			//购物车id
	private Long userId;		//用户id
	private Long itemId;		//商品id
	private String itemTitle;	//商品标题
	private String itemImage;	//图片信息
	private Long itemPrice;		//商品价格
	private Integer num;		//商品数量
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	public Long getItemId() {
		return itemId;
	}
	public void setItemId(Long itemId) {
		this.itemId = itemId;
	}
	public String getItemTitle() {
		return itemTitle;
	}
	public void setItemTitle(String itemTitle) {
		this.itemTitle = itemTitle;
	}
	public String getItemImage() {
		return itemImage;
	}
	public void setItemImage(String itemImage) {
		this.itemImage = itemImage;
	}
	public Long getItemPrice() {
		return itemPrice;
	}
	public void setItemPrice(Long itemPrice) {
		this.itemPrice = itemPrice;
	}
	public Integer getNum() {
		return num;
	}
	public void setNum(Integer num) {
		this.num = num;
	}
	
	
	
	
}
