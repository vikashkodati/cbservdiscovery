package com.tmobile.wrapper.service;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.Map;

import org.apache.http.HttpRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import com.netflix.appinfo.InstanceInfo.InstanceStatus;
import com.netflix.discovery.EurekaClient;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.tmobile.wrapper.model.Order;
import com.tmobile.wrapper.model.ServiceStatusInfo;

@RefreshScope
@Service
public class WrapperExecution {
	
	@Autowired
	RestTemplate restTemplate;
	
	@Autowired
	EurekaClient eurekaClient;
	
	@Value(value="${spring.application.name}")
	String appName;
	
	@Value(value="${orderService.name}")
	String serviceName;
	
	static int executed = 0;
	
	Logger logger = LoggerFactory.getLogger(WrapperExecution.class);
	
	@HystrixCommand(fallbackMethod="handleAsFallback", groupKey="orderWrapperService",commandKey="orderWrapperService")
	public Object getOrderDetails(String orderId) throws URISyntaxException{
		executed = 1;
		logger.info("executing wrapper service");
		URI uri = new URI("http://"+serviceName+"/orderservice/order/"+orderId);
		logger.info("invoking service..."+uri);
		Object order = null;
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
			HttpEntity<String> entity = new HttpEntity<String>("parameters", headers);
			order = restTemplate.exchange(uri, HttpMethod.GET, entity, Object.class);
			
		return order;
	}

	protected Object handleAsFallback(String orderId) throws URISyntaxException{
		logger.info("service..."+serviceName+" is not responding, executing fallback");
		String status = null;
		String instanceName = null;
		String instanceHost = null;
		ServiceStatusInfo serviceStatusInfo = new ServiceStatusInfo();
		serviceStatusInfo.setFallbackHandled("true");
		
		/* Just to demonstrate discovery*/
		if(null != eurekaClient.getApplication(serviceName) && null != eurekaClient.getApplication(serviceName).getInstances()
		   && eurekaClient.getApplication(serviceName).getInstances().size()>0){
		status = InstanceStatus.valueOf(eurekaClient.getApplication(serviceName).getInstances().get(0).getStatus().name()).toString();
		instanceName = eurekaClient.getApplication(serviceName).getInstances().get(0).getAppName();
		instanceHost = eurekaClient.getApplication(serviceName).getInstances().get(0).getHostName();
		serviceStatusInfo.setServiceStatus(status);
		serviceStatusInfo.setServiceHost(instanceHost);
		serviceStatusInfo.setServiceName(instanceName);
		serviceStatusInfo.setMessage("Service "+instanceName+" is currently "+status +"and not responding");
		}
		/* Just to demonstrate discovery*/
		
		else{
		serviceStatusInfo.setMessage("Service "+serviceName+" is not available");
		}
		if(executed==0){
			serviceStatusInfo.setMessage("Circuit opened as error rate threshold is met");
		}
		executed=0;
		return serviceStatusInfo;
	}
	
	
	public Map<String,String> getInfo(){
		return eurekaClient.getApplication(appName).getInstances().get(0).getMetadata();
	}
}
