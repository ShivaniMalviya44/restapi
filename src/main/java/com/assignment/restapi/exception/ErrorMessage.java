package com.assignment.restapi.exception;

import java.util.Date;

public class ErrorMessage {
  
	private Date timestamp;
	private String  message;
	private String  detail;
	
	public ErrorMessage() {
		
	}
    public ErrorMessage( Date timestamp, String  message, String  detail) {
		this.timestamp = timestamp;
		this.message = message;
		this.detail = detail;
	}
	public ErrorMessage(String string, String string2) {
		// TODO Auto-generated constructor stub
	}
	public Date getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getDetail() {
		return detail;
	}
	public void setDetail(String detail) {
		this.detail = detail;
	}
	
}
