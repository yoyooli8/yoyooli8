package com.ai.frame.dubbo.action.interceptor.paramProcess.fieldCheck;

import com.ai.frame.dubbo.common.util.StringUtil;

public abstract class AbstractorRangeFieldCheck extends AbstractorFieldCheck{
	protected static final int ERROR_NORANGE = 1;
	public AbstractorRangeFieldCheck(String fieldCheckCode) {
		super(fieldCheckCode);
	}
	protected void validateField(String fieldName, String fieldVal) throws FieldCheckException {
		if(StringUtil.isEmpty(fieldVal)){
			return;
		}
		if(fieldVal.indexOf("{") == -1 && fieldVal.indexOf("[") == -1){
			throwException(fieldName,ERROR_NORANGE);
		}
		int startIndex = 0;
		boolean startWith = false;
		int endIndex = 0;
		boolean endWith = false;
		startIndex = fieldVal.indexOf("{");
		if(startIndex == -1){
			startIndex = fieldVal.indexOf("[");
			startWith = true;
		}else{
			startWith = false;
		}
		
		endIndex = fieldVal.indexOf("}");
		if(endIndex == -1){
			endIndex = fieldVal.indexOf("]");
			endWith = true;
		}else{
			endWith = false;
		}
		String fieldValue = fieldVal.substring(0,startIndex);
		if(StringUtil.isEmpty(fieldValue)){
			return;
		}
		
		String fieldRange = fieldVal.substring(startIndex + 1,endIndex);
		int minLen = 0,maxLen = Integer.MAX_VALUE;
		if(fieldRange.indexOf(",")>-1){
			String[] tmpStr = fieldRange.split(",");
			if(!StringUtil.isEmpty(tmpStr[0])){
				minLen = str2Int(tmpStr[0],fieldName);
			}
			if(!StringUtil.isEmpty(tmpStr[1])){
				maxLen = str2Int(tmpStr[1],fieldName);
			}
		}else{
			minLen = str2Int(fieldRange,fieldName);
		}
		
		validateField(fieldName,fieldValue,minLen,maxLen,startWith,endWith);
	}
	protected abstract void validateField(String fieldName, String fieldVal,int minVal,int maxVal,boolean startWith,boolean endWith) throws FieldCheckException;
}
