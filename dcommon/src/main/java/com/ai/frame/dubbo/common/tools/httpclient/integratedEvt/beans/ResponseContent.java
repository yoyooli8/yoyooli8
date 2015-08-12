package com.ai.frame.dubbo.common.tools.httpclient.integratedEvt.beans;

public class ResponseContent {
	private ErrorInfo ErrorInfo;
	private Object RetInfo;
	public ResponseContent(){}
	public ResponseContent(String Code,String Message){
		ErrorInfo = new ErrorInfo();
		ErrorInfo.setCode(Code);
		ErrorInfo.setMessage(Message);
		ErrorInfo.setHint(Message);
	}
	public ErrorInfo getErrorInfo() {
		return ErrorInfo;
	}
	public void setErrorInfo(ErrorInfo errorInfo) {
		ErrorInfo = errorInfo;
	}
	public Object getRetInfo() {
		return RetInfo;
	}
	public void setRetInfo(Object retInfo) {
		RetInfo = retInfo;
	}
	
}
