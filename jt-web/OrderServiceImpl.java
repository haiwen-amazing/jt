package com.jt.web.service.impl;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jt.common.service.HttpClientService;
import com.jt.common.vo.SysResult;
import com.jt.web.pojo.Order;
import com.jt.web.service.OrderService;

@Service
public class OrderServiceImpl implements OrderService {

	@Autowired
	private HttpClientService httpClient;
	
	private static final ObjectMapper objectMapper = new ObjectMapper();
	
	
	@Override
	public String saveOrder(Order order) {
		String url = "http://order.jt.com/order/create";
		String orderId = null;
		try {
			String orderJSON = objectMapper.writeValueAsString(order);
			Map<String,String> params = new HashMap<>();
			params.put("orderJSON",orderJSON);
			
			//从后台返回值数据,采用SysResult
			String resultJSON = httpClient.doPost(url, params);
			SysResult sysResult = objectMapper.readValue(resultJSON,SysResult.class);
			if(sysResult.getStatus()==200){
				System.out.println("这里是create返回数据"+sysResult.getData());
				orderId = (String)sysResult.getData();
				return orderId;
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
		return orderId;
	}


	@Override
	public Order findOrderById(String id) {
		//http://order.jt.com/order/query/81425700649826
		String url = "http://order.jt.com/order/query/"+id;
		
		String orderJSON = httpClient.doGet(url);
		Order order = null;
		try {
			order = objectMapper.readValue(orderJSON,Order.class);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return order;
	}

}
