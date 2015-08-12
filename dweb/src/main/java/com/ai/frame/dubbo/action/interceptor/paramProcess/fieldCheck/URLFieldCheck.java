package com.ai.frame.dubbo.action.interceptor.paramProcess.fieldCheck;

import com.ai.frame.dubbo.common.util.StringUtil;

public class URLFieldCheck extends AbstractorFieldCheck{
	private static final String URL = "^[h][t]{2}[p][:][\\/][\\/][w]{3}[\\.][0-9A-Za-z]+[\\.][a-z]{2,3}([\\/][0-9A-Za-z]+)+([\\/][0-9A-Za-z]+[.][a-z]+)?$";
	public URLFieldCheck(String fieldCheckCode) {
		super(fieldCheckCode);
	}

	@Override
	protected void validateField(String fieldName, String fieldVal) throws FieldCheckException {
		if(StringUtil.isEmpty(fieldVal))return;
		if(!fieldVal.matches(URL)){
			throwException(fieldName,ERROR_CHECK);
		}
	}
}
