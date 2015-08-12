package com.ai.frame.dubbo.action.interceptor.paramProcess.fieldCheck;

import java.util.Locale;
import java.util.ResourceBundle;

import com.ai.frame.dubbo.action.interceptor.RequestInterceptor;
import com.ai.frame.dubbo.common.util.ClassUtil;
import com.ai.frame.dubbo.common.util.StringUtil;


public class FieldCheckFactory {
	private ResourceBundle rsbundle;
	private static final String ERRPREFIX = "msg_";
	private static final String EMPTYSTR  = "";
	private static final FieldCheckFactory instance = new FieldCheckFactory();
	private ThreadLocal<RequestInterceptor> reqInterceptor = new ThreadLocal<RequestInterceptor>();
	public static FieldCheckFactory getInstance(){
		return instance;
	}
	private FieldCheckFactory(){
		rsbundle = ResourceBundle.getBundle("com.ai.frame.web.interceptor.FieldCheck", Locale.getDefault(), FieldCheckFactory.class.getClassLoader());
	}
	
	@SuppressWarnings("rawtypes")
	public void excute(String fieldCheckCode,String fieldName,String fieldVal,RequestInterceptor reqInterceptor){
		int index = fieldCheckCode.indexOf("{");
		if(index == -1){
			index = fieldCheckCode.indexOf("[");
		}
		if(index > -1){
			String oldFieldCheckCode = fieldCheckCode;
			fieldCheckCode = fieldCheckCode.substring(0,index);
			fieldVal       = fieldVal + oldFieldCheckCode.substring(index);
		}
		
		String fieldCheckCls = getfieldCheckCls(fieldCheckCode,reqInterceptor);
		
		Class[]  params    = new Class[]{String.class};
		Object[] paramVals = new Object[]{fieldCheckCode};
		
		FieldCheckI fieldCheck = (FieldCheckI)ClassUtil.getInstance(fieldCheckCls,paramVals,params);
		fieldCheck.fieldCheck(fieldName, fieldVal);
		
		if(StringUtil.isEmpty(fieldCheckCls)){
			throw new FieldCheckException(fieldCheckCode,"fieldCheckCls");
		}
	}
	private void add2ThreadCache(RequestInterceptor reqInterceptor){
		RequestInterceptor cache = this.reqInterceptor.get();
		if (cache == null) {
			this.reqInterceptor.set(reqInterceptor);
		}
	}
	public String getfieldCheckCls(String fieldCheckCode,RequestInterceptor reqInterceptor){
		add2ThreadCache(reqInterceptor);
		String fieldChcls = getCustomePropVal(fieldCheckCode);
		if(!StringUtil.isEmpty(fieldChcls)){
			return fieldChcls;
		}
		try{
			return rsbundle.getString(fieldCheckCode);
		}catch(Exception e){
			return EMPTYSTR;
		}
	}
	private String getCustomePropVal(String key){
		RequestInterceptor reqInterceptor = this.reqInterceptor.get();
		String msgVal = reqInterceptor.getValueFromProperties(key);
		if(!StringUtil.isEmpty(msgVal)){
			return msgVal;
		}
		
		return null;
	}
	public String getCheckErrMsg(String fieldCheckCode){
		String msgKey = ERRPREFIX + fieldCheckCode;
		String msgVal = getCustomePropVal(msgKey);
		if(!StringUtil.isEmpty(msgVal)){
			return msgVal;
		}
		
		try{
			return rsbundle.getString(ERRPREFIX + fieldCheckCode);
		}catch(Exception e){
			return EMPTYSTR;
		}
	}
}
