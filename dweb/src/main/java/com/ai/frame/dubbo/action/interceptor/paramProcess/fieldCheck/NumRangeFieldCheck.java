package com.ai.frame.dubbo.action.interceptor.paramProcess.fieldCheck;

import com.ai.frame.dubbo.common.util.StringUtil;

public class NumRangeFieldCheck extends AbstractorRangeFieldCheck{
	private static final int ERROR_NONUM_CHECK = 2;
	public NumRangeFieldCheck(String fieldCheckCode) {
		super(fieldCheckCode);
	}

	protected void validateField(String fieldName, String fieldVal,int minVal,int maxVal,boolean startWith,boolean endWith) throws FieldCheckException{
		if(StringUtil.isEmpty(fieldVal)){
			return;
		}
		boolean flg = fieldVal.matches("^-?[0-9]*\\.?[0-9]+$");
		if(!flg){
			throwException(fieldName,ERROR_NONUM_CHECK);
		}
		
		double db_num = str2Double(fieldVal,fieldName);
		validateRangeVal(fieldName,db_num,minVal,maxVal,startWith,endWith);
	}
}
