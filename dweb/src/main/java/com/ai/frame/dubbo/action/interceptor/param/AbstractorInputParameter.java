package com.ai.frame.dubbo.action.interceptor.param;

import java.util.List;

import com.ai.frame.dubbo.action.interceptor.HttpRequestWrapper;
import com.ai.frame.dubbo.common.util.Constants;
import com.ai.frame.dubbo.dconfig.control.bean.Input;
import com.ai.frame.dubbo.dconfig.control.bean.Parameter;

public class AbstractorInputParameter implements InputParameterI{
	protected HttpRequestWrapper request;
	protected Input input;
	public AbstractorInputParameter(HttpRequestWrapper request,Input input){
		this.request = request;
		this.input   = input;
	}
	
	public List<Parameter> getParameters() {
		List<Parameter> parameters = input.getParameters();
		
		//add uid param 2 parameters
		Parameter uidparam = new Parameter();
        uidparam.setKey(Constants.UID.getValue());
        uidparam.setToKey(Constants.UID.getValue());
        uidparam.setScope(Constants.PARAM_SCOPE_REQ.getValue());
        
        if(!parameters.contains(uidparam)) parameters.add(uidparam);
		
		return parameters;
	}
}
