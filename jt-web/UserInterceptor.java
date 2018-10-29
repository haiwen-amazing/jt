package com.jt.web.intercept;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.druid.util.StringUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jt.web.pojo.User;
import com.jt.web.thread.UserThreadLocal;

import redis.clients.jedis.JedisCluster;

public class UserInterceptor implements HandlerInterceptor {

	@Autowired
	private JedisCluster jedisCluster;
	
	private static final ObjectMapper objectMapper = new ObjectMapper();
	
	
	/**
	 * 在调用Controller之前拦截
	 * boolean 代表 true放行	false拦截
	 * 拦截器使用用户登录校验
	 * 1 获取客户端的Cookie
	 * 2 判断cookie是否有token
	 * 3 判断redis中是否有用户的信息
	 * 如果用户都满足要求则放行，否则跳转登录
	 */
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		//1 获取客户端cookie
		Cookie[] cookies = request.getCookies();
		//2 获取token
		String token = null;
		for (Cookie cookie : cookies) {
			System.out.println(cookie);
			if("JT_TICKET".equals(cookie.getName())){
				token = cookie.getValue();
				System.out.println(token);
				break;
			}
		}
		
		if(!StringUtils.isEmpty(token)){
			//表示用户还有token，接着判断redis是否有shuju 
			String userJSON = jedisCluster.get(token);
			if(!StringUtils.isEmpty(userJSON)){
				User user = objectMapper.readValue(userJSON,User.class);
				UserThreadLocal.set(user);
				
				//代表用户已经登录的
				return true;
			}
		}
		//如果执行到这里，表示用户没有登录或者登录有误，只能重定向到登录页面
		response.sendRedirect("/user/login.html");
		return false;
	}

	/**
	 * 在业务逻辑完成后拦截
	 */
	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		// TODO Auto-generated method stub
		
	}

	/**
	 * 在业务逻辑执行之后，返回给客户端之前拦截
	 */
	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		UserThreadLocal.remove();
		
	}

}
