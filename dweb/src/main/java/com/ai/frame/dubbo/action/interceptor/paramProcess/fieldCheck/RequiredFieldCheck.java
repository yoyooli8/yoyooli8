package com.ai.frame.dubbo.action.interceptor.paramProcess.fieldCheck;

import com.ai.frame.dubbo.common.util.StringUtil;

public class RequiredFieldCheck extends AbstractorFieldCheck{
	public RequiredFieldCheck(String fieldCheckCode){
		super(fieldCheckCode);
	}
	
	protected void validateField(String fieldName,String fieldVal)throws FieldCheckException{
		
		if(StringUtil.isEmpty(fieldVal)){
			throwException(fieldName,ERROR_CHECK);
		}
	}
}
