package com.ai.frame.dubbo.action.interceptor.paramProcess.fieldCheck;

@SuppressWarnings("serial")
public class FieldCheckException extends RuntimeException{
	private String fieldCheckCode;
	private String fieldName;
	private int errorNum = 0;
	
	public String getFieldCheckCode() {
		return fieldCheckCode;
	}
	public int getErrorNum() {
		return errorNum;
	}
	public FieldCheckException(FieldCheckException e){
		this.fieldCheckCode = e.getFieldCheckCode();
		this.errorNum = e.getErrorNum();
		this.fieldName = e.getFieldName();
	}
	public FieldCheckException(String fieldCheckCode,String fieldName){
		this.fieldCheckCode = fieldCheckCode;
		this.fieldName = fieldName;
	}
	public FieldCheckException(String fieldCheckCode,String fieldName,int errorNum){
		this.fieldCheckCode = fieldCheckCode;
		this.errorNum = errorNum;
		this.fieldName = fieldName;
	}
	public FieldCheckException(String fieldCheckCode,Throwable cause){
		super(cause);
		this.fieldCheckCode = fieldCheckCode;
	}
	
	public String getMessage(){
		String errorMsg = FieldCheckFactory.getInstance().getCheckErrMsg(fieldCheckCode + errorNum);
		try{
			errorMsg = java.text.MessageFormat.format(errorMsg, fieldName);
		}catch(Exception e){}
		return errorMsg;
	}
	public String getFieldName() {
		return fieldName;
	}
}
