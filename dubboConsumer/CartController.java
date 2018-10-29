package com.tedu.dubboConsumer;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.tedu.service.CartService;

public class CartController {

	public static void main(String[] args) throws InterruptedException {
		ClassPathXmlApplicationContext context = 
				new ClassPathXmlApplicationContext("applicationContext-consumer.xml");
		CartService cartService = (CartService) context.getBean("cartService");
		while(true){
			String string = cartService.findCartByUserId(18L);
			System.out.println("服务消费者受到的结果:"+string);
			Thread.currentThread().sleep(1000);
		}
		
		
		
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
