package com.jt.web.controller;

import java.util.List;

import javax.management.RuntimeErrorException;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jt.common.vo.SysResult;
import com.jt.web.pojo.Cart;
import com.jt.web.pojo.Order;
import com.jt.web.service.CartService;
import com.jt.web.service.OrderService;
import com.jt.web.thread.UserThreadLocal;

@Controller
@RequestMapping("/order")
public class OrderController {

	@Autowired
	private CartService cartService;
	
	@Autowired
	private OrderService orderService;
	/**
	 * 订单
	 * @return
	 */
	@RequestMapping("/create")
	public String toCreate(Model model){
		//展现购物车的商品数据
		Long userId = UserThreadLocal.get().getId();
		List<Cart> carts = cartService.findCartByUserId(userId);
		model.addAttribute("carts",carts);
		return "order-cart";
	}
	
	/**
	 * 实现订单入库
	 * @return
	 * /service/order/submit
	 */
	@RequestMapping("/submit")
	@ResponseBody
	public SysResult saveOrder(Order order){
		try {
			Long userId = UserThreadLocal.get().getId();
			order.setUserId(userId);
			String orderId = orderService.saveOrder(order);
			System.out.println("orderId:"+orderId);
			if(StringUtils.isEmpty(orderId)){
				throw new RuntimeException();
			}
			
			return SysResult.oK(orderId);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return SysResult.build(201,"订单查询失败!");
	}
	
	
	@RequestMapping("/success")
	public String success(String id,Model model){
		Order order = orderService.findOrderById(id);
		model.addAttribute("order",order);
		return "success";
	}
	
	
	
	
	
	
	
	
	
	
	
}
