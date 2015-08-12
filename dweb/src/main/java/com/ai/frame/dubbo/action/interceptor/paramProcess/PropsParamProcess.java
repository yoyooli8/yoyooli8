package com.ai.frame.dubbo.action.interceptor.paramProcess;

import com.ai.frame.dubbo.action.interceptor.HttpRequestWrapper;
import com.ai.frame.dubbo.action.interceptor.RequestInterceptor;
import com.ai.frame.dubbo.common.util.StringUtil;
import com.ai.frame.dubbo.dconfig.control.bean.Parameter;
import com.ai.frame.dubbo.spi.InputObject;

public class PropsParamProcess extends AbstractParamProcess{

	public PropsParamProcess(InputObject inputObject,
			HttpRequestWrapper request, RequestInterceptor reqInterceptor) {
		super(inputObject, request, reqInterceptor);
	}
	public ParamProcessResult excute(Parameter param){
		String propval = reqInterceptor.getValueFromProperties(param.getKey());
		if(!StringUtil.isEmpty(propval)){
			inputObject.addParams(param.getKey(), param.getToKey(),propval);
		}
		
		return getSucResult();
	}
}
