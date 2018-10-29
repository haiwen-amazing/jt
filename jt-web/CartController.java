package com.jt.web.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jt.common.vo.SysResult;
import com.jt.web.pojo.Cart;
import com.jt.web.service.CartService;
import com.jt.web.thread.UserThreadLocal;

@Controller
@RequestMapping("/cart")
public class CartController {
	
	@Autowired
	private CartService cartService;
	
	/**购物车页面展现*/
	@RequestMapping("/show")
	public String findCartByUserId(Model model){
		Long userId =UserThreadLocal.get().getId();	//暂时写死
		//获取购物车列表信息
		List<Cart> cartList = cartService.findCartByUserId(userId);
		model.addAttribute("cartList", cartList);
		return "cart"; //返回购物车页面
	}
	
	/**
	 * 添加购物车
	 * @param itemId
	 * @param cart
	 * @return
	 */
	@RequestMapping("/add/{itemId}")
	public String saveCart(@PathVariable Long itemId,Cart cart){
		Long userId =UserThreadLocal.get().getId();
		cart.setUserId(userId);
		cart.setItemId(itemId);
		cartService.saveCart(cart);
		//跳转到购物车页面
		return "redirect:/cart/show.html";
	}
	
	/**
	 * 修改购物车数量
	 * @param itemId
	 * @param num
	 * @return
	 */
	@RequestMapping("/update/num/{itemId}/{num}")
	@ResponseBody
	public SysResult updateCartNum(@PathVariable Long itemId,@PathVariable Integer num){
		try {
			//获取用户的id
			Long userId = UserThreadLocal.get().getId();
			
			Cart cart = new Cart();
			cart.setUserId(userId);
			cart.setItemId(itemId);
			cart.setNum(num);
			
			cartService.updateCartNum(cart);
			
			System.out.println("修改成功");
			return SysResult.oK();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return SysResult.build(201,"购物车修改失败");
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	


}
