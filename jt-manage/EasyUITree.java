package com.jt.manage.vo;

public class EasyUITree {

	//为了进行EasyUITree的树形结果展现，需要设定如下树形
	private Long id;		//元素id
	private String text;	//元素文本内容
	private String state;	//"closed"节点默认关闭，"open"便是节点打开
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	@Override
	public String toString() {
		return "EasyUITree [id=" + id + ", text=" + text + ", state=" + state + "]";
	}
	
	
	
	
}
