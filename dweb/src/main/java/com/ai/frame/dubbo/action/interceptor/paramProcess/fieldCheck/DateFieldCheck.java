package com.ai.frame.dubbo.action.interceptor.paramProcess.fieldCheck;

import java.util.Calendar;
import java.util.Date;

import com.ai.frame.dubbo.common.util.StringUtil;

public class DateFieldCheck extends AbstractorFieldCheck{
	private static final int ERROR_NODATE     = 1;
	private static final int ERROR_NODATETIME = 2;
	
	public DateFieldCheck(String fieldCheckCode){
		super(fieldCheckCode);
	}
	protected void validateField(String fieldName,String fieldVal)throws FieldCheckException{
		if(StringUtil.isEmpty(fieldVal)){
			return;
		}
		Date date = null;
		if(fieldVal.matches("[0-9]{1,4}[\\/|-][0-9]{1,2}[\\/|-][0-9]{1,2}")){
			//Check YYYY/MM/DD格式日期
			date = dateCheckYMD(fieldVal,fieldName);
		}else if(fieldVal.matches("[0-9]{1,4}[\\/|-][0-9]{1,2}[\\/|-][0-9]{1,2} [0-9]{1,2}:[0-9]{1,2}:[0-9]{1,2}")){
			//Check YYYY/MM/DD hh:mm:ss格式日期
			date = dateCheckYMDHMS(fieldVal,fieldName);
		}else if(fieldVal.matches("^[0-9]{8}$")){
			//Check YYYYMMDD格式日期
			fieldVal = repDate(fieldVal);
			date = dateCheckYMD(fieldVal,fieldName);
		}else{
			throwException(fieldName,ERROR_CHECK);
		}
		
		if(date ==null){
			throwException(fieldName,ERROR_CHECK);
		}
	}
	
	protected String repDate(String date){
		StringBuffer retStr = new StringBuffer();
		try{
			if(StringUtil.isEmpty(date))return "";
			if(date.length() == 8){//YYYYMMDD
				retStr.append(date.substring(0,4));
				retStr.append("/");
				retStr.append(date.substring(4,6));
				retStr.append("/");
				retStr.append(date.substring(6,8));
			}else{
				retStr.append(date);
			}
			
			return retStr.toString();
		}catch(Exception e){
			return "";
		}
	}
	protected Date dateCheckYMDHMS(String fieldVal,String fieldName){
		Calendar cal = Calendar.getInstance();
		try{
			fieldVal = fieldVal.replace('/', '-');
			fieldVal = fieldVal.replace(':', '-');
			fieldVal = fieldVal.replace(' ', '-');
			String[] tmpStr = fieldVal.split("-");
			
			String strYYYY  = tmpStr[0]; // 年
			String strMM    = tmpStr[1]; // 月
			String strDD    = tmpStr[2]; // 日
			String strHH    = tmpStr[3]; // 小时
			String strMI    = tmpStr[4]; // 分
			String strSS    = tmpStr[5]; // 秒
			/**
			 * 指定日期/时间解释是否是宽松的。对于宽松的解释，
			 * 可以将诸如 "February 942, 1996" 之类的日期视为等同于 1996 年 1 月 1 日后的第 941 天。
			 * 而对于严格的（non-lenient）解释，这样的日期会导致抛出异常。默认情况下是宽松的
			 */
			cal.setLenient(false);
			
			cal.set(str2Int(strYYYY,fieldName), 
					str2Int(strMM,fieldName)-1, 
					str2Int(strDD,fieldName),
					str2Int(strHH,fieldName),
					str2Int(strMI,fieldName),
					str2Int(strSS,fieldName)
					);
			
			return cal.getTime();
		}catch(Exception e){
			throwException(e,fieldName,ERROR_NODATETIME);
		}
		return null;
	}
	protected Date dateCheckYMD(String fieldVal,String fieldName){
		Calendar cal = Calendar.getInstance();
		try{
			fieldVal = fieldVal.replace('/', '-');
			String[] tmpStr = fieldVal.split("-");
			
			String strYYYY  = tmpStr[0]; // 年
			String strMM    = tmpStr[1]; // 月
			String strDD    = tmpStr[2]; // 日
			/**
			 * 指定日期/时间解释是否是宽松的。对于宽松的解释，
			 * 可以将诸如 "February 942, 1996" 之类的日期视为等同于 1996 年 1 月 1 日后的第 941 天。
			 * 而对于严格的（non-lenient）解释，这样的日期会导致抛出异常。默认情况下是宽松的
			 */
			cal.setLenient(false);
			
			cal.set(str2Int(strYYYY,fieldName), str2Int(strMM,fieldName)-1, str2Int(strDD,fieldName));
			
			return cal.getTime();
		}catch(Exception e){
			throwException(e,fieldName,ERROR_NODATE);
		}
		return null;
	}
}
