package com.ai.frame.dubbo.action.interceptor.paramProcess;

import com.ai.frame.dubbo.spi.InputObject;


public class ParamProcessResult {
	public static final int RTN_SUCESS = 1;
	public static final int RTN_ERROR = 0;
	private int rtnCode;
	private String rtnMsg;
	private InputObject inputObject;

	public ParamProcessResult(InputObject inputObject) {
		this.inputObject = inputObject;
	}

	public ParamProcessResult(int rtnCode, String rtnMsg) {
		this.rtnCode = rtnCode;
		this.rtnMsg = rtnMsg;
	}

	public ParamProcessResult(int rtnCode, String rtnMsg,
			InputObject inputObject) {
		this.rtnCode = rtnCode;
		this.rtnMsg = rtnMsg;
		this.inputObject = inputObject;
	}

	public int getRtnCode() {
		return rtnCode;
	}

	public String getRtnMsg() {
		return rtnMsg;
	}

	public InputObject getInputObject() {
		return inputObject;
	}

	public void setRtnCode(int rtnCode) {
		this.rtnCode = rtnCode;
	}

	public void setRtnMsg(String rtnMsg) {
		this.rtnMsg = rtnMsg;
	}

	public void setInputObject(InputObject inputObject) {
		this.inputObject = inputObject;
	}
}
