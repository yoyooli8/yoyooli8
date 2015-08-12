package com.ai.frame.dubbo.action.interceptor.paramProcess.fieldCheck;

import com.ai.frame.dubbo.common.util.StringUtil;

public class EmailFieldCheck extends AbstractorFieldCheck{
	private static final String EMAIL = "\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*";
	public EmailFieldCheck(String fieldCheckCode) {
		super(fieldCheckCode);
	}

	@Override
	protected void validateField(String fieldName, String fieldVal)
			throws FieldCheckException {
		if(StringUtil.isEmpty(fieldVal))return;
		if(!fieldVal.matches(EMAIL)){
			throwException(fieldName,ERROR_CHECK);
		}
	}
}
