package com.ai.frame.dubbo.action.struts2;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;

import com.ai.frame.dubbo.action.BaseAction;
import com.ai.frame.dubbo.common.util.StringUtil;
import com.ai.frame.dubbo.spi.InputObject;
import com.ai.frame.dubbo.spi.OutputObject;

public class Strut2BaseAction extends BaseAction {

	@Override
	public HttpServletRequest getRequest() {
		return ServletActionContext.getRequest();
	}

	@Override
	public HttpServletResponse getResponse() {
		return ServletActionContext.getResponse();
	}

	@Override
	public ServletContext getServletContext() {
		return ServletActionContext.getServletContext();
	}
	
	public String excute(){
		InputObject inputObject = getInputObject();
		OutputObject object = getOutputObject(inputObject);
		
		String callback = getRequest().getParameter("callback");
		String respJson = convertOutputObject2Json(object);
		
		if(StringUtil.isEmpty(callback)){
			sendJson(respJson,true);
		}else{
			sendJson(callback+"("+respJson+")");
		}
		
		return null;
	}
	
	public String forwordTo(){
		String forword = getRequest().getParameter("forword");
		
		return forword;
	}
}
