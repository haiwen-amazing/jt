package com.jt.sso.service.impl;

import java.util.Date;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.druid.util.StringUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jt.common.vo.SysResult;
import com.jt.sso.mapper.UserMapper;
import com.jt.sso.pojo.User;
import com.jt.sso.service.UserService;

import redis.clients.jedis.JedisCluster;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserMapper userMapper;
	
	private ObjectMapper objectMapper = new ObjectMapper();
	
	@Autowired
	private JedisCluster jedisCluster;
	/**
	 * 思考：
	 * admin/1 	表示检验用户名为admin的数据
	 * sql: select count(*) from tb_user where username ="admin"
	 */
	@Override
	public boolean findCheckUser(String param, int type) {
		String cloumn = null;
		switch(type){
		case 1:
			cloumn = "username"; break;
		case 2:
			cloumn = "phone"; break;
		case 3:
			cloumn = "email"; break;
		}
		
		int count = userMapper.findCheckUser(param,cloumn);
		//如果返回true，表示用户已经存在
		return count==0?false:true;
	}

	/**
	 * 标记sso新增业务
	 * (数据不全)
	 */
	@Override
	public void saveUser(User user) {
		//
		System.out.println("插入到数据库了！");
		String md5Pass = DigestUtils.md5Hex(user.getPassword());
		user.setPassword(md5Pass);		//将密码进行加密
		user.setEmail(user.getPhone());//暂时代替，否则入库报错
		user.setCreated(new Date());
		user.setUpdated(user.getCreated());
		userMapper.insert(user);
	}

	/**
	 * 编辑SSO 业务实现
	 * 1 根据用户名和密码，查询数据库检查数据是否正确
	 * 		无数据：用户名和密码不正确，直接返回null 或者 throw
	 * 		有数据：用户名和密码是否正确
	 * 2 根据加密的算法生成tokenMD5（“JT_TICKET_”+System.currentTime+username）
	 * 3 将用户信息转化为userJSON数据，将token:userJSON保存到redis中
	 * 4 将token数据返回
	 */
	@Override
	public String findUserByUP(User user) {
		//数据里的密码是密文（MD5）
		user.setPassword(DigestUtils.md5Hex(user.getPassword()));
		User userDB = userMapper.findUserByUP(user);
		
		String returnToken=null;
		if(userDB == null){
			throw new RuntimeException();
		}
		String token = "JT_TICKET"+System.currentTimeMillis()+user.getUsername();
		returnToken = DigestUtils.md5Hex(token);
		
		try {
			String userJSON = objectMapper.writeValueAsString(userDB);
			//保存到redis中
			jedisCluster.setex(returnToken, 3600*24*7,userJSON);
			System.out.println("用户单点登录成功");
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
		return returnToken;
	}


}
