package com.jt.web.service.impl;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jt.common.service.HttpClientService;
import com.jt.common.vo.SysResult;
import com.jt.web.pojo.User;
import com.jt.web.service.UserService;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private HttpClientService httpClient;
	private static final ObjectMapper objectMapper = new ObjectMapper();
	@Override
	public void saveUser(User user) {
		System.out.println("规矩：前台是不能插入数据的");
		String url = "http://sso.jt.com/user/register";
		Map<String,String> params = new HashMap<>();
		params.put("username",user.getUsername());
		params.put("password",user.getPassword());
		params.put("phone",user.getPhone());
		params.put("email",user.getEmail());
		//前台通过httpClient将数据进行远程传输，如果程序在后台执行错误，前台就获取不到数据
		String result = httpClient.doPost(url,params);
		try {
			//检测返回值结果是否正确
			SysResult sysResult = objectMapper.readValue(result,SysResult.class);
			if(sysResult.getStatus() != 200){
				//表示程序有错
				throw new RuntimeException();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * 1 定义远程url
	 * 2 封装参数
	 * 3 发起httpClient请求resultJSON	sysResultJSON
	 * 4 判断返回值的状态是否为200，如果不是200则抛出异常
	 * 5如果状态码为200，获取里面的token数据之后返回
	 */
	@Override
	public String findUserByUP(User user) {
		String token = null;
		String url = "http://sso.jt.com/user/login";
		Map<String,String> params = new HashMap<>();
		params.put("username",user.getUsername());
		params.put("password",user.getPassword());
		
		String resultJSON = httpClient.doPost(url,params);
		
		try {
			SysResult sysResult =
					objectMapper.readValue(resultJSON,SysResult.class);
			if(sysResult.getStatus()==200){
				//获取token数据
				token = (String)sysResult.getData();
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
		return token;
	}


}
