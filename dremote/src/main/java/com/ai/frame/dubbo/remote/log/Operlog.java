package com.ai.frame.dubbo.remote.log;

import java.util.Date;

public class Operlog {
	/**日志ID*/
	private int logId;
	/**日志类型*/
	private String logType;
	/**操作入参*/
	private String opParam;
	/**操作出参*/
	private String opResp;
	/**操作人*/
	private String operator;
	/**操作时间*/
	private Date opTime;
	/**操作消耗的时长*/
	private long usedTime;
	public int getLogId() {
		return logId;
	}
	public void setLogId(int logId) {
		this.logId = logId;
	}
	public String getLogType() {
		return logType;
	}
	public void setLogType(String logType) {
		this.logType = logType;
	}
	public String getOpParam() {
		return opParam;
	}
	public void setOpParam(String opParam) {
		this.opParam = opParam;
	}
	public String getOpResp() {
		return opResp;
	}
	public void setOpResp(String opResp) {
		this.opResp = opResp;
	}
	public String getOperator() {
		return operator;
	}
	public void setOperator(String operator) {
		this.operator = operator;
	}
	public Date getOpTime() {
		return opTime;
	}
	public void setOpTime(Date opTime) {
		this.opTime = opTime;
	}
	public long getUsedTime() {
		return usedTime;
	}
	public void setUsedTime(long usedTime) {
		this.usedTime = usedTime;
	}
}
