package com.jt.manage.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jt.manage.mapper.UserMapper;
import com.jt.manage.pojo.User;
import com.jt.manage.service.UserService;

@Service
public class UserSeriveImpl implements UserService {

	//@Resource	//单个项目时用哪个都可以，分布式项目的话最好用上面，总的来说，用上面是最好的，这个可以忽略了
	@Autowired
	private UserMapper userMapper;
	
	
	@Override
	public List<User> findAll() {
		
		return userMapper.findAll();
	}

}
