package com.jt.sso.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.druid.util.StringUtils;
import com.jt.common.vo.SysResult;
import com.jt.sso.pojo.User;
import com.jt.sso.service.UserService;

import redis.clients.jedis.JedisCluster;

@Controller
@RequestMapping("/user")
public class UserController {

	@Autowired
	private UserService userService;
	@Autowired
	private JedisCluster jedisCluster;
	
	
	/**
	 * 注册时，查看用户是否存在
	 * @param param
	 * @param type
	 * @param callback
	 * @return
	 * JSONP格式返回   param 表示校验参数   type表示校验类型
	 */
	@RequestMapping("/check/{param}/{type}")
	@ResponseBody
	public MappingJacksonValue checkUser(
			@PathVariable String param,
			@PathVariable int type,
			String callback
			){
		boolean flag = userService.findCheckUser(param,type);
		MappingJacksonValue jacksonValue = 
					new MappingJacksonValue(SysResult.oK(flag));
		jacksonValue.setJsonpFunction(callback);
		return jacksonValue;
	}
	
	
	//编辑sso后台用户注册业务
	@RequestMapping("/register")
	@ResponseBody
	public SysResult saveUser(User user){
		System.out.println("这里是SSO后台注册业务");
		try {
			userService.saveUser(user);
			return SysResult.oK();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return SysResult.build(201,"注册失败！！");
	}
	
	//实现SSO用户登录
	@RequestMapping("/login")
	@ResponseBody
	public SysResult findUserByUP(User user){
		try {
			String token = userService.findUserByUP(user);
			if(StringUtils.isEmpty(token)){
				return SysResult.build(201,"用户查询失败");
			}
			return SysResult.oK(token);
		} catch (Exception e) {
			e.printStackTrace();
			return SysResult.build(201,"用户查询失败");
		}
	}
	
	/**
	 * 用户登录后状态显示
	 * @param token
	 * @param callback
	 * @return
	 */
	@RequestMapping("/query/{token}")
	@ResponseBody
	public MappingJacksonValue findUserByTicket(@PathVariable String token,String callback){
		String userJSON = jedisCluster.get(token);
		MappingJacksonValue jacksonValue = null;
		if(!StringUtils.isEmpty(userJSON)){
			//redis中的数据不为空
			jacksonValue = new MappingJacksonValue(SysResult.oK(userJSON));
		}else{
			jacksonValue = new MappingJacksonValue(SysResult.build(201,"用户查询失败"));
		}
		jacksonValue.setJsonpFunction(callback);
		return jacksonValue;
	}
	
	
	
	
	
	
	
	
	
	
	
}
