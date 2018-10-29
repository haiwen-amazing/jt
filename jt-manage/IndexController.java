package com.jt.manage.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller

public class IndexController {

	@RequestMapping("/index")
	public String index(){
		return "index";
	}
	
	/**
	 * 通过restFul实现页面的通用跳转
	 * url:
	 * 		/page/item-add
	 * 		/page/item-list
	 * 思考：如果能获取动态变化的数据，则可以实现页面的动态跳转
	 * restFul格式要求：
	 * 			1.参数拼接在url中，并且以"/"进行分割
	 * 			2.参数的位置必须固定
	 * 			3.后台服务端接收参数使用{}包裹变量，之后使用@PathVariable实现数据的动态赋值
	 * @return
	 */
	@RequestMapping("/page/{module}")
	public String module(@PathVariable String module){
		return module;
	}
	
}
