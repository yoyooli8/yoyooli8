package com.ai.frame.dubbo.action.interceptor.paramProcess.fieldCheck;

import com.ai.frame.dubbo.common.util.StringUtil;

public class ForbiddenFieldCheck extends AbstractorFieldCheck{
	private static final String FORBIDDEN = "!<>\"#$'|\\~%";
	public ForbiddenFieldCheck(String fieldCheckCode) {
		super(fieldCheckCode);
	}

	@Override
	protected void validateField(String fieldName, String fieldVal) throws FieldCheckException {
		if(StringUtil.isEmpty(fieldVal))return;
		
		int len = fieldVal.length();
		String sub_check = "";
		boolean flg = false;
		for(int i = 0;i<len;i++){
			int index = FORBIDDEN.indexOf(fieldVal.substring(i,i+1));
			if(index > -1){
				sub_check = FORBIDDEN.substring(index,index+1);
				flg = true;
			}
		}
		if(flg){
			throwException(sub_check,ERROR_CHECK);
		}
	}
}
