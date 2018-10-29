package com.jt.web.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jt.common.service.HttpClientService;
import com.jt.common.vo.SysResult;
import com.jt.web.pojo.Cart;
import com.jt.web.service.CartService;

@Service
public class CartServiceImpl implements CartService {

	@Autowired
	private HttpClientService httpClient;

	private static final ObjectMapper objectMapper = new ObjectMapper();

	/**
	 * 购物车回显
	 */
	@Override
	public List<Cart> findCartByUserId(Long userId) {
		String url = "http://cart.jt.com/cart/query/" + userId;
		String resultJSON = httpClient.doGet(url);
		List<Cart> cartList = null;
		try {
			SysResult sysResult = 
					objectMapper.readValue(resultJSON, SysResult.class);
			cartList = (List<Cart>) sysResult.getData();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return cartList;
	}

	@Override
	public void saveCart(Cart cart) {
		String url="http://cart.jt.com/cart/save";
		Map<String,String> params = new HashMap<>();
		try {
			//为了传参简单，将数据转化为json
			String cartJSON = objectMapper.writeValueAsString(cart);
			params.put("cartJSON", cartJSON);
			httpClient.doPost(url,params);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void updateCartNum(Cart cart) {
		//url
		String url = "http://cart.jt.com/cart/update/num/"+cart.getUserId()+"/"+cart.getItemId()+"/"+cart.getNum();
		httpClient.doGet(url);
	}

}
