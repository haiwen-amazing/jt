package com.jt.manage.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Select;

import com.jt.manage.pojo.User;

public interface UserMapper {
	
	//查到user表中的数据
	//@Select("select * from user")
	List<User> findAll();
}
