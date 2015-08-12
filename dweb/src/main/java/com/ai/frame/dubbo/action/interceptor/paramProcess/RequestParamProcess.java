package com.ai.frame.dubbo.action.interceptor.paramProcess;

import com.ai.frame.dubbo.action.interceptor.HttpRequestWrapper;
import com.ai.frame.dubbo.action.interceptor.RequestInterceptor;
import com.ai.frame.dubbo.spi.InputObject;

public class RequestParamProcess extends  AbstractParamProcess{

	public RequestParamProcess(InputObject inputObject,
			HttpRequestWrapper request, RequestInterceptor reqInterceptor) {
		super(inputObject, request, reqInterceptor);
	}

}
