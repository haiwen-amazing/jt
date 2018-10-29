package com.jt.sso.mapper;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.jt.common.mapper.SysMapper;
import com.jt.sso.pojo.User;

public interface UserMapper extends SysMapper<User> {

	/**
	 * 根据参数，查找并返回记录数
	 * @param param	参数
	 * @param cloumn 类型
	 * @return	记录数
	 */
	int findCheckUser(@Param("param")String param, @Param("cloumn")String cloumn);

	@Select("select * from tb_user where username=#{username} and password=#{password}")
	User findUserByUP(User user);

}
