package com.ai.frame.dubbo.common.tools.httpclient.integratedEvt.beans;

public class ErrorInfo {
	private String Message;
	private String Hint;
	private String Code;
	public String getMessage() {
		return Message;
	}
	public void setMessage(String message) {
		Message = message;
	}
	public String getHint() {
		return Hint;
	}
	public void setHint(String hint) {
		Hint = hint;
	}
	public String getCode() {
		return Code;
	}
	public void setCode(String code) {
		Code = code;
	}
	
}
