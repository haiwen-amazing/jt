package com.jt.order.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jt.order.mapper.OrderItemMapper;
import com.jt.order.mapper.OrderMapper;
import com.jt.order.mapper.OrderShippingMapper;
import com.jt.order.pojo.Order;
import com.jt.order.pojo.OrderItem;
import com.jt.order.pojo.OrderShipping;
import com.jt.order.service.OrderService;

@Service
public class OrderServiceImpl implements OrderService {

	@Autowired
	private OrderMapper orderMapper;
	@Autowired
	private OrderItemMapper orderItemMapper;
	@Autowired
	private OrderShippingMapper orderShippingMapper;
	
	//登录用户id+当前时间戳
	@Override
	public String saveOrder(Order order) {
		String orderId = ""+order.getUserId()+System.currentTimeMillis();
		System.out.println(order);
		
		Date date = new Date();
		//实现订单入库
		order.setOrderId(orderId);
		order.setStatus(1);		//表示未付款
		order.setCreated(date);
		order.setUpdated(date);
		orderMapper.insert(order);

		System.out.println("订单入库成功");
		
		//实现订单物流入库
		OrderShipping orderShipping = order.getOrderShipping();
		orderShipping.setOrderId(orderId);
		orderShipping.setCreated(date);
		orderShipping.setUpdated(date);
		orderShippingMapper.insert(orderShipping);
		System.out.println("订单物流入库成功");
		
		//实现订单商品入库
		
		List<OrderItem> orderItemList = order.getOrderItems();
		for (OrderItem orderItem : orderItemList) {
			orderItem.setOrderId(orderId);
			orderItem.setCreated(date);
			orderItem.setUpdated(date);
			orderItemMapper.insert(orderItem);
		}
		
		System.out.println("订单商品入库成功！");
		return orderId;
	}

	@Override
	public Order findOrderById(String orderId) {
		Order order = orderMapper.selectByPrimaryKey(orderId);
		OrderShipping orderShipping = 
				orderShippingMapper.selectByPrimaryKey(orderId);
		OrderItem orderItem = new OrderItem();
		orderItem.setOrderId(orderId);
		List<OrderItem> orderItemList = 
				orderItemMapper.select(orderItem);
		//数据封装
		order.setOrderShipping(orderShipping);
		order.setOrderItems(orderItemList);
		
		return order;
	}
	
	
	
	
}
