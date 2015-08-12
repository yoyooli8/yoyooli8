package com.ai.frame.dubbo.action.struts2;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.dispatcher.multipart.MultiPartRequestWrapper;

import com.ai.frame.dubbo.action.interceptor.HttpRequestWrapper;

public class Struts2HttpRequestWrapper extends HttpRequestWrapper {
	public Struts2HttpRequestWrapper(HttpServletRequest request) {
		super(request);
	}
	@Override
	public Map<String, Object> getParameters() {
		return ServletActionContext.getContext().getParameters();
	}

	@Override
	public boolean isMultiPartRequest() {
		if(request instanceof MultiPartRequestWrapper){
			return true;
		}
		return false;
	}

}
