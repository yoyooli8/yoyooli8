package com.ai.frame.dubbo.common.tools.httpclient.integratedEvt.beans;

public class BodyRequest {
	private Object BusiParams;
	private String BusiCode;
	public BodyRequest(Object BusiParams,String BusiCode){
		this.BusiCode  = BusiCode;
		this.BusiParams= BusiParams;
	}
	public Object getBusiParams() {
		return BusiParams;
	}
	public void setBusiParams(Object busiParams) {
		BusiParams = busiParams;
	}
	public String getBusiCode() {
		return BusiCode;
	}
	public void setBusiCode(String busiCode) {
		BusiCode = busiCode;
	}
	
}
