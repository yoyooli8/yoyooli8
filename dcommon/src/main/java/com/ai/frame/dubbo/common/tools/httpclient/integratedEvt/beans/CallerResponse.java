package com.ai.frame.dubbo.common.tools.httpclient.integratedEvt.beans;

public class CallerResponse {
	public static final String CODE_SUCSS = "0000";
	public static final String CODE_ERROR = "1111";
	private ResponseContent Response;
	public CallerResponse(){}
	public CallerResponse(String Code,String Message){
		Response = new ResponseContent(Code,Message);
	}
	public ResponseContent getResponse() {
		return Response;
	}

	public void setResponse(ResponseContent response) {
		Response = response;
	}
	
}
