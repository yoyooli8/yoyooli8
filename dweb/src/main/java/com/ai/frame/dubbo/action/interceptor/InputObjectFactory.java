package com.ai.frame.dubbo.action.interceptor;

import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

import com.ai.frame.dubbo.action.interceptor.param.InputParameterI;
import com.ai.frame.dubbo.action.interceptor.paramProcess.ParamProcessI;
import com.ai.frame.dubbo.action.interceptor.paramProcess.ParamProcessResult;
import com.ai.frame.dubbo.common.util.ClassUtil;
import com.ai.frame.dubbo.common.util.Constants;
import com.ai.frame.dubbo.common.util.StringUtil;
import com.ai.frame.dubbo.dconfig.control.bean.Input;
import com.ai.frame.dubbo.dconfig.control.bean.Parameter;
import com.ai.frame.dubbo.spi.InputObject;

public class InputObjectFactory {
	private InputObject inputObject;
	private RequestInterceptor reqInterceptor;
	private ResourceBundle rsbundle;
	private static final String REQPREFIX = "req_";
	public InputObjectFactory(){}
	public InputObjectFactory(InputObject inputObject,RequestInterceptor reqInterceptor){
		this.inputObject = inputObject;
		this.reqInterceptor = reqInterceptor;
		
		rsbundle = ResourceBundle.getBundle("com.ai.frame.web.interceptor.InputObject", Locale.getDefault(), InputObjectFactory.class.getClassLoader());
	}
	
	/**请求参数整理封装*/
	@SuppressWarnings("rawtypes")
	public ParamProcessResult excute(Parameter param,HttpRequestWrapper request){
		String paramScope = param.getScope();
		String paramCls   = getDealParamsClass(paramScope);
		
		Class[]  params    = new Class[]{InputObject.class,HttpRequestWrapper.class,RequestInterceptor.class};
		Object[] paramVals = new Object[]{inputObject,request,reqInterceptor};
		ParamProcessI paramProcess = (ParamProcessI)ClassUtil.getInstance(paramCls,paramVals,params);
		return paramProcess.excute(param);
	}
	
	/**取得请求参数*/
	@SuppressWarnings("rawtypes")
	public List<Parameter> getParameters(HttpRequestWrapper request,Input input){
		String paramCls   = getDealReqParametersClass(input);
		Class[]  params    = new Class[]{HttpRequestWrapper.class,Input.class};
		Object[] paramVals = new Object[]{request,input};
		InputParameterI inputParameter = (InputParameterI)ClassUtil.getInstance(paramCls,paramVals,params);
		
		return inputParameter.getParameters();
		
	}
	
	public String getDealReqParametersClass(Input input){
		String scope = input.getScope();
		if(StringUtil.isEmpty(scope)){
			scope = Constants.INPUT_SCOPE_CON.getValue();
		}
		String paramcls = reqInterceptor.getValueFromProperties(REQPREFIX + scope);
		if(StringUtil.isEmpty(paramcls)){
			return rsbundle.getString(REQPREFIX + scope);
		}
		return paramcls;
	}

	public String getDealParamsClass(String paramScope){
		String dealparamcls = reqInterceptor.getValueFromProperties(paramScope);
		if(StringUtil.isEmpty(dealparamcls)){
			return rsbundle.getString(paramScope);
		}
		return dealparamcls;
	}
}
