package com.ai.frame.dubbo.common.exception;

@SuppressWarnings("serial")
public class AinbException extends RuntimeException{
	private String exceptionName;
	public AinbException(String exceptionName){
		super();
		
		this.exceptionName = exceptionName;
	}
	
	public AinbException(String exceptionName,String message) {
		super(message);
		
		this.exceptionName = exceptionName;
	}
	public AinbException(String exceptionName,Throwable cause) {
        super(cause);
        
        this.exceptionName = exceptionName;
    }
	public AinbException(String exceptionName,String message, Throwable cause) {
        super(message, cause);
        
        this.exceptionName = exceptionName;
    }
	
	public String getMessage() {
		String errMsg = super.getMessage();
		
        return (exceptionName != null) ? (exceptionName + ": " + errMsg) : errMsg;
    }
}
