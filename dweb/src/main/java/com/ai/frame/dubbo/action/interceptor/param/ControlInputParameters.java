package com.ai.frame.dubbo.action.interceptor.param;

import com.ai.frame.dubbo.action.interceptor.HttpRequestWrapper;
import com.ai.frame.dubbo.dconfig.control.bean.Input;

public class ControlInputParameters extends AbstractorInputParameter{
	public ControlInputParameters(HttpRequestWrapper request,Input input){
		super(request,input);
	}
}
