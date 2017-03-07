package com.tmobile.wrapper.model;




public class Order {

	private String ordernumber;

	private String orderdate;

	private String ordertype;

	private String customerid;

	private String ponumber;

	private String showstatuscode;

	private String showorderstatusmessage;

	public String getOrderNumber() {
		return ordernumber;
	}

	public String getOrderDate() {
		return orderdate;
	}

	public String getOrderType() {
		return ordertype;
	}

	public String getCustomerId() {
		return customerid;
	}

	public String getPoNumber() {
		return ponumber;
	}

	public String getShowstatusCode() {
		return showstatuscode;

	}

	public String getShowOrderstatusMessage() {
		return showorderstatusmessage;

	}

	public void setOrderNumber(String ordernumber) {
		this.ordernumber = ordernumber;
	}

	public void setOrderDate(String orderdate) {
		this.orderdate = orderdate;
	}

	public void setOrderType(String ordertype) {
		this.ordertype = ordertype;
	}

	public void setCustomerId(String customerid) {
		this.customerid = customerid;
	}

	public void setPoNumber(String ponumber) {
		this.ponumber = ponumber;
	}

	public void setShowstatusCode(String showstatuscode) {
		this.showstatuscode = showstatuscode;
	}

	public void setShowOrderstatusMessage(String showorderstatusmessage) {
		this.showorderstatusmessage = showorderstatusmessage;
	}
}
