package com.ai.frame.dubbo.action.interceptor.paramProcess.fieldCheck;

public abstract class AbstractorFieldCheck implements FieldCheckI{
	public static final int ERROR_CHECK = 0;
	public static final int ERROR_NUM_CHECK = 9;
	protected String fieldCheckCode;
	public AbstractorFieldCheck(String fieldCheckCode){
		this.fieldCheckCode = fieldCheckCode;
	}
	public void fieldCheck(String fieldName, String fieldVal) {
		try{
			validateField(fieldName,fieldVal);
		}catch(Exception e){
			throwException(e,fieldName,ERROR_CHECK);
		}

	}
	protected abstract void validateField(String fieldName, String fieldVal)throws FieldCheckException;
	
	protected int str2Int(String fieldVal,String fieldName){
		try{
			int intVal = Integer.parseInt(fieldVal);
			
			return intVal;
		}catch(Exception e){
			throw new FieldCheckException(fieldCheckCode,fieldName,ERROR_NUM_CHECK);
		}
	}
	protected double str2Double(String fieldVal,String fieldName){
		try{
			double dbVal = Double.parseDouble(fieldVal);
			
			return dbVal;
		}catch(Exception e){
			throw new FieldCheckException(fieldCheckCode,fieldName,ERROR_NUM_CHECK);
		}
	}
	
	protected void throwException(Exception e,String fieldName,int num){
		if(e instanceof FieldCheckException){
			throw new FieldCheckException((FieldCheckException)e);
		}else{
			throw new FieldCheckException(fieldCheckCode,fieldName,num);
		}
	}

	protected void throwException(String fieldName,int num){
		throw new FieldCheckException(fieldCheckCode,fieldName,num);
	}
	
	protected void validateRangeVal(String fieldName,double filedVal,double minVal,double maxVal,boolean startWith,boolean endWith){
		double startVal = filedVal - minVal;
		double endVal   = maxVal   - filedVal;
		
		if(startWith && startVal < 0){
			throwException(fieldName,ERROR_CHECK);
		}
		if(!startWith && startVal <= 0){
			throwException(fieldName,ERROR_CHECK);
		}
		
		if(endWith && endVal < 0){
			throwException(fieldName,ERROR_CHECK);
		}
		if(!endWith && endVal <= 0){
			throwException(fieldName,ERROR_CHECK);
		}
	}
}
