package com.ai.frame.dubbo.action.interceptor.paramProcess.fieldCheck;

import java.util.Date;

import com.ai.frame.dubbo.common.util.StringUtil;


public class DateRangeFieldCheck extends DateFieldCheck{
	protected static final int ERROR_NORANGE = 3;
	public DateRangeFieldCheck(String fieldCheckCode) {
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
		Date minDate = null,maxDate = null;
		if(fieldRange.indexOf(",")>-1){
			String[] tmpStr = fieldRange.split(",");
			if(!StringUtil.isEmpty(tmpStr[0])){
				minDate = str2Date(tmpStr[0],fieldName);
			}
			if(!StringUtil.isEmpty(tmpStr[1])){
				maxDate = str2Date(tmpStr[1],fieldName);
			}
		}else{
			minDate = str2Date(fieldRange,fieldName);
		}
		
		validateField(fieldName,fieldValue,minDate,maxDate,startWith,endWith);
	}
	protected void validateField(String fieldName, String fieldVal,Date minVal,Date maxVal,boolean startWith,boolean endWith) throws FieldCheckException{
		Date fieldDate = str2Date(fieldVal,fieldName);
		
		long long_num = fieldDate.getTime();
		long min_num = minVal == null ? 0 : minVal.getTime();
		long max_num = maxVal == null ? Long.MAX_VALUE : maxVal.getTime();
		
		validateRangeVal(fieldName,long_num,min_num,max_num,startWith,endWith);
		
	}
	private Date str2Date(String strDate,String fieldName){
		if(StringUtil.isEmpty(strDate)){
			return null;
		}
		Date date = null;
		//YYYY/MM/DD格式日期
		if(strDate.matches("[0-9]{1,4}[\\/|-][0-9]{1,2}[\\/|-][0-9]{1,2}")){
			date = dateCheckYMD(strDate,fieldName);
		}else if(strDate.matches("[0-9]{1,4}[\\/|-][0-9]{1,2}[\\/|-][0-9]{1,2} [0-9]{1,2}:[0-9]{1,2}:[0-9]{1,2}")){
			//YYYY/MM/DD hh:mm:ss格式日期
			date = dateCheckYMDHMS(strDate,fieldName);
		}else if(strDate.matches("^[0-9]{8}$")){
			//YYYYMMDD格式日期
			strDate = repDate(strDate);
			date = dateCheckYMD(strDate,fieldName);
		}else{
			throwException(fieldName,ERROR_CHECK);
		}
		return date;
	}
}
