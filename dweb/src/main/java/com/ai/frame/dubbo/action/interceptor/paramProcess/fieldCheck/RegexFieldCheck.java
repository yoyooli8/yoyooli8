package com.ai.frame.dubbo.action.interceptor.paramProcess.fieldCheck;

import com.ai.frame.dubbo.common.util.StringUtil;

public class RegexFieldCheck extends AbstractorFieldCheck{
	private static final int ERROR_NOREGEX = 1;
	public RegexFieldCheck(String fieldCheckCode) {
		super(fieldCheckCode);
	}

	@Override
	protected void validateField(String fieldName, String fieldVal) throws FieldCheckException {
		if(StringUtil.isEmpty(fieldVal)){
			return;
		}
		if(fieldVal.indexOf("{") == -1 || fieldVal.indexOf("}") == -1){
			throwException(fieldName,ERROR_NOREGEX);
		}
		
		int sindex = fieldVal.indexOf("{");
		int eindex = fieldVal.lastIndexOf("}");
		String fieldValue = fieldVal.substring(0,sindex);
		String regex      = fieldVal.substring(sindex+1,eindex);
		if(StringUtil.isEmpty(regex)){
			return;
		}
		
		boolean flg = fieldValue.matches(regex);
		if(!flg){
			throwException(fieldName,ERROR_CHECK);
		}
		
	}
}
