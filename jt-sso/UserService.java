package com.jt.sso.service;

import com.jt.sso.pojo.User;

public interface UserService {

	/**
	 * 查找用户的信息，是否存在
	 * @param param
	 * @param type
	 * @return
	 */
	boolean findCheckUser(String param, int type);

	void saveUser(User user);

	String findUserByUP(User user);

}
