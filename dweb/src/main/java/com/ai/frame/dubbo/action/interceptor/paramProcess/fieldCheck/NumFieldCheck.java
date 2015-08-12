package com.ai.frame.dubbo.action.interceptor.paramProcess.fieldCheck;

import com.ai.frame.dubbo.common.util.StringUtil;

public class NumFieldCheck extends AbstractorFieldCheck{
	public NumFieldCheck(String fieldCheckCode) {
		super(fieldCheckCode);
	}

	@Override
	protected void validateField(String fieldName, String fieldVal) throws FieldCheckException {
		if(StringUtil.isEmpty(fieldVal)){
			return;
		}
		
		boolean flg = fieldVal.matches("^-?[0-9]*\\.?[0-9]+$");
		if(!flg){
			throwException(fieldName,ERROR_CHECK);
		}
	}
}
