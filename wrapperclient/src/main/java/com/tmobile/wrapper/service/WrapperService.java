package com.tmobile.wrapper.service;

import java.net.URISyntaxException;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.tmobile.wrapper.model.Order;

@RestController
public class WrapperService {
	
	
	@Autowired
	WrapperExecution wrapperExecution;
	
	@RequestMapping(path="getOrderDetails/{orderId}")
	public @ResponseBody Object getDetails(@PathVariable String orderId) throws URISyntaxException{
		return wrapperExecution.getOrderDetails(orderId);
	}
	
	@RequestMapping(path="metaData")
	public @ResponseBody Map<String,String> getInfo(){
		return wrapperExecution.getInfo();
	}

}
