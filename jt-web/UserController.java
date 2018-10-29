package com.jt.web.controller;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.druid.util.StringUtils;
import com.jt.common.vo.SysResult;
import com.jt.web.pojo.User;
import com.jt.web.service.UserService;

import redis.clients.jedis.JedisCluster;

@Controller
@RequestMapping("/user")
public class UserController {
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private JedisCluster jedisCluster;
	

	@RequestMapping("/{moduleName}")
	public String index(@PathVariable String moduleName ){
		return moduleName;
	}
	
	/**
	 * 前台显示注册用户
	 * @param user
	 * @return
	 */
	@RequestMapping("/doRegister")
	@ResponseBody
	public SysResult saveUser(User user){
		try {
			System.out.println("这里是前台注册");
			userService.saveUser(user);
			return SysResult.oK();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return SysResult.build(201,"注册失败");
	}
	
	/**
	 * 实现用户登录操作
	 * @return
	 */
	@RequestMapping("/doLogin")
	@ResponseBody
	public SysResult doLogin(User user,HttpServletResponse response){
		try {
			String token = userService.findUserByUP(user);
			//判断登录是否有效
			if(StringUtils.isEmpty(token)){
				return SysResult.build(201,"用户登录失败！");
			}
			//将token数据保存到Cookie中
			Cookie cookie = new Cookie("JT_TICKET",token);
			cookie.setPath("/");
			//cookie的生命周期（s）：>0 	生命的存活时间;	=0	表示立即删除	; -1 表示当前会话结束删除
			cookie.setMaxAge(3600*24*7);
			
			response.addCookie(cookie);
			
			return SysResult.oK();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return SysResult.build(201,"用户查询失败");
	}
	
	/**
	 * 用户登出操作
	 * 1 删除redis
	 * 2 删除cookie
	 */
	@RequestMapping("/logout")
	public String logout(HttpServletRequest request,HttpServletResponse response){
		//获取cookies
		Cookie[] cookies = request.getCookies();
		String token = null;
		for (Cookie cookie : cookies) {
			if("JT_TICKET".equals(cookie.getName())){
				token = cookie.getValue();
				break;
			}
		}
		//删除redis
		jedisCluster.del(token);
		//删除Cookie
		Cookie cookie = new Cookie("JT_TICKET","");
		cookie.setPath("/");
		cookie.setMaxAge(0);
		response.addCookie(cookie);
		//重定向首页
		//response.sendRedirect("/index.html");
		return "redirect:/index.html" ;
	}
	
	
	
	
}
