package com.ai.frame.dubbo.action.interceptor.paramProcess;

import com.ai.frame.dubbo.action.interceptor.HttpRequestWrapper;
import com.ai.frame.dubbo.action.interceptor.RequestInterceptor;
import com.ai.frame.dubbo.action.interceptor.paramProcess.fieldCheck.FieldCheckFactory;
import com.ai.frame.dubbo.common.log.Logger;
import com.ai.frame.dubbo.common.log.LoggerFactory;
import com.ai.frame.dubbo.common.util.StringUtil;
import com.ai.frame.dubbo.dconfig.control.bean.Parameter;
import com.ai.frame.dubbo.spi.InputObject;

public class AbstractParamProcess implements ParamProcessI{
	protected InputObject inputObject;              // 请求参数需要转化的对象
	protected RequestInterceptor reqInterceptor;    // struts2拦截器(框架中自定义的拦截器)
	protected final String forbiddenCheck = "forbidden";
	protected HttpRequestWrapper request;
	protected Logger log = LoggerFactory.getCustomerLog(getClass(), "Param.process");
	public AbstractParamProcess(InputObject inputObject,HttpRequestWrapper request,RequestInterceptor reqInterceptor){
		this.inputObject = inputObject;
		this.reqInterceptor = reqInterceptor;
		this.request = request;
	}
	
	/**请求参数默认实现为httpServletRequest中获取参数**/
	public ParamProcessResult excute(Parameter param){
		String key = param.getKey();
		String[] values = request.getParameterValues(key);
		
		//输入参数验证
		String fieldCheckCode = param.getRegex();
		ParamProcessResult result = checkParamValues(fieldCheckCode,key,values);
		if(result.getRtnCode() == ParamProcessResult.RTN_ERROR) return result;
		
		if(values!=null && values.length == 1){// 单值
			inputObject.addParams(key, param.getToKey(),values[0]);
		}else if(values!=null && values.length > 1){// 多值
			
			inputObject.addParams(key, param.getToKey(), values);
		}
		
		return getSucResult();
	}
	
	protected ParamProcessResult checkParamValues(String fieldCheckCode,String key,String[] values){
		if(values == null) return getSucResult();
		try{
			for(String value:values){
				FieldCheckFactory.getInstance().excute(forbiddenCheck,key,value,reqInterceptor);
				if(!StringUtil.isEmpty(fieldCheckCode)){
					String[] fieldCheck = fieldCheckCode.split(";");
					for(String check:fieldCheck){
						FieldCheckFactory.getInstance().excute(check,key,value,reqInterceptor);
					}
				}
			}
			
			return getSucResult();
		}catch(Exception e){
			return getErrorResult(e.getMessage());
		}
	}
	protected ParamProcessResult getSucResult(){
		ParamProcessResult result = new ParamProcessResult(inputObject);
		result.setRtnCode(ParamProcessResult.RTN_SUCESS);
		
		return result;
	}
	
	protected ParamProcessResult getErrorResult(String errorMsg){
		ParamProcessResult result = new ParamProcessResult(ParamProcessResult.RTN_ERROR,errorMsg);
		
		return result;
	}
}
