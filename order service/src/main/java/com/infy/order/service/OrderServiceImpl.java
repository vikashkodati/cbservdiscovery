package com.infy.order.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.infy.order.controller.OrderController;
import com.infy.order.model.Order;
import com.infy.order.repository.OrderRepository;
import com.netflix.appinfo.InstanceInfo.InstanceStatus;
import com.netflix.discovery.EurekaClient;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;

@Service
public class OrderServiceImpl implements OrderService {

	@Autowired
	OrderRepository orderRepo;
	
	@Autowired
	EurekaClient eurekaClient;
	
	@Value(value="${spring.application.name}")
	String appName;
	
	Logger logger = LoggerFactory.getLogger(OrderServiceImpl.class);

	@HystrixCommand(fallbackMethod = "circuitBreakerTest",groupKey="fetchService",commandKey="fetchService")
	@Override
	public Order findByOrderNo(String orderNo) {
		try {
			Thread.sleep(6000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Order order = orderRepo.findOne(orderNo);
		return order;
	}

	@Override
	public Order findByPoNumber(String poNumber) {
		Order order = orderRepo.findByPonumber(poNumber);
		return order;
	}

	@Override
	public List<Order> findByOrderType(String orderType) {
		List<Order> order = orderRepo.findByOrdertype(orderType);
		return order;

	}

	@HystrixCommand(groupKey="fetchService",commandKey="fetchService")
	@Override
	public Order findByCustomerId(String customerId) {
		Order order = orderRepo.findByCustomerid(customerId);
		return order;

	}

	@Override
	public List<Order> findAllOrder() {

		List<Order> orderList = (List<Order>) orderRepo.findAll();

		return orderList;
	}


	@HystrixCommand(groupKey="saveService",commandKey="saveService")
	@Override
	public void saveOrder(List<Order> orders) {
		orderRepo.save(orders);
	}

	@Override
	public void deleteOrder(String orderNumber) {

		orderRepo.delete(orderNumber);
	}

	@Override
	public void markDown() {

		logger.info("marking service as down");
		eurekaClient.getApplication(appName).getInstances().get(0).setStatus(InstanceStatus.DOWN);
	}
	
	@Override
	public void updateOrder(List<Order> orders) {

		orderRepo.save(orders);
	}

	@Override
	public Order circuitBreakerTest(String orderNo) {

		Order orderMock = new Order();
		orderMock.setOrderNumber("182");
		orderMock.setCustomerId("2");
		orderMock.setOrderDate("08-11-2017 09:00:00");
		orderMock.setOrderType("SUB");

		return orderMock;
	}

}
