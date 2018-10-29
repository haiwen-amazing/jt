package com.jt.sso.pojo;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.jt.common.po.BasePojo;

@Table(name="tb_user")
public class User extends BasePojo{
	/**
	 * 
	 */
	private static final long serialVersionUID = 2254853826606540196L;
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;	//用户id编号
	private String username;	//用户名
	private String password;	//密码
	private String phone;		//电话号码
	private String email;		//用户邮箱  但是没有实现用电话号码代替
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	
	
}
