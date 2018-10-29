package com.jt.order.job;

import java.util.Date;

import org.joda.time.DateTime;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.quartz.QuartzJobBean;

import com.jt.order.mapper.OrderMapper;

public class PaymentOrderJob extends QuartzJobBean{

	@Override
	protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
		//删除2天天的恶意订单
		ApplicationContext applicationContext = (ApplicationContext) context.getJobDetail().getJobDataMap().get("applicationContext");
		//计算2天前的时间
		Date agoDate = new DateTime().minusDays(2).toDate();
		//获取orderMapper接口
		OrderMapper orderMapper = applicationContext.getBean(OrderMapper.class);
		orderMapper.updateStatus(agoDate);
		System.out.println("定时任务执行成功!!!!");
	}




}
