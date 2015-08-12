package com.ai.frame.dubbo.action.interceptor.paramProcess.fieldCheck;

public class StringRangeFieldCheck extends AbstractorRangeFieldCheck{
	public StringRangeFieldCheck(String fieldCheckCode) {
		super(fieldCheckCode);
	}

	@Override
	protected void validateField(String fieldName, String fieldVal, int minVal,
			int maxVal, boolean startWith, boolean endWith)
			throws FieldCheckException {
		int byteLen = fieldVal.getBytes().length;
		validateRangeVal(fieldName,byteLen,minVal,maxVal,startWith,endWith);
		
	}
}
